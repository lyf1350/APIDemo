package com.test.demo.repository;

import com.test.demo.model.Message;
import com.test.demo.model.MessageState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MessageStateRepository extends JpaRepository<MessageState,Long> {
    @Modifying
    @Transactional
    @Query("update MessageState set state=1 where id=?1")
    void updateById(Long id);
    @Modifying
    @Transactional
     void deleteMessageStateByMessageAndUserId(Message message, Long userId);

    List<MessageState> findByUserId(Long userId);

}
