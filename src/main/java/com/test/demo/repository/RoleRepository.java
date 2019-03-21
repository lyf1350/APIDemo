package com.test.demo.repository;

import com.test.demo.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findRoleByRoleName(String roleName);

    @Modifying
    @Transactional
    void deleteByRoleName(String roleName);

}
