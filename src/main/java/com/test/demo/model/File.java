package com.test.demo.model;

import com.alibaba.fastjson.annotation.JSONType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class File {
    @Id
    @GeneratedValue
    Long id;
    @Column
    String fileName;
    @Column
    String uuid;
    @Column
    @CreatedDate
    Timestamp createTime;
    @Column
    String suffix;
    @JoinColumn
    @ManyToOne
    User uploader;
    @Column
    String type="default";

    public File(String fileName, String uuid,String suffix,User uploader) {
        this.fileName = fileName;
        this.uuid = uuid;
        this.suffix=suffix;
        this.uploader=uploader;
    }

    public File(){

    }
}
