package com.test.demo.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "esfile")
@Data
public class ESFile {
    @Id
    String id;
    String fileName;
    String content;

    public ESFile(){

    }
    public ESFile(String id,String fileName,String content){
        this.id=id;
        this.fileName=fileName;
        this.content=content;
    }
}
