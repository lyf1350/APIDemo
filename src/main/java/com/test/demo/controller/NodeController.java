package com.test.demo.controller;

import com.test.demo.common.JsonResult;
import com.test.demo.model.*;
import com.test.demo.repository.*;
import com.test.demo.repository.result.NodeReviewer;
import com.test.demo.service.NodeService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.*;

@RestController
@RequestMapping("/api/node")
@Slf4j
public class NodeController {

    @Autowired
    SignoffRepository signoffRepository;
    @Autowired
    WorkflowLogRepository workflowLogRepository;
    @Autowired
    NodeRepository nodeRepository;
    @Autowired
    WorkflowRepository workflowRepository;

    @Autowired
    NodeService nodeService;

    @Autowired
    PropertyRepository propertyRepository;

    private void setNodes(List<Node> nodes,Set<Node> nodeSet,Set<Node> notSet){
        nodes.forEach(node -> {
            if(!node.getNodeStatus().equals("未开始")){
                node.setNodeStatus("未开始");
                node.setStartTime(null);
                node.setEndTime(null);
                node.getSignoffs().forEach(signoff -> signoff.setSignoffStatus("未做决定"));
                nodeSet.add(node);
                notSet.add(node);
                setNodes(node.getNextNodes(),nodeSet,notSet);
            }
        });
    }


    @PostMapping("/execute")
    @ApiOperation(value="执行节点",notes="拒绝则重新开始此流程，同意则如果同级节点都完成，则到下一个节点")
    public JsonResult executeNode(Long id, String decision, String remark,String property, @SessionAttribute User user) {

        Node node=nodeRepository.findById(id).get();
        Property prop=node.getWorkflow().getProperty();
        prop.setProperty(property);
        propertyRepository.save(prop);
        log.info("node:"+node+"\ndecision:"+decision+" remark:"+remark);
        Timestamp now = new Timestamp(new Date().getTime());
        WorkflowLog workflowLog = new WorkflowLog(node.getWorkflow().getId(), node, user, decision, node.getStartTime(), now, remark);
        workflowLogRepository.save(workflowLog);
        if (decision.equals("拒绝")) {
            Node startNode=nodeService.getStartNode(node);
            node.setNodeStatus("未开始");
            Set<Node> nodeSet=new HashSet<>();
            Set<Node> notSet=new HashSet<>();
            for(Node node1:startNode.getNextNodes()){
                if(!node1.getNodeStatus().equals("已开始")){
                    for(Action action:node1.getActions()){
                        Object obj=action.execute("pre");
                        if(obj!=null){
                            return JsonResult.error(obj.toString());
                        }
                    }
                    node1.setNodeStatus("已开始");
                    node1.setStartTime(now);
                    node1.setEndTime(null);
                    node1.getSignoffs().forEach(signoff -> signoff.setSignoffStatus("未做决定"));
                    nodeSet.add(node1);
                    notSet.add(node1);
                    setNodes(node1.getNextNodes(),nodeSet,notSet);
                }
            }

            log.info("nodeset:"+nodeSet);
            notSet.forEach(node1 -> {
                nodeService.notifyUser(node1,0);
                signoffRepository.saveAll(node1.getSignoffs());
            });
            nodeRepository.saveAll(nodeSet);
            return JsonResult.success();
        }
        Set<Group> groupSet = new HashSet<>();
        Set<Role> roleSet = new HashSet<>();
        List<Member> members = user.getMembers();

        members.forEach(member -> {
            groupSet.add(member.getGroup());
            roleSet.add(member.getRole());
        });
        int signoffCnt = 0;
        List<Signoff> signoffs=new ArrayList<>();
        for (Signoff signoff : node.getSignoffs()) {
            if (!signoff.getSignoffStatus().equals("未做决定"))
                continue;
            signoffCnt++;
            boolean approve=false;
            Object reviewer=signoff.getReviewer().getReviewer();
            if(reviewer instanceof User){
                approve=user.equals(reviewer);
            }else if(reviewer instanceof Member){
                approve=members.contains(reviewer);
            }else if(reviewer instanceof Group){
                approve=groupSet.contains(reviewer);
            }else if(reviewer instanceof Role){
                approve=roleSet.contains(reviewer);
            }
            if(approve){
                signoff.setSignoffStatus("已同意");
                signoffs.add(signoff);
                signoffCnt--;
            }
        }
        if (signoffCnt == 0) {
            for(Action action:node.getActions()){
                Object obj=action.execute("post");
                if(obj!=null)
                    return JsonResult.error(obj.toString());
            }

            boolean complete = true;
            log.info("node:"+node);
            for (Node node1 : node.getNextNodes()
            ) {
                for (Node node2 : node1.getPreviousNodes()) {
                    if (node2.equals(node))
                        continue;
                    log.info(("node2:"+node));
                    if (!node2.getNodeStatus().equals("已完成"))
                        complete = false;
                }
            }
            if (complete) {
                if(node.getNextNodes().size()==0||node.getNextNodes().size()==1&& node.getNextNodes().get(0).getNodeName()==null){
                    log.info("end workflow:"+node.getWorkflow().getId());
                    node.getWorkflow().setWorkflowStatus("已完成");
                    workflowRepository.save(node.getWorkflow());
                    workflowLogRepository.save(new WorkflowLog(node.getWorkflow().getId(),null,user,"结束流程",now,now,""));
                    nodeService.notifyUser(node,1);
                }else{
                    List<Node> nodes=new ArrayList<>();
                    for (Node node1 : node.getNextNodes()) {
                        if(node1.getNodeStatus().equals("未开始")){
                            for(Action action:node1.getActions()){
                                Object obj=action.execute("pre");
                                if(obj!=null)
                                    return JsonResult.error(obj.toString());
                            }
                            node1.setNodeStatus("已开始");
                            node1.setStartTime(now);
                            nodes.add(node1);
                        }
                    }
                    nodes.forEach(node1 -> nodeService.notifyUser(node1,0));
                    nodeService.notifyUser(node,0);
                    nodeRepository.saveAll(node.getNextNodes());
                }
            }
            node.setNodeStatus("已完成");
            node.setEndTime(now);
            nodeRepository.save(node);
        }
        signoffRepository.saveAll(signoffs);
        return JsonResult.success();
    }

    @GetMapping("/list")
    @ApiOperation(("/获得用户所有流程"))
    public JsonResult getAllNodes(@SessionAttribute User user) {
        Set<Group> groupSet = new HashSet<>();
        Set<Role> roleSet = new HashSet<>();

        user.getMembers().forEach(member -> {
            groupSet.add(member.getGroup());
            roleSet.add(member.getRole());
        });

        List<NodeReviewer> nodeReviewers = new ArrayList<>();
        nodeReviewers.addAll(nodeRepository.findAllByGroup(groupSet));
        nodeReviewers.addAll(nodeRepository.findAllByRole(roleSet));
        nodeReviewers.addAll(nodeRepository.findAllByMember(user.getMembers()));
        nodeReviewers.addAll(nodeRepository.findAllByUser(user));
        Set<Workflow> finishedWorkflows = new HashSet<>();
        Set<Workflow> executedWorkflows = new HashSet<>();
        Set<Workflow> pendingWorkflows = new HashSet<>();
        Set<Node> pendingNodes = new HashSet<>();

        nodeReviewers.forEach(nodeReviewer -> {
            if (nodeReviewer.getNode().getWorkflow().getWorkflowStatus().equals("已完成")) {
                finishedWorkflows.add(nodeReviewer.getNode().getWorkflow());
            } else {
                if (nodeReviewer.getNode().getNodeStatus().equals("未开始"))
                    return;
                if (nodeReviewer.getSignoff().getSignoffStatus().equals("未做决定")) {
                    pendingNodes.add(nodeReviewer.getNode());
                    pendingWorkflows.add(nodeReviewer.getNode().getWorkflow());
                    executedWorkflows.remove(nodeReviewer.getNode().getWorkflow());
                } else {
                    if (pendingWorkflows.contains(nodeReviewer.getNode().getWorkflow()))
                        return;
                    executedWorkflows.add(nodeReviewer.getNode().getWorkflow());
                }
            }
        });
        Map<String, Set> map = new HashMap<>();
        map.put("finished", finishedWorkflows);
        map.put("executed", executedWorkflows);
        map.put("pending", pendingNodes);
        return JsonResult.success(map);
    }






}
