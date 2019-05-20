package com.test.demo.controller;


import cn.hutool.core.lang.Console;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.test.demo.common.JsonResult;
import com.test.demo.model.NodeTemplate;
import com.test.demo.model.User;
import com.test.demo.model.WorkflowTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.spring.web.json.Json;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Level;

@RestController
@RequestMapping("/api/test")
@Slf4j
public class TestController {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @GetMapping("")
    public JsonResult test(HttpServletRequest request) {
//        log.info("id:" + request.getSession().getId());
//        log.info("test api call user:" + request.getSession().getAttribute("user"));
//        List map=jdbcTemplate.queryForList("select * from file");
//        Console.log(map);
        log.info(request.getRequestURL().toString());
        return  JsonResult.success("");
    }

    @GetMapping("/test2/{id}")
    public String test2(int id) {
        return "test2" + id;
    }

    @PostMapping("/1/{path}")
    public void post(String path, HttpServletResponse response) {
        try {
            byte[] bytes = new byte[1024];
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream("D:\\" + path));
            int i = bufferedInputStream.read(bytes);
            OutputStream outputStream = response.getOutputStream();
            while (i != -1) {
                outputStream.write(bytes, 0, bytes.length);
                outputStream.flush();
                i = bufferedInputStream.read(bytes);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @GetMapping("/1/{path}")
    public void get(@PathVariable String path, HttpServletResponse response) {
        try {
            log.info("path:" + path);
            byte[] bytes = new byte[1024];
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream("D:\\" + path));
            int i = bufferedInputStream.read(bytes);
            OutputStream outputStream = response.getOutputStream();
            while (i != -1) {
                outputStream.write(bytes, 0, bytes.length);
                outputStream.flush();
                i = bufferedInputStream.read(bytes);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @GetMapping("/3")
    public JsonResult test3(String para) {
        return JsonResult.success(para);
    }

}
