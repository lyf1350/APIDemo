package com.test.demo.controller;

import cn.hutool.core.lang.Console;
import com.test.demo.common.JsonResult;
import com.test.demo.model.Config;
import com.test.demo.repository.ConfigRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/config")
@Slf4j
public class ConfigController {
    @Autowired
    ConfigRepository configRepository;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @PostMapping("createOrSave")
    public JsonResult createOrSave(Config config){
        log.info("config:"+config);
        if(config.getType().equals("holiday")){
            Config temp=configRepository.findByConfigName(config.getConfigName());
            if(temp!=null)
                config.setId(temp.getId());
        }
        return JsonResult.success(configRepository.save(config));
    }

    @GetMapping("delete")
    public JsonResult delete(Long id){
        configRepository.deleteById(id);
        return JsonResult.success();
    }


    @GetMapping("get")
    public JsonResult get(String name){
        return JsonResult.success(configRepository.findByConfigName(name));
    }
    @GetMapping("getTables")
    public JsonResult getTables(){
        return JsonResult.success(configRepository.findAllByType("table"));
    }
    @GetMapping("holiday")
    public JsonResult getHolidays(){
        List<Config> configs=configRepository.findAllByType("holiday");
        Map<String,String> map=new HashMap<>();
        configs.forEach(config -> map.put(config.getConfigName(),config.getVal()));
        return JsonResult.success(map);
    }
}
