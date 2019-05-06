package com.test.demo.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@ApiModel(value="用户",description = "user")
public class User {
    @Id
    @GeneratedValue
    @ApiModelProperty(value="主键")
    Long ID;
    @Column
    String username;
    @Column
    @JsonIgnore
    String password;
    @Column
    String email;
    @Column
    String phone;
    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    List<Member> members;
    @Column
    Integer state;
    public User(){
    }
    public User(String username, String password, String email, String phone) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.state=1;
    }

    public User(Long ID,String username, String password, String email, String phone, List<Member> members) {
        this.ID=ID;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.members = members;
    }
    @Override
    public boolean equals(Object object){
        if(object!=null&&object instanceof User){
            return ((User) object).getID().equals(this.getID());
        }
        return false;
    }

    @Override
    public int hashCode(){
        return this.getID().hashCode();
    }
}