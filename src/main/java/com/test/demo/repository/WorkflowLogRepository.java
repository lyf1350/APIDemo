package com.test.demo.repository;

import com.test.demo.model.Node;
import com.test.demo.model.Workflow;
import com.test.demo.model.WorkflowLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

public interface WorkflowLogRepository extends JpaRepository<WorkflowLog,Long> {

    @Query("select  w.startTime from WorkflowLog w where w.workflow=?1")
    Timestamp findStartTimeByWorkflow(Workflow workflow);
    @Query("select  w.startTime from WorkflowLog w where w.node=?1")
    Timestamp findStartTimeByNode(Node node);

    List<WorkflowLog> findAllByWorkflow(Workflow workflow);
}
