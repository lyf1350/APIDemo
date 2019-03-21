package com.test.demo.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Action {
    @Id
    @GeneratedValue
    Long id;
    @JoinColumn
    @ManyToOne
    ActionTemplate actionTemplate;
    @JoinColumn
    @OneToMany
    List<Argument> arguments;
}
