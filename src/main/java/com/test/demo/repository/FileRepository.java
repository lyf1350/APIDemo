package com.test.demo.repository;

import com.test.demo.model.File;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FileRepository extends JpaRepository<File,Long> {

    File findByUuid(String uuid);
}
