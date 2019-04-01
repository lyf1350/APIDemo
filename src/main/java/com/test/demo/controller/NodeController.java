package com.test.demo.controller;

import com.test.demo.common.JsonResult;
import com.test.demo.model.*;
import com.test.demo.repository.NodeRepository;
import com.test.demo.repository.SignoffRepository;
import com.test.demo.repository.WorkflowLogRepository;
import com.test.demo.repository.WorkflowRepository;
import com.test.demo.repository.result.NodeReviewer;
import com.test.demo.service.NodeService;
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

    private void setNodes(List<Node> nodes,Set<Node> nodeSet){
        nodes.forEach(node -> {
            if(!node.getNodeStatus().equals("未开始")){
                node.setNodeStatus("未开始");
                node.setStartTime(null);
                node.setEndTime(null);
                node.getSignoffs().forEach(signoff -> signoff.setSignoffStatus("未做决定"));
                signoffRepository.saveAll(node.getSignoffs());
                nodeSet.add(node);
                setNodes(node.getNextNodes(),nodeSet);
            }
        });
    }

    private Node getStartNode(Node node){
        return node.getPreviousNodes().size()>0?getStartNode(node.getPreviousNodes().get(0)):node;
    }
    @PostMapping("/execute")
    public JsonResult executeNode(Node node, String decision, String remark, @SessionAttribute User user) {
        if (user == null)
            return JsonResult.error();
        node=nodeRepository.findById(node.getId()).get();
        log.info("node:"+node+"\ndecision:"+decision+" remark:"+remark);
        Timestamp now = new Timestamp(new Date().getTime());
        WorkflowLog workflowLog = new WorkflowLog(node.getWorkflow(), node, user, decision, node.getStartTime(), now, remark);
        workflowLogRepository.save(workflowLog);
        if (decision.equals("拒绝")) {
            Node startNode=getStartNode(node);
            Set<Node> nodeSet=new HashSet<>();
            startNode.getNextNodes().forEach(node1 -> {
                if(!node1.getNodeStatus().equals("已开始")){
                    node1.setNodeStatus("已开始");
                    node1.setStartTime(now);
                    node1.setEndTime(null);
                    node1.getSignoffs().forEach(signoff -> signoff.setSignoffStatus("未做决定"));
                    signoffRepository.saveAll(node1.getSignoffs());
                    nodeSet.add(node1);
                    setNodes(node1.getNextNodes(),nodeSet);
                }
            });
            log.info("nodeset:"+nodeSet);
            nodeRepository.saveAll(nodeSet);
            return JsonResult.success();
        }
        Set<Long> groupSet = new HashSet<>();
        Set<Long> roleSet = new HashSet<>();
        Set<Long> memberSet = new HashSet<>();

        user.getMembers().forEach(member -> {
            groupSet.add(member.getGroup().getId());
            roleSet.add(member.getRole().getId());
            memberSet.add(member.getId());
        });
        int signoffCnt = 0;
        for (Signoff signoff : node.getSignoffs()) {
            if (!signoff.getSignoffStatus().equals("未做决定"))
                continue;
            signoffCnt++;
            boolean approve=false;
            switch (signoff.getReviewer().getType()) {
                case "user":
                    approve=user.getID().equals(signoff.getReviewer().getReviewerId());
                        break;
                case "member":
                    approve=memberSet.contains(signoff.getReviewer().getReviewerId());
                        break;
                case "group":
                    approve=groupSet.contains(signoff.getReviewer().getReviewerId());
                        break;
                case "role":
                    approve=roleSet.contains(signoff.getReviewer().getReviewerId());
                        break;
                default:
                    break;
            }
            if(approve){
                log.info("ty");
                signoff.setSignoffStatus("已同意");
                signoffRepository.save(signoff);
                signoffCnt--;
            }
        }
        if (signoffCnt == 0) {
            node.setNodeStatus("已完成");
            node.setEndTime(now);
            nodeRepository.save(node);
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
                if(node.getNextNodes().size()==0||node.getNextNodes().size()==1&& node.getNextNodes().get(0).getNodeTemplate()==null){
                    node.getWorkflow().setWorkflowStatus("已完成");
                    workflowRepository.save(node.getWorkflow());
                    workflowLogRepository.save(new WorkflowLog(node.getWorkflow(),null,null,"结束流程",now,now,""));
                }else{
                    for (Node node1 : node.getNextNodes()) {
                        node1.setNodeStatus("已开始");
                        node1.setStartTime(now);
                        nodeService.notifyUser(node);
                    }
                    nodeRepository.saveAll(node.getNextNodes());
                }


            }
        }

        return JsonResult.success();
    }

    @GetMapping("/list")
    public JsonResult getAllNodes(@SessionAttribute User user) {
        Set<Long> groupSet = new HashSet<>();
        Set<Long> roleSet = new HashSet<>();
        Set<Long> memberSet = new HashSet<>();

        user.getMembers().forEach(member -> {
            groupSet.add(member.getGroup().getId());
            roleSet.add(member.getRole().getId());
            memberSet.add(member.getId());
        });
        List<Long> reviewerId = new ArrayList<>();
        reviewerId.add(user.getID());
        reviewerId.addAll(groupSet);
        reviewerId.addAll(roleSet);
        reviewerId.addAll(memberSet);
        List<NodeReviewer> nodeReviewers = nodeRepository.findByUser(reviewerId);
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
