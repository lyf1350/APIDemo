package com.test.demo.controller;

import com.alibaba.fastjson.JSON;
import com.test.demo.common.JsonResult;
import com.test.demo.model.*;
import com.test.demo.repository.*;
import com.test.demo.service.NodeService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.*;

@RestController
@Slf4j
@RequestMapping("/api/workflow")
public class WorkflowController  {

    @Autowired
    WorkflowRepository workflowRepository;
    @Autowired
    NodeRepository nodeRepository;
    @Autowired
    WorkflowLogRepository workflowLogRepository;
    @Autowired
    SignoffRepository signoffRepository;
    @Autowired
    NodeTemplateRepository nodeTemplateRepository;
    @Autowired
    NodeService nodeService;
    @Autowired
    ReviewerRepository reviewerRepository;
    @Autowired
    PropertyRepository propertyRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ActionRepository actionRepository;
    @PostMapping("create")
    @ApiOperation(value="创建流程")
    public JsonResult createWorkflow(WorkflowTemplate workflowTemplate,String nodeArray ,@SessionAttribute User user){
        List<NodeTemplate> nodeTemplates1= JSON.parseArray(nodeArray,NodeTemplate.class);
        log.info("nodeArray:"+nodeArray);
        log.info("nodeTemplate:"+nodeTemplates1);
        Map<String,List<Reviewer>> nodeReviewerMap=new HashMap<>();

        nodeTemplates1.forEach(e->nodeReviewerMap.put(e.getNodeKey(),e.getReviewers()));
        Workflow workflow=workflowRepository.save(new Workflow(user,workflowTemplate,propertyRepository.save(new Property())));
        List<NodeTemplate> nodeTemplates=nodeTemplateRepository.findAllByWorkflowTemplate(workflowTemplate);
        Map<NodeTemplate,Node> nodeMap=new HashMap<>();
        Timestamp timestamp=new Timestamp(new Date().getTime());
        Node startNode=nodeRepository.save(new Node(workflow,null,null,null));

        Node endNode=nodeRepository.save(new Node(workflow,null,null,null));
        List<Node> notList=new ArrayList<>();
        List<String> errorList=new ArrayList<>();
        nodeTemplates.forEach(nodeTemplate -> {
            if(errorList.size()>0)
                return;
            List<Signoff> signoffs=new ArrayList<>();
            nodeReviewerMap.get(nodeTemplate.getNodeKey()).forEach(reviewer ->{
                if(reviewer.getType().equals("member")){
                    reviewer.setMember(memberRepository.findByGroupAndRole(reviewer.getGroup(),reviewer.getRole()));
                }
                signoffs.add(new Signoff(reviewerRepository.save(reviewer)));
            } );
            List<Action> actions=new ArrayList<>();
            nodeTemplate.getActions().forEach(action ->actions.add(new Action(action.getType(),action.getAction(),action.getActionArguments())) );
            Node node=nodeRepository.save(new Node(workflow,nodeTemplate,signoffRepository.saveAll(signoffs),actionRepository.saveAll(actions)));
            if(nodeTemplate.getPreviousNodeTemplate()==null||nodeTemplate.getPreviousNodeTemplate().size()==0){
                for(Action action:node.getActions()){
                    Object obj=action.execute("pre");
                    if(obj!=null){
                        deleteWorkflow(workflow.getId(),user);
                        errorList.add(obj.toString());
                        return;
                    }
                }
                startNode.getNextNodes().add(node);
                node.getPreviousNodes().add(startNode);
                node.setNodeStatus("已开始");
                node.setStartTime(timestamp);
                node.setNodeLevel(0);
                notList.add(node);
            }
            if(nodeTemplate.getNextNodeTemplate()==null||nodeTemplate.getNextNodeTemplate().size()==0){
                node.getNextNodes().add(endNode);
                endNode.getPreviousNodes().add(node);
            }
            nodeMap.put(nodeTemplate,node);
        });
        if(errorList.size()>0)
            return JsonResult.error(errorList.get(0));
        notList.forEach(node -> nodeService.notifyUser(node,0));
        nodeRepository.save(startNode);
        nodeRepository.save(endNode);
        nodeTemplates.forEach(nodeTemplate -> {
            nodeTemplate.getPreviousNodeTemplate().forEach(nodeTemplate1 -> {
                nodeMap.get(nodeTemplate).getPreviousNodes().add(nodeMap.get(nodeTemplate1));
            });
            nodeTemplate.getNextNodeTemplate().forEach(nodeTemplate1 -> {
                nodeMap.get(nodeTemplate).getNextNodes().add(nodeMap.get(nodeTemplate1));
            });
        });
        nodeRepository.saveAll(nodeMap.values());
        workflowLogRepository.save(new WorkflowLog(workflow.getId(),null,user,"发起流程",timestamp,timestamp,""));
        return JsonResult.success();
    }

    @GetMapping("/log")
    public JsonResult getLog(Long id){
        return JsonResult.success(workflowLogRepository.findAllByWorkflowId(id));
    }
    @PostMapping("/delete")
    public JsonResult deleteWorkflow(Long id,@SessionAttribute User user){
        log.info("workflow:"+id);
        Timestamp timestamp=new Timestamp(new Date().getTime());
        workflowLogRepository.save(new WorkflowLog(id,null,user,"删除流程",timestamp,timestamp,""));
        List<Node> nodes=nodeRepository.findAllByWorkflow(id);
        nodeRepository.deleteAll(nodes);
        workflowRepository.deleteById(id);
        return  JsonResult.success();
    }

    @PostMapping("/save")
    public JsonResult saveWorkflow(Long id,String property){
        Property property1=workflowRepository.findPropertyById(id);
        property1.setProperty(property);
        propertyRepository.save(property1);
        return JsonResult.success();
    }
}
