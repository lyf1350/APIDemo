package com.test.demo.model;

import cn.hutool.core.util.ArrayUtil;
import com.test.demo.common.Invoke;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.lang.reflect.Method;
import java.util.List;

@Entity
@Data
@Slf4j
public class Action {
    @Id
    @GeneratedValue
    Long id;
    @Column
    String type;
    @Column
    String action;
    @Column
    String actionArguments;

    public Action(){

    }
    public Action(String type, String action, String actionArguments) {
        this.type = type;
        this.action = action;
        this.actionArguments = actionArguments;
    }

    public Object execute(String type){
        if(!type.equals(type))
            return null;
        try{
            Class c= Invoke.class;
            Method m=c.getMethod(action,String.class);
            if(m!=null)
                return m.invoke(c.newInstance(),actionArguments);
        }catch (Exception e){
            log.error(e.getMessage());
            log.error(ArrayUtil.join(e.getStackTrace(),"\n"));
        }
        return "execute action:"+action+" failed";
    }
    @Override
    public boolean equals(Object action){
        if(action instanceof Action){
            if(((Action) action).getId().equals(this.getId()))
                return true;
        }
        return false;
    }
    @Override
    public int hashCode(){
        return this.getId().hashCode();
    }
}
