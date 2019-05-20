package com.trade.jobs.sms;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    public static void main(String[] args) throws Exception {
        new ClassPathXmlApplicationContext("classpath:applicationContext-sms.xml");
    }

}
