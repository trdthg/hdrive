package com.ly.test;

import com.ly.bean.Student;
import com.ly.service.StudentService;
import com.ly.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class SpringTest {

    public static void main(String[] args) {

        // 初始化 spring 容器
        ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
        // 通过容器 去找 bean 对象
        // 通过配置文件中的 id 获取 对象
        // Student student = (Student) ac.getBean("student");
        //
        //
        // System.out.println(student.toString());

        // ac.getBean("stus")
        StudentService bean = ac.getBean(StudentService.class);

        bean.register();
    }

}
