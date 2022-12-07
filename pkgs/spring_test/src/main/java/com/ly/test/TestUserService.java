package com.ly.test;

import com.ly.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class TestUserService {

    public static void main(String[] args) throws IOException {

        ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");

        UserService bean = ac.getBean(UserService.class);

        // bean.registerUser(100,"张三");

        bean.login(100, "张三");

    }
}
