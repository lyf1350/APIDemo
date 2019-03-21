package com.test.demo.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Node {
    @Id
    @GeneratedValue
    Long id;
    @Column
    String nodeStatus;
    @JoinColumn
    @ManyToOne
    Workflow workflow;
    @JoinColumn
    @ManyToOne
    NodeTemplate nodeTemplate;
}
