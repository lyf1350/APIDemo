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
}
