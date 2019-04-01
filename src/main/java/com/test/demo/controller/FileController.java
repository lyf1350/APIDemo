package com.test.demo.controller;


import com.test.demo.common.JsonResult;
import com.test.demo.repository.FileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.UUID;

@RestController
@RequestMapping("/api/file")
@Slf4j
public class FileController {
    @Autowired
    FileRepository fileRepository;
    @GetMapping("/list")
    public JsonResult getAllFiles(){
        return JsonResult.success(fileRepository.findAll());
    }

    @GetMapping("/{id}")
    public void getFile(@PathVariable String id, HttpServletResponse response){
        File file=new File("D:/"+id+".pdf");

        if(!file.exists()){
            file=new File("D:/"+fileRepository.findByUuid(id).getFileName());
        }
        if(!file.exists())
            return;
        try {
            byte[] bytes = new byte[1024];
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
            int i = bufferedInputStream.read(bytes);
            OutputStream outputStream = response.getOutputStream();
            while (i != -1) {
                outputStream.write(bytes, 0, bytes.length);
                outputStream.flush();
                i = bufferedInputStream.read(bytes);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @PostMapping("/upload")
    public JsonResult uploadFile(MultipartFile file){
        if(file.isEmpty())
            return JsonResult.error("上传失败");
        log.info("name:"+file.getName());
        log.info("name:"+file.getOriginalFilename());
        String uuid=UUID.randomUUID().toString();
        return JsonResult.success();
    }

}
