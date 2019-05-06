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

    @Override
    public boolean equals(Object object){
        if(object!=null&&object instanceof Role){
            return ((Role) object).getId().equals(this.getId());
        }
        return false;
    }

    @Override
    public int hashCode(){
        return this.getId().hashCode();
    }
}
