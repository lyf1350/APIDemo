package com.test.demo;

import com.alibaba.fastjson.JSON;
import com.test.demo.model.Message;
import com.test.demo.repository.MessageRepository;
import com.test.demo.repository.MessageStateRepository;
import com.test.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class TestClass {

    public static void main(String[] Args){
        String a="root";
        String b="root";
        System.out.println("equlas:"+a.equals(b));
    }
}
