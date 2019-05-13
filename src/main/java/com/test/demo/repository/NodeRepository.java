package com.test.demo.repository;

import com.test.demo.model.*;
import com.test.demo.repository.result.NodeReviewer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;


public interface NodeRepository extends JpaRepository<Node,Long> {


    @Query("select n as node,s as signoff from Node n join n.signoffs s join s.reviewer r where  r.type='group' and r.group in (?1) order by n.startTime")
    List<NodeReviewer> findAllByGroup(Collection<Group> groups);

    @Query("select n as node,s as signoff from Node n join n.signoffs s join s.reviewer r where  r.type='member' and r.member in (?1) order by n.startTime")
    List<NodeReviewer> findAllByMember(Collection<Member> members);

    @Query("select n as node,s as signoff from Node n join n.signoffs s join s.reviewer r where  r.type='role' and r.role in (?1) order by n.startTime")
    List<NodeReviewer> findAllByRole(Collection<Role> roles);

    @Query("select n as node,s as signoff from Node n join n.signoffs s join s.reviewer r where  r.type='user' and r.user =?1 order by n.startTime")
    List<NodeReviewer> findAllByUser(User user);
    @Query("select n from Node n join n.workflow w where w.id=?1")
    List<Node> findAllByWorkflow(Long id);

}
