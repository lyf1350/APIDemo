package com.test.demo.controller;

import cn.hutool.core.lang.Console;
import com.test.demo.common.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/table")
@Slf4j
public class TableController {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @GetMapping("desc")
    public JsonResult desc(String name){
        List<String> result=new ArrayList();
        List map=jdbcTemplate.queryForList("desc "+name);
        map.forEach(e->{
            result.add(((Map)e).get("Field").toString());
        });
        Console.log(map);
        return JsonResult.success(result);
    }

    @GetMapping("get")
    public JsonResult get(String name){
        return JsonResult.success(jdbcTemplate.queryForList("select * from "+name));
    }
}
