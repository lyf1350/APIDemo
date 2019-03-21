package com.test.demo.repository;

import com.test.demo.model.WorkflowLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkflowLogRepository extends JpaRepository<WorkflowLog,Long> {
}
