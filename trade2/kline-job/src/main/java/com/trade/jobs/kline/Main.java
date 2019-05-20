package com.trade.jobs.kline;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;

public class Main {

    public static void main(String[] args) throws Exception {
        long startTime = System.currentTimeMillis();
        System.out.printf("USE SYSTEM %s %s %s \n", System.getProperty("os.name"), System.getProperty("os.arch"), System.getProperty("os.version"));
        System.out.printf("USE JDK %s %s \n", System.getProperty("java.version"), System.getProperty("java.vm.specification.name"));
        new ClassPathXmlApplicationContext("classpath:applicationContext-kline.xml");
        System.out.printf("kine job started take %d ms\n", (System.currentTimeMillis() - startTime));
        System.out.println("Start Time " + new Date());
    }

}
