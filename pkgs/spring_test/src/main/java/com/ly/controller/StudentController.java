package com.ly.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.ly.po.Student;
import com.ly.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Controller
public class StudentController {

     @Autowired
    StudentService ss;

    // 登录的功能
    @RequestMapping("login.do")
    @ResponseBody
    public  String  login(String nickname, String password, HttpSession session) throws IOException {
        String s = null;
        Student student = ss.login(nickname, password);
        if(student!=null){
            session.setAttribute("stuinfo",student);
            s = JSONObject.toJSONString(student);

            return  s;
        }

        System.out.println(s);
        return JSONObject.toJSONString(s);
    }

    @RequestMapping("movieview.do")
    public  String  movieTypeView(){


        return "movie_type";
    }


    @RequestMapping("list.do")
    @ResponseBody
    public  String  showAllstus() throws IOException {

        List<Student> students = ss.finAllStus();

        String s = JSONArray.toJSONString(students);

        System.out.println(s+"===========");

        return s;
    }

    @RequestMapping("delete.do")
    @ResponseBody
    public  String  deleteStu(String snickname) throws IOException {

        boolean b = ss.deleteStuByNickName(snickname);
      JSONObject jsonObject=new JSONObject();



        if(b){
            jsonObject.put("flag","ok");

        }else {

            jsonObject.put("flag","failed");
        }

        return jsonObject.toJSONString();

    }

    @RequestMapping("addStu.do")
    @ResponseBody
    public  String  addStu(Student student){

        System.out.println(student);
        boolean b1 = ss.checkNicName(student.getSnickname());
        System.out.println(b1+"=========");
        JSONObject jsonObject=new JSONObject();
        if(b1){
            boolean b = ss.input(student);
            if(b){
                jsonObject.put("flag","ok");

            }else {

                jsonObject.put("flag","failed");
            }

        }else {

            jsonObject.put("flag","erroruserinfo");
        }


        return jsonObject.toJSONString();

    }

    @RequestMapping("update.do")
    @ResponseBody
    public  String  updateStu(Student student){

        System.out.println(student+"==========");
        boolean b1 = ss.input(student);

        JSONObject jsonObject=new JSONObject();
        if(b1){
            boolean b = ss.input(student);
            if(b){
                jsonObject.put("flag","ok");

            }else {

                jsonObject.put("flag","failed");
            }

        }

        return  jsonObject.toJSONString();
    }

    // 模糊查询的方法

    @RequestMapping("likename.do")
    @ResponseBody
    public  String likename(String nickname) throws IOException {

        List<Student> students = ss.likeName(nickname);


        String s = JSONArray.toJSONString(students);

        return s;

    }

    @RequestMapping("logout.do")
    public  String  logout(HttpSession session){
//         清空 session 中的数据
        session.invalidate();
        return "login";
    }
}
