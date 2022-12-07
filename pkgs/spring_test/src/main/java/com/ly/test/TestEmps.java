package com.ly.test;

import com.ly.service.EmployeeService;
import org.apache.hadoop.hbase.client.Result;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class TestEmps {

    public static void main(String[] args) throws IOException {

        ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");

        EmployeeService bean = ac.getBean(EmployeeService.class);

        Result aa = bean.login("aa", "1234");

        System.out.println(aa);

    }
}
