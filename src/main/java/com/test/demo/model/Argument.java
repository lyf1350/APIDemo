package com.test.demo.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class Argument {
    @Id
    @GeneratedValue
    Long id;
    @Column
    String argumentKey;
    @Column
    String arugmentValue;
}
