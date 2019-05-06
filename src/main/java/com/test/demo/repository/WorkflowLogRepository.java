package com.test.demo.repository;

import com.test.demo.model.Node;
import com.test.demo.model.Workflow;
import com.test.demo.model.WorkflowLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

public interface WorkflowLogRepository extends JpaRepository<WorkflowLog,Long> {


    List<WorkflowLog> findAllByWorkflowId(Long workflowId);
}
