package com.test.demo.controller;

import com.test.demo.common.JsonResult;
import com.test.demo.model.WorkflowTemplate;
import com.test.demo.repository.WorkflowTemplateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/workflowTemplate")
public class WorkflowTemplateController {
    @Autowired
    WorkflowTemplateRepository workflowTemplateRepository;
    @GetMapping("/list")
    public JsonResult getAllWorkflowTemplate(){
        return JsonResult.success(workflowTemplateRepository.findAll());
    }

    @PostMapping("/save")
    public JsonResult saveWorkflowTemplate(WorkflowTemplate workflowTemplate){
        return JsonResult.success(workflowTemplateRepository.save(workflowTemplate));
    }
}
