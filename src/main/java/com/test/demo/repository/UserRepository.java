package com.test.demo.repository;
import com.test.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsername(String username);
    @Transactional
    @Modifying
    @Query("update User set username = ?1 where id = ?2")
    int modifyById(String  username, Long id);

    @Query("select u.id from User u where username=?1")
    Long getIdByUsername(String username);

    @Query("select u from User u join u.members m join m.group g where g.groupName=?1")
    List<User> findByGroup(String groupName);

    @Query("select u from User u join u.members m join m.role r where r.roleName=?1")
    List<User> findByRole(String roleName);


    @Query ("select u.username from User u where id=?1")
    String getNameById(Long id);
    @Query("select u.username from User u join u.members m join m.group g where g.id=?1")
    List<String> getNameByGroupId(Long id);
    @Query("select u.username from User u join u.members m join m.role r where r.id=?1")
    List<String> getNameByRoleId(Long id);
    @Query("select u.username from User u join u.members m  where m.id=?1")
    List<String> getNameByMemberId(Long id);

    @Query ("select u.username from User u where id in(?1)")
    List<String> getNameByIds(Collection<Long> ids);
    @Query("select u.username from User u join u.members m join m.group g where g.id in(?1)")
    List<String> getNameByGroupIds(Collection<Long> ids);
    @Query("select u.username from User u join u.members m join m.role r where r.id in(?1)")
    List<String> getNameByRoleIds(Collection<Long> ids);
    @Query("select u.username from User u join u.members m  where m.id in(?1)")
    List<String> getNameByMemberIds(Collection<Long> ids);

}
