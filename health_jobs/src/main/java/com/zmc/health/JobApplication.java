package com.zmc.health;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * 包名: com.zmc.health
 *
 * @author zmc
 * 日期: 2021/8/3
 */

public class JobApplication {
    public static void main(String[] args) throws IOException {
     new ClassPathXmlApplicationContext("classpath:applictionContext-jobs.xml");
     System.in.read();
    }
}
