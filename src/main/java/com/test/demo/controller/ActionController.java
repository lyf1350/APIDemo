package com.test.demo.controller;

import com.test.demo.common.Invoke;
import com.test.demo.common.JsonResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/action")
public class ActionController {
    @GetMapping("methods")
    public JsonResult getAllMethods(){
        Class c= Invoke.class;
        Method[] methods=c.getDeclaredMethods();
        List<String> methodList=new ArrayList<>();
        for(Method m:methods){
            methodList.add(m.getName());
        }
        return JsonResult.success(methodList);
    }
}
