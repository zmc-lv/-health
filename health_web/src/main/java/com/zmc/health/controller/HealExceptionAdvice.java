package com.zmc.health.controller;

import com.zmc.health.entity.Result;
import com.zmc.health.exception.MyException;
import org.aspectj.weaver.ast.Var;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 包名: com.zmc.health.controller
 *
 * @author zmc
 * 日期: 2021/7/31
 */
@RestControllerAdvice
public class HealExceptionAdvice {

    private static final Logger logger = LoggerFactory.getLogger(HealExceptionAdvice.class);
    /**
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MyException.class)
    public Result handleMyException(MyException e) {
        return new Result(false, e.getMessage());
    }

    /**
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {

        logger.error("发现未知异常");
        return new Result(false,"发现未知异常，请稍后重试");
    }
}
