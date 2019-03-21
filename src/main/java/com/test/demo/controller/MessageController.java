package com.test.demo.controller;


import com.alibaba.fastjson.JSON;
import com.test.demo.common.JsonResult;
import com.test.demo.model.Message;
import com.test.demo.model.MessageState;
import com.test.demo.model.User;
import com.test.demo.repository.MessageRepository;
import com.test.demo.repository.MessageStateRepository;
import com.test.demo.repository.UserRepository;
import com.test.demo.service.MailService;
import com.test.demo.util.WebSocketUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/message")
@Slf4j
public class MessageController {
    @Autowired
    MailService mailService;
    @Autowired
    MessageRepository messageRepository;
    @Autowired
    MessageStateRepository messageStateRepository;
    @Autowired
    UserRepository userRepository;




    @PostMapping("/send")
    @ApiOperation(value = "发送邮件或站内信", notes = "无", response = JsonResult.class)

    public JsonResult send(String source, String dest, String title, String content, String type, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");

        if (user == null)
            return JsonResult.error("用户未登录");
        List<String> destList = JSON.parseObject(dest, List.class);
        List<String> typeList = JSON.parseObject(type, List.class);
        if (destList.size() != typeList.size())
            return JsonResult.error("参数错误");
        Set<User> userSet = new HashSet();
        for (int i = 0; i < destList.size(); i++) {
            switch (typeList.get(i)) {
                case "user":
                    userSet.add(userRepository.findByUsername(destList.get(i)));
                    break;
                case "role":
                    userSet.addAll(userRepository.findByRole(destList.get(i)));
                    break;
                case "group":
                    userSet.addAll(userRepository.findByGroup(destList.get(i)));
                    break;
                default:
                    break;
            }
        }
        Message message = new Message(source, dest, type, title, content);

        message = messageRepository.save(message);
        for (User u : userSet) {
            MessageState messageState = messageStateRepository.save(new MessageState(u.getID(), 0, message));
            WebSocketUtil.sendMessage(u.getUsername(), JSON.toJSONString(messageState));
        }

        return JsonResult.success();
    }


    @GetMapping("/update")
    @ApiOperation(value = "更新消息状态", notes = "无", response = JsonResult.class)
    public JsonResult updateMessageState(Long id) {
        messageStateRepository.updateById(id);
        return JsonResult.success();
    }

    @GetMapping("/delete")
    @ApiOperation(value = "删除消息", notes = "无", response = JsonResult.class)
    public JsonResult deleteMessageState(Long id) {
        messageStateRepository.deleteById(id);
        return JsonResult.success();
    }

    @GetMapping("/list")
    @ApiOperation(value = "获得用户站内消息", notes = "无", response = JsonResult.class)
    public JsonResult getMessages(Long id) {
        return JsonResult.success(messageStateRepository.findByUserId(id));
    }


}
