package com.test.demo.repository;

import com.test.demo.model.*;
import com.test.demo.repository.result.NodeReviewer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;


public interface NodeRepository extends JpaRepository<Node,Long> {


    @Query("select n as node,s as signoff from Node n join n.signoffs s join s.reviewer r where  r.group in (?1) order by n.startTime")
    List<NodeReviewer> findAllByGroup(Collection<Group> groups);

    @Query("select n as node,s as signoff from Node n join n.signoffs s join s.reviewer r where  r.member in (?1) order by n.startTime")
    List<NodeReviewer> findAllByMember(Collection<Member> members);

    @Query("select n as node,s as signoff from Node n join n.signoffs s join s.reviewer r where  r.role in (?1) order by n.startTime")
    List<NodeReviewer> findAllByRole(Collection<Role> roles);

    @Query("select n as node,s as signoff from Node n join n.signoffs s join s.reviewer r where  r.user =?1 order by n.startTime")
    List<NodeReviewer> findAllByUser(User user);
    @Query("select n from Node n join n.workflow w where w.id=?1")
    List<Node> findAllByWorkflow(Long id);

}
