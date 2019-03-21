package com.test.demo.repository;
import com.test.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsername(String username);
    @Transactional
    @Modifying
    @Query("update User set username = ?1 where id = ?2")
    int modifyById(String  username, Long id);

    @Query("select u.ID from User u where username=?1")
    Long getIdByUsername(String username);

    @Query("select u from User u join u.members m join m.group g where g.groupName=?1")
    List<User> findByGroup(String groupName);

    @Query("select u from User u join u.members m join m.role r where r.roleName=?1")
    List<User> findByRole(String roleName);
}
