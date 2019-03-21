package com.test.demo.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@ApiModel(value="用户")
public class User {
    @Id
    @GeneratedValue
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
    List<Member> members;

    public User(){
    }
    public User(String username, String password, String email, String phone) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
    }

    public User(Long ID,String username, String password, String email, String phone, List<Member> members) {
        this.ID=ID;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.members = members;
    }
}