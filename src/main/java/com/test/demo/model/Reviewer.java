package com.test.demo.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Collection;

@Data
@Entity
public class Reviewer {
    @Id
    @GeneratedValue
    Long id;
    @Column
    String type;
    @JoinColumn
    @ManyToOne
    Group group;
    @JoinColumn
    @ManyToOne
    User user;
    @JoinColumn
    @ManyToOne
    Role role;
    @JoinColumn
    @ManyToOne
    Member member;
    @Column
    Integer rate=0;
    public Reviewer(){
    }
    public Reviewer(Group reviewerGroup) {
        this.group = reviewerGroup;
    }

    public Reviewer(User reviewerUser) {
        this.user = reviewerUser;
    }

    public Reviewer(Role reviewerRole) {
        this.role = reviewerRole;
    }

    public Object getReviewer(){
        return user!=null?user:member!=null?member:group!=null?group:role!=null?role:null;
    }

    public Long getReviewerId(){
        return user!=null?user.getId():member!=null?member.getId():group!=null?group.getId():role!=null?role.getId():null;
    }






}
