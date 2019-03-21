package com.test.demo.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@ApiModel(value="成员")
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
}
