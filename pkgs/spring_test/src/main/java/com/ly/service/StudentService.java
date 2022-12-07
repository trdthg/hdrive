package com.ly.service;

import com.ly.dao.StudentDao;

public class StudentService {

    StudentDao dao;

    public void setDao(StudentDao dao) {
        this.dao = dao;
    }

    public void register() {

        System.out.println("业务逻辑层的处理");

        dao.add();
    }
}
