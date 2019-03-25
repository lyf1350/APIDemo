package com.test.demo;

import com.alibaba.fastjson.JSON;
import com.test.demo.model.Message;
import com.test.demo.repository.MessageRepository;
import com.test.demo.repository.MessageStateRepository;
import com.test.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestClass {

    public static void main(String[] Args){
        String a="root";
        String b="root";
        System.out.println("equlas:"+a.equals(b));
        Map<String, List<String>>  testMap=new HashMap<>();
        List<String> list=new ArrayList<>();
        list.add("1");
        testMap.put("1",list);
        testMap.forEach((key,value)->{
            System.out.println("key:"+key.getClass());
            System.out.println(("value:"+value.getClass()));
        });
    }
}
