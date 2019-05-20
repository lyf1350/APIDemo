package com.test.demo.controller;


import com.test.demo.common.JsonResult;
import com.test.demo.model.User;
import com.test.demo.repository.UserRepository;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {
    @Autowired
    UserRepository userRepository;
    @PostMapping("/login")
    @ApiOperation(value = "登录", notes = "使用用户名密码登录", response = JsonResult.class)
    public JsonResult login(String username, String password, HttpServletRequest request) {
        if (username == null || password == null) {
            return JsonResult.error();
        }
        User user = userRepository.findByUsername(username);
        if (user == null)
            return JsonResult.error();
        if(user.getState()==0)
            return JsonResult.error();
        if (user.getPassword().equals(password)) {
            request.getSession().setAttribute("user", user);
            return JsonResult.success(user);
        } else
            return JsonResult.error();
    }

    @PostMapping("/register")
    @ApiOperation(value = "注册", notes = "无", response = JsonResult.class)
    public JsonResult register(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return JsonResult.error("用户名重复");
        } else {
            System.out.println("register user:" + user);
            userRepository.save(user);
            return JsonResult.success();
        }
    }

    @GetMapping("/logout")
    @ApiOperation(value = "登出", notes = "无", response = JsonResult.class)
    public JsonResult logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return JsonResult.success();
    }

    @GetMapping("/list")
    @ApiOperation(value = "获得所有用户", notes = "无", response = JsonResult.class)
    public JsonResult getUsers() {
        return JsonResult.success(userRepository.findAll());
    }

    @GetMapping("/list/valid")
    @ApiOperation(value = "获得所有有效用户", notes = "无", response = JsonResult.class)
    public JsonResult getValidUsers() {
        return JsonResult.success(userRepository.findAllByState(1));
    }

    @PostMapping("/save")
    @ApiOperation(value = "保存", notes = "无", response = JsonResult.class)
    public JsonResult save(User user) {
        if(user.getPassword()==null)
            user.setPassword(userRepository.findByUsername(user.getUsername()).getPassword());
        userRepository.save(user);
        return JsonResult.success();

    }
}
