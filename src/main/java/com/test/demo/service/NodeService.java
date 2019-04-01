package com.test.demo.service;

import com.alibaba.fastjson.JSON;
import com.test.demo.common.SocketMessage;
import com.test.demo.model.Node;
import com.test.demo.model.User;
import com.test.demo.repository.UserRepository;
import com.test.demo.util.WebSocketUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class NodeService {
    @Autowired
    UserRepository userRepository;
    public void notifyUser(Node node){
        Set<User> users=new HashSet<>();
        node.getSignoffs().forEach(signoff -> {
            switch(signoff.getReviewer().getType()){
                case "user":
                    users.add(userRepository.findById(signoff.getReviewer().getReviewerId()).get());
                    break;
                case "member":
                    users.addAll(userRepository.findByMemberId(signoff.getReviewer().getReviewerId()));
                    break;
                case "group":
                    users.addAll(userRepository.findByGroupId(signoff.getReviewer().getReviewerId()));
                    break;
                case "role":
                    users.addAll(userRepository.findByRoleId(signoff.getReviewer().getReviewerId()));
                    break;
            }
        });
        String msg=JSON.toJSONString(new SocketMessage("workflow", JSON.toJSONString(node)));
        users.forEach(user -> {
            WebSocketUtil.sendMessage(user.getUsername(),msg);
        });
    }
}
