package com.test.demo;

import com.test.demo.model.Message;
import com.test.demo.model.MessageState;
import com.test.demo.repository.MessageRepository;
import com.test.demo.repository.MessageStateRepository;
import com.test.demo.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiDemoApplicationTests {
//    @Autowired
//     MessageRepository messageRepository;
//    @Autowired
//     MessageStateRepository messageStateRepository;
//    @Autowired
//     UserRepository userRepository;
    @Test
    public void contextLoads() {
//       MessageState state= messageStateRepository.save(new MessageState(2L,0,new Message(1L)));
//       System.out.println("state:"+state);
//        messageStateRepository.updateByUser(2L,new Message(1L));
//        messageStateRepository.deleteMessageStateByMessageAndUserId(new Message(1L),2L);
    }

}
