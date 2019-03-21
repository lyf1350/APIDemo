package com.test.demo.repository;

import com.test.demo.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface GroupRepository extends JpaRepository<Group,Long> {
    Group findGroupByGroupName(String groupName);
    @Modifying
    @Transactional
    void deleteByGroupName(String groupName);
}
