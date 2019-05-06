package com.test.demo.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
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
    @Column
    String workflowName;
    @Column
    String workflowModel;
    @Column
    String workflowLayout;
    @JoinColumn
    @OneToOne
    Property property;

    public Workflow(User workflowUser, WorkflowTemplate workflowTemplate,Property property) {
        this.workflowUser = workflowUser;
        this.workflowName=workflowTemplate.getTemplateName();
        this.workflowModel=workflowTemplate.getTemplateModel();
        this.workflowLayout=workflowTemplate.getTemplateLayout();
        this.property=property;
    }
    public Workflow(){

    }
}
