package com.test.demo;


import com.alibaba.fastjson.JSON;
import com.test.demo.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;

@SpringBootApplication
@EnableJpaAuditing
@Slf4j
public class ApiDemoApplication {

    @Bean
    ConfigurableWebBindingInitializer getConfigurableWebBindingInitializer(){
        ConfigurableWebBindingInitializer initializer=new ConfigurableWebBindingInitializer();
        FormattingConversionService service=new DefaultFormattingConversionService();
        initializer.setConversionService(service);
        service.addConverter(new Converter<String, User>() {
            @Override
            public User convert(String s) {
                log.info("convert user:"+s);
                return JSON.parseObject(s,User.class);
            }
        });
        service.addConverter(new Converter<String, Group>() {
            @Override
            public Group convert(String s) {
                log.info("convert group:"+s);

                return JSON.parseObject(s, Group.class);
            }
        });
        service.addConverter(new Converter<String, Role>() {
            @Override
            public Role convert(String s) {
                log.info("convert role:"+s);
                return JSON.parseObject(s,Role.class);
            }
        });
        service.addConverter(new Converter<String, Member>() {
            @Override
            public Member convert(String s) {
                log.info("convert Member:"+s);
                return JSON.parseObject(s,Member.class);
            }
        });
        service.addConverter(new Converter<String, NodeTemplate>() {
            @Override
            public NodeTemplate convert(String s) {
                log.info("convert NodeTemplate:"+s);
                return JSON.parseObject(s,NodeTemplate.class);
            }
        });
        service.addConverter(new Converter<String, Reviewer>() {
            @Override
            public Reviewer convert(String s) {
                log.info("convert Reviewer:"+s);
                return JSON.parseObject(s,Reviewer.class);
            }
        });
        service.addConverter(new Converter<String, WorkflowTemplate>() {
            @Override
            public WorkflowTemplate convert(String s) {
                log.info("convert WorkflowTemplate:"+s);
                return JSON.parseObject(s,WorkflowTemplate.class);
            }
        });
        service.addConverter(new Converter<String, Workflow>() {
            @Override
            public Workflow convert(String s) {
                log.info("convert Workflow:"+s);
                return JSON.parseObject(s,Workflow.class);
            }
        });
        service.addConverter(new Converter<String, Node>() {
            @Override
            public Node convert(String s) {
                log.info("convert Node:"+s);
                return JSON.parseObject(s,Node.class);
            }
        });
        service.addConverter(new Converter<String, File>() {
            @Override
            public File convert(String s) {
                log.info("convert Node:"+s);
                return JSON.parseObject(s,File.class);
            }
        });
        return initializer;
    }

    public static void main(String[] args) throws Exception{
        SpringApplication.run(ApiDemoApplication.class);
    }

}
