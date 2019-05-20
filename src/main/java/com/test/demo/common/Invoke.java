package com.test.demo.common;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Invoke {
    public void test(String abc){
        log.info("invoke test");
    }
    public String test2(String def){
        log.info("invoke test2");
        return "error invoke";
    }
}
