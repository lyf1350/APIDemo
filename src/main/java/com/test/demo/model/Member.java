package com.test.demo.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Member {
    @Id
    @GeneratedValue
    Long id;
    @JoinColumn
    @ManyToOne
    Group group;
    @JoinColumn
    @ManyToOne
    Role role;
    public Member(){

    }
    public Member(Group group, Role role){
        this.group=group;
        this.role=role;
    }

    @Override
    public boolean equals(Object object){
        if(object!=null&&object instanceof Member){
            return ((Member) object).getId().equals(this.getId());
        }
        return false;
    }

    @Override
    public int hashCode(){
        return this.getId().hashCode();
    }
}
