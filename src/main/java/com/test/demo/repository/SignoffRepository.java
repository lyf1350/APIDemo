package com.test.demo.repository;

import com.test.demo.model.Signoff;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SignoffRepository extends JpaRepository<Signoff,Long> {
}
