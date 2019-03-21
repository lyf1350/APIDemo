package com.test.demo.model;


import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name="MyGroup")
@Data
@ApiModel(value="组")
public class Group {
    @GeneratedValue
    @Id
    Long Id;
    @Column
    String groupName;

    public Group(){

    }
    public Group(String group){
        this.groupName=group;
    }
}
