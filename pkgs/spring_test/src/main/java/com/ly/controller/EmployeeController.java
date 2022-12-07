package com.ly.controller;

import com.ly.po.Employee;
import com.ly.service.EmployeeService;
import org.apache.hadoop.hbase.client.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

@Controller
public class EmployeeController {

    @Autowired
    EmployeeService es;

    @RequestMapping("register.do")
    public String register(String sname, String spass) throws UnsupportedEncodingException {

        String sname1 = new String(sname.getBytes("iso8859-1"), "utf-8");

        System.out.println(sname1 + spass);

        return "success";
    }

    @RequestMapping("register1.do")
    public String register1(String sname, String spass) throws UnsupportedEncodingException {

        System.out.println(sname + spass);

        return "success";
    }

    @RequestMapping("register2.do")
    public String register2(String sname, String spass) throws UnsupportedEncodingException {

        System.out.println(sname + spass);

        return "success";
    }

    @RequestMapping("registeremp.do")
    public String registerEmp(Employee employee) throws IOException {

        boolean b = es.checkNick(employee.getNickname());
        System.out.println(b + "======");
        if (b) {
            es.inputEmp(employee);
            return "success";
        }

        return "failed";

    }

    @RequestMapping("login.do")
    public String login(String nickname, String pswd) throws IOException {

        Result result = es.login(nickname, pswd);

        if (result != null) {

            return "show";
        }
        return "failed";
    }

}
