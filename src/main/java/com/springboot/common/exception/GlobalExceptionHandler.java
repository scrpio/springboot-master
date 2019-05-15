package com.springboot.common.exception;

import com.springboot.common.util.ResultUtil;
import com.springboot.model.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 用于捕获和处理Controller抛出的异常
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    private final static Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(AuthException.class)
    @ResponseStatus(value = HttpStatus.OK)
    public Result<Object> handleAuthentication(AuthException e) {
        LOG.info("Authentication Exception handler:" + e.getMsg());
        return new ResultUtil<>().setErrorMsg(500, e.getMsg());
    }

    @ExceptionHandler(TransException.class)
    @ResponseStatus(value = HttpStatus.OK)
    public Result<Object> handleTransException(TransException e) {
        LOG.info("TransException handler:", e.getMsg());
        return new ResultUtil<>().setErrorMsg(500, e.getMsg());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.OK)
    public Result<Object> allExceptionHandler(Exception e) {
        LOG.info("Exception handler:" + e.getMessage());
        return new ResultUtil<>().setErrorMsg(500, e.getMessage());
    }
}
