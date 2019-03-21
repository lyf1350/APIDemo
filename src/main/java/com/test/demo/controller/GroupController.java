package com.test.demo.controller;


import com.test.demo.common.JsonResult;
import com.test.demo.model.Group;
import com.test.demo.repository.GroupRepository;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/group")
@Slf4j
public class GroupController {

    @Autowired
    GroupRepository groupRepository;
    @PostMapping("/create")
    @ApiOperation(value = "创建组", notes = "无", response = JsonResult.class)
    public JsonResult createGroup(Group group) {
        groupRepository.save(group);
        return JsonResult.success();
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除组", notes = "无", response = JsonResult.class)
    public JsonResult deleteGroup(Group group) {
        groupRepository.delete(group);
        return JsonResult.success();
    }

    @GetMapping("/list")
    @ApiOperation(value = "获得所有组", notes = "无", response = JsonResult.class)
    public JsonResult getGroups() {
        return JsonResult.success(groupRepository.findAll());
    }

}
