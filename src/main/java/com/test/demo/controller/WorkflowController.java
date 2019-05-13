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
        Node startNode=nodeRepository.save(new Node(workflow,null,null));

        Node endNode=nodeRepository.save(new Node(workflow,null,null));
        nodeTemplates.forEach(nodeTemplate -> {
            List<Signoff> signoffs=new ArrayList<>();
            nodeReviewerMap.get(nodeTemplate.getNodeKey()).forEach(reviewer ->{
                if(reviewer.getType().equals("member")){
                    reviewer.setMember(memberRepository.findByGroupAndRole(reviewer.getGroup(),reviewer.getRole()));
                }
                signoffs.add(new Signoff(reviewerRepository.save(reviewer)));
            } );
            Node node=nodeRepository.save(new Node(workflow,nodeTemplate,signoffRepository.saveAll(signoffs)));
            if(nodeTemplate.getPreviousNodeTemplate()==null||nodeTemplate.getPreviousNodeTemplate().size()==0){
                startNode.getNextNodes().add(node);
                node.getPreviousNodes().add(startNode);
                node.setNodeStatus("已开始");
                node.setStartTime(timestamp);
                node.setNodeLevel(0);
                nodeService.notifyUser(node,0);
            }
            if(nodeTemplate.getNextNodeTemplate()==null||nodeTemplate.getNextNodeTemplate().size()==0){
                node.getNextNodes().add(endNode);
                endNode.getPreviousNodes().add(node);
            }
            nodeMap.put(nodeTemplate,node);
        });
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
        workflowLogRepository.save(new WorkflowLog(workflow,null,user,"发起流程",timestamp,timestamp,""));
        return JsonResult.success();
    }

    @GetMapping("/log")
    public JsonResult getLog(Long id){
        log.info("workflow:"+id);
        return JsonResult.success(workflowLogRepository.findAllByWorkflowId(id));
    }
    @PostMapping("/delete")
    public JsonResult deleteWorkflow(Long id){
        log.info("workflow:"+id);

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
