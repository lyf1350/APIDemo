package com.test.demo.controller;

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
    @PostMapping("create")
    @ApiOperation(value="创建流程")
    public JsonResult createWorkflow(WorkflowTemplate workflowTemplate, @SessionAttribute User user){
        if(user==null)
            return JsonResult.error();
        Workflow workflow=workflowRepository.save(new Workflow(user,workflowTemplate));
        List<NodeTemplate> nodeTemplates=nodeTemplateRepository.findAllByWorkflowTemplate(workflowTemplate);
        Map<NodeTemplate,Node> nodeMap=new HashMap<>();
        Timestamp timestamp=new Timestamp(new Date().getTime());
        Node endNode=nodeRepository.save(new Node(workflow,null,null));
        Node startNode=nodeRepository.save(new Node(workflow,null,null));
        nodeTemplates.forEach(nodeTemplate -> {
            List<Signoff> signoffs=new ArrayList<>();
            nodeTemplate.getReviewers().forEach(reviewer -> {
                signoffs.add(new Signoff(reviewer));
            });
            Node node=nodeRepository.save(new Node(workflow,nodeTemplate,signoffRepository.saveAll(signoffs)));

            if(nodeTemplate.getPreviousNodeTemplate()==null||nodeTemplate.getPreviousNodeTemplate().size()==0){
                startNode.getNextNodes().add(node);
                node.getPreviousNodes().add(startNode);
                node.setNodeStatus("已开始");
                node.setStartTime(timestamp);
                nodeService.notifyUser(node);
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

    @PostMapping("/log")
    public JsonResult getLog(Workflow workflow){
        log.info("workflow:"+workflow);
        return JsonResult.success(workflowLogRepository.findAllByWorkflow(workflow));
    }
}
