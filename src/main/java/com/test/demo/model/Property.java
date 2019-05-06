package com.test.demo.model;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class Property {
    @Id
    @GeneratedValue
    Long id;
    @Column
    String property;
}
