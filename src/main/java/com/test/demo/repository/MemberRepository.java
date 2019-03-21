package com.test.demo.repository;

import com.test.demo.model.Group;
import com.test.demo.model.Member;
import com.test.demo.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Long> {

    Member findByGroupAndRole(Group group, Role role);
}
