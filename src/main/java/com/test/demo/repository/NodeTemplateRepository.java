package com.test.demo.repository;

import com.test.demo.model.NodeTemplate;
import com.test.demo.model.WorkflowTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NodeTemplateRepository extends JpaRepository<NodeTemplate,Long> {


    NodeTemplate findNodeTemplateByNodeKeyAndWorkflowTemplate(String nodeKey, WorkflowTemplate workflowTemplate);
}
