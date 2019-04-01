package com.test.demo.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Signoff {

    @Id
    @GeneratedValue
    Long id;
    @JoinColumn
    @ManyToOne
    Reviewer reviewer;
    @Column
    String signoffStatus="未做决定";

    public Signoff(Reviewer reviewer) {
        this.reviewer = reviewer;
    }
    public Signoff(){

    }
}
