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

    public File(String fileName, String uuid) {
        this.fileName = fileName;
        this.uuid = uuid;
    }

    public File(){

    }
}
