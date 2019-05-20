package com.test.demo;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.test.demo.common.Invoke;
import com.test.demo.model.Message;
import com.test.demo.repository.MessageRepository;
import com.test.demo.repository.MessageStateRepository;
import com.test.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.*;
import java.util.logging.Formatter;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
public class TestClass {

    public static void main(String[] Args){
        try{
            Class c= Invoke.class;
            Method[] methods=c.getDeclaredMethods();
            for(int i=0;i<methods.length;i++){
                System.out.println(methods[i].getName());
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println(e.toString());
            System.out.println(ArrayUtil.join(e.getStackTrace(),"\n"));
        }

    }

    public static int office2PDF(String sourceFile, String destFile) throws FileNotFoundException {
        try {
            File inputFile = new File(sourceFile);
            if (!inputFile.exists()) {
                return -1;// 找不到源文件, 则返回-1
            }

            // 如果目标路径不存在, 则新建该路径
            File outputFile = new File(destFile);
            if (!outputFile.getParentFile().exists()) {
                outputFile.getParentFile().mkdirs();
            }

            OpenOfficeConnection connection = new SocketOpenOfficeConnection("127.0.0.1", 8100);
            connection.connect();

            // convert
            DocumentConverter converter = new OpenOfficeDocumentConverter(
                    connection);
            converter.convert(inputFile, outputFile);

            // close the connection
            connection.disconnect();

            return 0;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 1;
    }
    public void test(String a){
        System.out.println("a:"+a);
    }
}
