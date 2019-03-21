package com.test.demo.repository;

import com.test.demo.model.Action;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActionRepository extends JpaRepository<Action,Long> {
}
