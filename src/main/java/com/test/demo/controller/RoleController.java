package com.test.demo.controller;

import com.test.demo.common.JsonResult;
import com.test.demo.model.Role;
import com.test.demo.repository.RoleRepository;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/role")
@Slf4j
public class RoleController {

    @Autowired
    RoleRepository roleRepository;
    @PostMapping("/create")
    @ApiOperation(value = "创建角色", notes = "无", response = JsonResult.class)
    public JsonResult createRole(Role role) {

        roleRepository.save(role);
        return JsonResult.success();
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除角色", notes = "无", response = JsonResult.class)
    public JsonResult deleteRole(Role role) {
        roleRepository.delete(role);
        return JsonResult.success();
    }

    @GetMapping("/list")
    @ApiOperation(value = "获得所有角色", notes = "无", response = JsonResult.class)
    public JsonResult getRoles() {
        return JsonResult.success(roleRepository.findAll());
    }

}
