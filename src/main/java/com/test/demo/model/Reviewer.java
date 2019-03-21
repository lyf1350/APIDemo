package com.test.demo.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Reviewer {
    @Id
    @GeneratedValue
    Long id;
    @Column
    Long reviewerId;
    @Column
    String type;
    @Column
    Integer rate=0;
}
