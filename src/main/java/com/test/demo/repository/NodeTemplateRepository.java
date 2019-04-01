package com.test.demo.repository;

import com.test.demo.model.NodeTemplate;
import com.test.demo.model.WorkflowTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NodeTemplateRepository extends JpaRepository<NodeTemplate,Long> {

    List<NodeTemplate> findAllByWorkflowTemplate(WorkflowTemplate workflowTemplate);
    NodeTemplate findNodeTemplateByNodeKeyAndWorkflowTemplate(String nodeKey, WorkflowTemplate workflowTemplate);
}
