package com.test.demo.repository;

import com.test.demo.model.Argument;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArgumentRepository extends JpaRepository<Argument,Long> {
}
