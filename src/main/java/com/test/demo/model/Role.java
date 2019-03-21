package com.test.demo.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@ApiModel(value="角色")
public class Role {
    @Id
    @GeneratedValue
    Long id;

    @Column
    String roleName;

    public Role(){

    }
    public Role(String role){
        this.roleName=role;
    }
}
