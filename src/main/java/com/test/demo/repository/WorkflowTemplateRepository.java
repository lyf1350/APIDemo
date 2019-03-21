package com.test.demo.repository;

import com.test.demo.model.WorkflowTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkflowTemplateRepository extends JpaRepository<WorkflowTemplate,Long> {
}
