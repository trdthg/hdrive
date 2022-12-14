package com.ly.service;

import com.ly.dao.StudentDao;
import com.ly.po.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class StudentService {

    @Autowired
    StudentDao sdao;

    public Student  login(String nickName,String password) throws IOException {


         return sdao.findStudenBuNameAndpasword(nickName,password);

    }

    public List<Student> finAllStus() throws IOException {


        return  sdao.finAllstus();
    }

    public boolean deleteStuByNickName(String snickname) {

          return  sdao.deleteByNickName(snickname);

    }

    public boolean input(Student student)  {


        return sdao.inputStu(student);
    }

    public boolean checkNicName(String snickname) {

        return  sdao.checkNick(snickname);
    }

    public List<Student> likeName(String nickname) throws IOException {

        return  sdao.likeName(nickname);
    }
}
