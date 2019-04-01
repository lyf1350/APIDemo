package com.test.demo.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
public class WorkflowLog {

    @Id
    @GeneratedValue
    Long id;
    @ManyToOne
    @JoinColumn
    Workflow workflow;
    @ManyToOne
    @JoinColumn
    Node node;
    @ManyToOne
    @JoinColumn
    User person;
    @Column
    String decision;
    @Column
    Timestamp startTime;
    @Column
    Timestamp endTime;
    @Column
    String remark;

    public WorkflowLog(Workflow workflow, Node node, User person, String decision, Timestamp startTime, Timestamp endTime, String remark) {
        this.workflow = workflow;
        this.node = node;
        this.person = person;
        this.decision = decision;
        this.startTime = startTime;
        this.endTime = endTime;
        this.remark = remark;
    }
    public WorkflowLog(){

    }
}
