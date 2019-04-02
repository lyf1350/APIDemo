package com.test.demo.controller;


import com.test.demo.common.JsonResult;
import com.test.demo.repository.FileRepository;
import com.test.demo.util.ConvertUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/file")
@Slf4j
public class FileController {
    @Autowired
    FileRepository fileRepository;
    @GetMapping("/list")
    @ApiOperation("获得所有文件")
    public JsonResult getAllFiles(){
        return JsonResult.success(fileRepository.findAll());
    }

    @GetMapping("/{id}")
    @ApiOperation(value="获得预览文件",notes="根据uuid获取需要预览的文件，如果没有找到生成的pdf文件，则返回源文件")
    public void getFile(@PathVariable String id, HttpServletResponse response){
        File file=new File("D:/upload/"+id+".pdf");
        log.info("id:"+id);
        log.info("name:"+file.getAbsolutePath());
        if(!file.exists()){
            log.info("find");
            com.test.demo.model.File f=fileRepository.findByUuid(id);
            if(f!=null){
                file=new File("D:/upload/"+id+f.getSuffix());
            }
        }
        log.info("file:"+file.exists());
        if(!file.exists())
            return;
        try {
            InputStream inputStream=new FileInputStream(file);
            OutputStream outputStream = response.getOutputStream();

            IOUtils.copy(inputStream,outputStream);
            outputStream.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @GetMapping("/download/{id}")
    @ApiOperation("下载文件")
    public void downLoadFile(@PathVariable String id, HttpServletResponse response){
        com.test.demo.model.File f=fileRepository.findByUuid(id);
        File file=new File("D:/upload/"+id+f.getSuffix());

        if(!file.exists())
            return;
        try {
            InputStream inputStream=new FileInputStream(file);
            OutputStream outputStream = response.getOutputStream();
            response.setContentType("application/x-download");
            String header="attachment;filename="+f.getFileName();
            response.addHeader("Content-Disposition",header);
            IOUtils.copy(inputStream,outputStream);
            outputStream.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/upload")
    @ApiOperation(value="上传文件",notes="上传文件，如果是office文件，则生成pdf文件")
    public JsonResult uploadFile (MultipartFile file){
        if(file.isEmpty())
            return JsonResult.error("上传失败");
        String name=file.getOriginalFilename();
        String suffix=name.substring(name.lastIndexOf('.'));
        String uuid=UUID.randomUUID().toString();
        List suffixList= Arrays.asList(".xls",".xlsx",".doc",".docx",".ppt",".pptx");
        try{
            file.transferTo(new File("D:/upload/"+uuid+suffix));
            if(suffixList.contains(suffix)){
                ConvertUtil.office2PDF("D:/upload/"+uuid+suffix,"D:/upload/"+uuid+".pdf");
            }
            fileRepository.save(new com.test.demo.model.File(name,uuid,suffix));
        }catch (Exception e){
            e.printStackTrace();
        }

        return JsonResult.success();
    }

    @PostMapping("/delete")
    @ApiOperation("删除文件")
    public JsonResult deleteFile(com.test.demo.model.File file){
        log.info("file:"+file);
        fileRepository.delete(file);
        return JsonResult.success();
    }

}
