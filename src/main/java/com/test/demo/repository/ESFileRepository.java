package com.test.demo.repository;


import com.test.demo.model.ESFile;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ESFileRepository extends ElasticsearchRepository<ESFile,String> {

}
