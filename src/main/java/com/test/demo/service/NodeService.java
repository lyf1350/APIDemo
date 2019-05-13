package com.test.demo.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.test.demo.common.SocketMessage;
import com.test.demo.model.Node;
import com.test.demo.model.User;
import com.test.demo.model.Workflow;
import com.test.demo.repository.UserRepository;
import com.test.demo.util.WebSocketUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NodeService {
    @Autowired
    UserRepository userRepository;
    public  Node getStartNode(Node node){
        return node.getPreviousNodes().size()>0?getStartNode(node.getPreviousNodes().get(0)):node;
    }
    public  void notifyUser(Node node,int type){
        Set<String> users=new HashSet<>();
        String msg;
        if(type==0){
            node.getSignoffs().forEach(signoff -> {
                switch(signoff.getReviewer().getType()){
                    case "user":
                        users.add(userRepository.getNameById(signoff.getReviewer().getReviewerId()));
                        break;
                    case "member":
                        users.addAll(userRepository.getNameByMemberId(signoff.getReviewer().getReviewerId()));
                        break;
                    case "group":
                        users.addAll(userRepository.getNameByGroupId(signoff.getReviewer().getReviewerId()));
                        break;
                    case "role":
                        users.addAll(userRepository.getNameByRoleId(signoff.getReviewer().getReviewerId()));
                        break;
                }
            });
            msg=JSON.toJSONString(new SocketMessage("node", JSON.toJSONString(node, SerializerFeature.DisableCircularReferenceDetect)));

        }else{
            Map<String,Set<Long>> idMap=new HashMap<>();
            idMap.put("user",new HashSet());
            idMap.put("member",new HashSet());
            idMap.put("role",new HashSet());
            idMap.put("group",new HashSet());
            Node startNode=getStartNode(node);
            getIds(startNode.getNextNodes(),idMap);
            if(idMap.get("user").size()>0)
                users.addAll(userRepository.getNameByIds(idMap.get("user")));
            if(idMap.get("member").size()>0)
                users.addAll(userRepository.getNameByMemberIds(idMap.get("member")));
            if(idMap.get("role").size()>0)
                users.addAll(userRepository.getNameByRoleIds(idMap.get("role")));
            if(idMap.get("group").size()>0)
                users.addAll(userRepository.getNameByGroupIds(idMap.get("group")));
            msg=JSON.toJSONString(new SocketMessage("workflow", JSON.toJSONString(node.getWorkflow(), SerializerFeature.DisableCircularReferenceDetect)));

        }

        users.forEach(user -> {
            WebSocketUtil.sendMessage(user,msg);
        });
    }

    private void getIds(List<Node> nodes,Map<String,Set<Long>> idMap){
        if(nodes==null||nodes.size()==0)
            return;
        nodes.forEach(node -> {
            node.getSignoffs().forEach(signoff ->
                idMap.get(signoff.getReviewer().getType()).add(signoff.getReviewer().getReviewerId())
            );
            getIds(node.getNextNodes(),idMap);
        });
    }

}
