package com.test.demo.repository;

import com.test.demo.model.Workflow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkflowRepository extends JpaRepository<Workflow,Long> {
}
