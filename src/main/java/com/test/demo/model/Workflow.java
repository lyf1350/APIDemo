package com.test.demo.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Time;
import java.sql.Timestamp;
@EntityListeners(AuditingEntityListener.class)
@Entity
@Data
public class Workflow {
    @Id
    @GeneratedValue
    Long id;
    @ManyToOne
    @JoinColumn
    User workflowUser;
    @Column
    @CreatedDate
    Timestamp createTime;
    @Column
    Timestamp endTime;
    @Column
    String workflowStatus="已开始";
    @ManyToOne
    @JoinColumn
    WorkflowTemplate workflowTemplate;

    public Workflow(User workflowUser, WorkflowTemplate workflowTemplate) {
        this.workflowUser = workflowUser;
        this.workflowTemplate = workflowTemplate;
    }
    public Workflow(){

    }
}
