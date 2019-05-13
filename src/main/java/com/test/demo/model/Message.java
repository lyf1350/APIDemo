package com.test.demo.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Message {
    @Id
    @GeneratedValue
    Long Id;
    @Column
    String source;
    @Column
    String dest;
    @Column
    String type;
    @Column
    String title;
    @Column
    String content;
    @Column
    @CreatedDate
    Timestamp createDate;
    public Message(){
    }
    public Message(Long id){
        this.Id=id;
    }

    public Message(String source, String dest, String type, String title, String content) {
        this.source = source;
        this.dest = dest;
        this.type = type;
        this.title = title;
        this.content = content;
    }
}
