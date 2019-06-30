package com.test.demo.controller;


import com.test.demo.common.JsonResult;
import com.test.demo.model.User;
import com.test.demo.repository.UserRepository;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
        Subject sub = SecurityUtils.getSubject();
        UsernamePasswordToken token=new UsernamePasswordToken(username,password);
        try{
            sub.login(token);
        }catch (UnknownAccountException uae){
            log.error("uae");
            return JsonResult.error("未知账户",400);
        }catch (LockedAccountException lae){
            log.error("lae");
            return JsonResult.error("账户已过期",423);

        }catch (ExcessiveAttemptsException eae){
            log.error("eae");
            return JsonResult.error("请求超限",429);

        }catch (AuthenticationException ae){
            log.error("ae");
            return JsonResult.error("认证出错",500);
        }finally {
            token.clear();
        }
        return JsonResult.success(request.getSession().getAttribute("user"));

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
        SecurityUtils.getSubject().logout();
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

    @GetMapping("/logged")
    public JsonResult user(HttpSession session){
        log.info("subject:",SecurityUtils.getSubject().getPrincipal());
        Object obj=SecurityUtils.getSubject().getPrincipal();
        if(obj!=null&&obj instanceof User){
            session.setAttribute("user",obj);
            return JsonResult.success(obj);
        }
        return JsonResult.error();
    }
}
