package com.test.demo.model;

import com.alibaba.fastjson.annotation.JSONType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@JSONType(ignores = {"nextNodes","previousNodes"})
@ToString(exclude = {"previousNodes","nextNodes"})
public class Node {
    @Id
    @GeneratedValue
    Long id;
    @Column
    String nodeStatus="未开始";
    @JoinColumn
    @ManyToOne
    Workflow workflow;
    @JoinColumn
    @ManyToOne
    NodeTemplate nodeTemplate;
    @Column
    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    List<Signoff> signoffs;
    @JsonIgnore
    @Column
    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    List<Node> nextNodes=new ArrayList<>();
    @JsonIgnore
    @Column
    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    List<Node> previousNodes=new ArrayList<>();
    @Column
    Timestamp startTime;
    @Column
    Timestamp endTime;

    public Node(Workflow workflow, NodeTemplate nodeTemplate, List<Signoff> signoffs) {
        this.workflow = workflow;
        this.nodeTemplate = nodeTemplate;
        this.signoffs = signoffs;
    }
    public Node(){

    }

    @Override
    public boolean equals(Object node){
        if(node instanceof Node){
            if(((Node) node).getId().equals(this.getId()))
                return true;
        }
        return false;
    }
    @Override
    public int hashCode(){
        return this.getId().hashCode();
    }
}
