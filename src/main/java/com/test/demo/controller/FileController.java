package com.test.demo.controller;


import cn.hutool.core.io.IoUtil;
import com.alibaba.fastjson.JSON;
import com.test.demo.common.JsonResult;
import com.test.demo.model.ESFile;
import com.test.demo.model.User;
import com.test.demo.repository.ESFileRepository;
import com.test.demo.repository.FileRepository;
import com.test.demo.util.ConvertUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.tika.Tika;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/file")
@Slf4j
public class FileController {
    @Autowired
    FileRepository fileRepository;
    @Autowired
    ESFileRepository esFileRepository;
    @Value("${file.dir}")
    private  String dir;
    @Value(("#{'${file.office}'.split(',')}"))
    List<String> officeList;
    Tika tika=new Tika();
    @GetMapping("/list")
    @ApiOperation("获得所有文件")
    public JsonResult getAllFiles() {
        return JsonResult.success(fileRepository.findAll());
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "获得预览文件", notes = "根据uuid获取需要预览的文件，如果没有找到生成的pdf文件，则返回源文件")
    public void getFile(@PathVariable String id, HttpServletResponse response) {
        File file = new File(dir + id + ".pdf");
        log.info("id:" + id);
        log.info("name:" + file.getAbsolutePath());
        if (!file.exists()) {
            log.info("find");
            com.test.demo.model.File f = fileRepository.findByUuid(id);
            if (f != null) {
                file = new File(dir + id + f.getSuffix());
            }
        }
        log.info("file:" + file.exists());
        if (!file.exists())
            return;
        try {
            InputStream inputStream = new FileInputStream(file);
            OutputStream outputStream = response.getOutputStream();

            IoUtil.copy(inputStream, outputStream);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/download/{id}")
    @ApiOperation("下载文件")
    public void downLoadFile(@PathVariable String id, HttpServletResponse response) {
        com.test.demo.model.File f = fileRepository.findByUuid(id);
        File file = new File(dir + id + f.getSuffix());

        if (!file.exists())
            return;
        try {
            InputStream inputStream = new FileInputStream(file);
            OutputStream outputStream = response.getOutputStream();
            response.setContentType("application/x-download");
            String header = "attachment;filename=" + f.getFileName();
            response.addHeader("Content-Disposition", header);
            IoUtil.copy(inputStream, outputStream);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/upload")
    @ApiOperation(value = "上传文件", notes = "上传文件，如果是office文件，则生成pdf文件")
    public JsonResult uploadFile(MultipartFile file,@SessionAttribute User user) throws Exception {
        if (file.isEmpty())
            return JsonResult.error("上传失败");
        log.info("officeList:"+officeList);
        log.info("dir");
        String name = file.getOriginalFilename();
        String suffix = name.substring(name.lastIndexOf('.'));
        String uuid = UUID.randomUUID().toString();
        File file2=new File(dir + uuid + suffix);
        file.transferTo(file2);

//        List suffixList = Arrays.asList(".xls", ".xlsx", ".doc", ".docx", ".ppt", ".pptx");
//        if (suffixList.contains(suffix)) {
//            ConvertUtil.office2PDF(dir + uuid + suffix, dir + uuid + ".pdf");
//        }
        esFileRepository.save(new ESFile(uuid,name,tika.parseToString(new FileInputStream(file2))));
        return JsonResult.success(fileRepository.save(new com.test.demo.model.File(name, uuid, suffix,user)));
    }

    @PostMapping("/delete")
    @ApiOperation("删除文件")
    public JsonResult deleteFile(com.test.demo.model.File file) {
        log.info("file:" + file);
        fileRepository.delete(file);
        File file1=new File(dir+file.getUuid()+file.getSuffix());
        if(file1.exists()){
            file1.delete();
            file1=new File(dir+file.getUuid()+".pdf");
            if(file1.exists())
                file1.delete();
        }

        return JsonResult.success();
    }

    @GetMapping("/get")
    public JsonResult getFiles(String str){
        List<String> list= JSON.parseArray(str,String.class);
        List<com.test.demo.model.File> files=new ArrayList<>();
        list.forEach(uuid->{
            com.test.demo.model.File temp=fileRepository.findByUuid(uuid);
            if(temp!=null)
                files.add(temp);
        });
        return  JsonResult.success(files);
    }

    @GetMapping("/search")
    public JsonResult search(String keyword){
        SearchQuery searchQuery=new NativeSearchQueryBuilder().withQuery(new QueryStringQueryBuilder(keyword)).build();
        return JsonResult.success(esFileRepository.search(searchQuery));
    }

}