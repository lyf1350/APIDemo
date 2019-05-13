package com.test.demo.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class Config {
    @Id
    @GeneratedValue
    Long id;
    @Column
    String configName;
    @Column
    String type;
    @Column
    String val;
    @Column
    String scope="site";
    @Column
    String context;
}
