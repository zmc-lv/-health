package com.zmc.health.service;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * Package_Name: com.zmc.health.service
 *
 * @author zmc
 * Date: 2021/7/29
 */
public class ServiceApplication {

    public static void main(String[] args) throws IOException {
        new ClassPathXmlApplicationContext("classpath:applicationContext-service.xml");
        System.in.read();
    }
}
