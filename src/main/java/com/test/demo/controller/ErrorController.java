package com.test.demo.controller;

import com.test.demo.common.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class ErrorController  {

    @ResponseBody
    @ExceptionHandler(BindException.class)
    public JsonResult bindExceptionHandler(BindException ex) {
        StringBuffer sb = new StringBuffer();
        ex.getFieldErrors().forEach(fieldError -> sb.append(fieldError.getDefaultMessage()).append(","));
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        log.error("参数绑定异常", ex);
        return JsonResult.error(sb.toString());
    }



    @ResponseBody
    @ExceptionHandler(DataAccessException.class)
    public JsonResult serviceExceptionExceptionHandler(DataAccessException ex) {
        log.error("触发数据只读异常", ex);
        return JsonResult.error("触发数据只读异常！");
    }
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public JsonResult serviceExceptionExceptionHandler(Exception ex) {
        log.error("系统未知异常", ex);
        return JsonResult.error("系统未知异常");
    }

}
