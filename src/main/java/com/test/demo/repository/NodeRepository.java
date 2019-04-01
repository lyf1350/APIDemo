package com.test.demo.repository;

import com.test.demo.model.Node;
import com.test.demo.repository.result.NodeReviewer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface NodeRepository extends JpaRepository<Node,Long> {

    @Query("select n as node,s as signoff from Node n join n.signoffs s join s.reviewer r where  r.reviewerId in (?1) order by n.startTime desc ")
    List<NodeReviewer> findByUser(List<Long> reviewerId);
}
