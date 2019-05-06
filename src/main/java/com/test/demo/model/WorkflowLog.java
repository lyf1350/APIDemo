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
    @Column
    Long workflowId;
    @Column
    String nodeName;
    @Column
    Long nodeId;
    @Column
    String userName;
    @Column
    Long userId;
    @Column
    String decision;
    @Column
    Timestamp startTime;
    @Column
    Timestamp endTime;
    @Column
    String remark;

    public WorkflowLog(Workflow workflow, Node node, User person, String decision, Timestamp startTime, Timestamp endTime, String remark) {
        this.workflowId=workflow.getId();
        if(node!=null){
            this.nodeName=node.getNodeName();
            this.nodeId=node.getId();
        }
        if(person!=null){
            this.userName=person.getUsername();
            this.userId=person.getID();
        }

        this.decision = decision;
        this.startTime = startTime;
        this.endTime = endTime;
        this.remark = remark;
    }
    public WorkflowLog(){

    }
}
