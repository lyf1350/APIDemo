package com.test.demo.controller;

import com.test.demo.common.JsonResult;
import com.test.demo.model.Group;
import com.test.demo.model.Member;
import com.test.demo.model.Role;
import com.test.demo.model.User;
import com.test.demo.repository.MemberRepository;
import com.test.demo.repository.UserRepository;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/member")
@Slf4j
public class MemberController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    MemberRepository memberRepository;

    @PostMapping("/attach")
    @ApiOperation(value = "用户关联成员", notes = "无", response = JsonResult.class)
    public JsonResult attachMember(Group group, Role role, String username) {
        Member member = memberRepository.findByGroupAndRole(group, role);
        if (member == null) {
            member = new Member(group, role);
            member = memberRepository.save(member);
        }
        User user = userRepository.findByUsername(username);
        if(!user.getMembers().contains(member)){
            user.getMembers().add(member);
            userRepository.save(user);
            return JsonResult.success(member);
        }
        return JsonResult.error();
    }

    @PostMapping("/detach")
    @ApiOperation(value = "用户取消关联成员", notes = "无", response = JsonResult.class)
    public JsonResult detachMember(Member member, String username) {

        User user = userRepository.findByUsername(username);
        user.getMembers().remove(member);
        userRepository.save(user);
        return JsonResult.success();
    }

    @GetMapping("/list")
    public JsonResult getAllMembers(){
        return  JsonResult.success(memberRepository.findAll());
    }
}
