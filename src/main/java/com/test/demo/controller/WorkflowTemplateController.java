package com.test.demo.controller;

import com.alibaba.fastjson.JSON;
import com.test.demo.common.JsonResult;
import com.test.demo.model.*;
import com.test.demo.repository.*;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@Slf4j
@RequestMapping("/api/workflowTemplate")
public class WorkflowTemplateController {
    @Autowired
    WorkflowTemplateRepository workflowTemplateRepository;
    @Autowired
    NodeTemplateRepository nodeTemplateRepository;
    @Autowired
    ReviewerRepository reviewerRepository;
    @Autowired
    NodeRepository nodeRepository;
    @Autowired
    WorkflowRepository workflowRepository;
    @Autowired
    ActionRepository actionRepository;
    @GetMapping("/list")
    public JsonResult getAllWorkflowTemplate(){
        return JsonResult.success(workflowTemplateRepository.findAll());
    }

    @PostMapping("/save")
    public JsonResult saveWorkflowTemplate(WorkflowTemplate workflowTemplate){
        return JsonResult.success(workflowTemplateRepository.save(workflowTemplate));
    }

    @PostMapping("/createOrUpdate")
    @ApiOperation("创建或更新流程模版")
    public JsonResult createOrUpdateWorkflowTemplate(String nodeArray, String fromMap, String toMap, WorkflowTemplate workflowTemplate){
        workflowTemplate=workflowTemplateRepository.save(workflowTemplate);
        List<NodeTemplate> nodeTemplates= JSON.parseArray(nodeArray,NodeTemplate.class);
        Map<String,NodeTemplate> nodeMap=new HashMap<>();
        for(NodeTemplate nodeTemplate : nodeTemplates){
            NodeTemplate temp=nodeTemplateRepository.findNodeTemplateByNodeKeyAndWorkflowTemplate(nodeTemplate.getNodeKey(),workflowTemplate);
            if(temp==null){
                nodeTemplate.setWorkflowTemplate(workflowTemplate);
                nodeTemplate.setReviewers(reviewerRepository.saveAll(nodeTemplate.getReviewers()));
                nodeTemplate.setActions(actionRepository.saveAll(nodeTemplate.getActions()));
                temp=nodeTemplate;
            }else{
                temp.setTemplateName(nodeTemplate.getTemplateName());
                List<Reviewer> reviewers=new ArrayList();
                for(Reviewer reviewer :nodeTemplate.getReviewers()){
                    boolean find=false;
                    for(Reviewer reviewer1:temp.getReviewers()){
                        if(reviewer.getReviewer().equals(reviewer1.getReviewer())){
                            find=true;
                            reviewers.add(reviewer1);
                            break;
                        }
                    }
                    if(!find){
                        reviewers.add(reviewerRepository.save(reviewer));
                    }
                }
                temp.setReviewers(reviewers);
                List<Action> actions=new ArrayList<>();
                for(Action action:nodeTemplate.getActions()){
                    boolean find=false;
                    for(Action action1:temp.getActions()){
                        if(action.equals(action1)){
                            find=true;
                            actions.add(action1);
                            break;
                        }
                    }
                    if(!find){
                        actions.add(actionRepository.save(action));
                    }
                }
                temp.setActions(actions);
            }
            temp.setPreviousNodeTemplate(new ArrayList<>());
            temp.setNextNodeTemplate(new ArrayList<>());
            nodeMap.put(nodeTemplate.getNodeKey(),nodeTemplateRepository.save(temp));
        }
        Map<String,List<String>> from=JSON.parseObject(fromMap,Map.class);
        Map<String,List<String>> to=JSON.parseObject(toMap,Map.class);
        from.forEach((key,value)->value.forEach(node->nodeMap.get(key).getPreviousNodeTemplate().add(nodeMap.get(node))));
        to.forEach((key,value)->value.forEach(node->nodeMap.get(key).getNextNodeTemplate().add(nodeMap.get(node))));
        nodeTemplateRepository.saveAll(nodeMap.values());
        return JsonResult.success();
    }

    @GetMapping("/delete")
    public JsonResult deleteWorkflowTemplate(Long id){
        log.info("workflowTemplate id:"+id);
        WorkflowTemplate workflowTemplate=workflowTemplateRepository.findById(id).get();
        List<NodeTemplate> nodeTemplates=nodeTemplateRepository.findAllByWorkflowTemplate(workflowTemplate);
        nodeTemplateRepository.deleteAll(nodeTemplates);
        workflowTemplateRepository.deleteById(id);
        return JsonResult.success();
    }
}
