package com.test.demo.controller;


import com.alibaba.fastjson.JSON;
import com.test.demo.common.JsonResult;
import com.test.demo.model.NodeTemplate;
import com.test.demo.model.User;
import com.test.demo.model.WorkflowTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

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
    public JsonResult post(String nodeArray, String fromMap, String toMap, WorkflowTemplate workflowTemplate){
        log.info("nodeData:"+nodeArray);
        log.info(("fromMap:"+fromMap));
        log.info(("toMap:"+toMap));
        log.info(("workflowTemplate:"+workflowTemplate));
        List<NodeTemplate> nodeTemplates=JSON.parseArray(nodeArray,NodeTemplate.class);
        Map<String,List<String>> from=JSON.parseObject(fromMap,Map.class);
        Map<String,List<String>> to=JSON.parseObject(toMap,Map.class);
        log.info("nodeTemplate:"+nodeTemplates);
        log.info("from:"+from);
        log.info("to:"+to);
        return JsonResult.success(new User("a","b","c","d"));

    }
    @GetMapping("/3")
    public JsonResult test3(String para){

        return JsonResult.success(para);
    }

}
