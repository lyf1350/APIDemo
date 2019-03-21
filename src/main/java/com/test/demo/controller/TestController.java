package com.test.demo.controller;


import com.test.demo.common.JsonResult;
import com.test.demo.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/test")
@Slf4j
public class TestController {

    @GetMapping("")
    public String test(HttpServletRequest request){
        log.info("id:"+request.getSession().getId());
        log.info("test api call user:"+request.getSession().getAttribute("user"));
        return "abc";
    }
    @GetMapping("/test2/{id}")
    public String test2(int id){
        return "test2"+id;
    }
    @PostMapping("/1")
    public JsonResult post(List<String> dest){
        log.info("user:"+dest);
        return JsonResult.success(new User("a","b","c","d"));
    }
    @GetMapping("/3")
    public JsonResult test3(String para){

        return JsonResult.success(para);
    }

}
