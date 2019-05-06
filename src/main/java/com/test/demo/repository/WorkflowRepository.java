package com.test.demo.repository;

import com.test.demo.model.Property;
import com.test.demo.model.Workflow;
import com.test.demo.model.WorkflowTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface WorkflowRepository extends JpaRepository<Workflow,Long> {


    @Query("select w.property from Workflow w where w.id=?1")
    Property findPropertyById(Long id);
}
