package com.test.demo.model;

import lombok.Data;
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

    public File(String fileName, String uuid,String suffix) {
        this.fileName = fileName;
        this.uuid = uuid;
        this.suffix=suffix;
    }

    public File(){

    }
}
