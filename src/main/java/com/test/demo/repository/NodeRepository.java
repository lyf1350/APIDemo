package com.test.demo.repository;

import com.test.demo.model.Node;
import org.springframework.data.jpa.repository.JpaRepository;


public interface NodeRepository extends JpaRepository<Node,Long> {

}
