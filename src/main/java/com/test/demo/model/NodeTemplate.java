package com.test.demo.model;

import com.alibaba.fastjson.annotation.JSONType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@ToString(exclude = {"previousNodeTemplate","nextNodeTemplate"})
@JSONType(ignores = {"previousNodeTemplate","nextNodeTemplate"})
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
    @LazyCollection(LazyCollectionOption.FALSE)

    List<Action> actions;
    @Column
    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)

    List<Reviewer> reviewers;
    @ManyToOne
    @JoinColumn
    WorkflowTemplate workflowTemplate;
    @JsonIgnore
    @ManyToMany
    @Column
    @LazyCollection(LazyCollectionOption.FALSE)
    List<NodeTemplate> previousNodeTemplate;
    @JsonIgnore
    @ManyToMany
    @Column
    @LazyCollection(LazyCollectionOption.FALSE)
    List<NodeTemplate> nextNodeTemplate;
    @Override
    public boolean equals(Object nodeTemplate){
        if(nodeTemplate instanceof NodeTemplate){
            if(((NodeTemplate) nodeTemplate).getId().equals(this.getId()))
                return true;
        }
        return true;
    }
    @Override
    public int hashCode(){
        return this.getId().hashCode();
    }
}
