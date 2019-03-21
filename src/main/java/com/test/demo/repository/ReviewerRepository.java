package com.test.demo.repository;

import com.test.demo.model.Reviewer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewerRepository extends JpaRepository<Reviewer,Long> {
}
