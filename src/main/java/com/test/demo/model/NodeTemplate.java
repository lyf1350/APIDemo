package com.test.demo.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class NodeTemplate {
    @Id
    @GeneratedValue
    Long id;
    @Column
    String templateName;
    @Column
    String nodeKey;
    @Column
    @OneToMany
    List<Action> actions;
    @Column
    @OneToMany
    List<Reviewer> reviewers;
    @ManyToOne
    @JoinColumn
    WorkflowTemplate workflowTemplate;
    @ManyToMany
    @Column
    List<NodeTemplate> previousNodeTemplate;
    @ManyToMany
    @Column
    List<NodeTemplate> nextNodeTemplate;
    @Override
    public boolean equals(Object nodeTemplate){
        if(nodeTemplate instanceof NodeTemplate){
            if(((NodeTemplate) nodeTemplate).getId().equals(this.getId()))
                return true;
        }
        return true;
    }
}
