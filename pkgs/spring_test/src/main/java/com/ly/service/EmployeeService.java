package com.ly.service;

import com.ly.dao.EmployeeDao;
import com.ly.po.Employee;
import org.apache.hadoop.hbase.client.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmployeeService {

    @Autowired
    EmployeeDao edao;

    public void inputEmp(Employee employee) throws IOException {

        edao.addEmp(employee);
    }

    public boolean checkNick(String nickname) throws IOException {

        boolean b = edao.checkRowKey(nickname);
        return b;
    }

    public Result login(String nickname, String pass) throws IOException {

        Result emp = edao.getEmp(nickname, pass);

        return emp;
    }

}
