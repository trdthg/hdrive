package com.ly.service;

import com.ly.dao.UserDao;
import lombok.Data;

import java.io.IOException;

@Data
public class UserService {

  UserDao userDao;

  public void registerUser(Integer cid, String cname) throws IOException {

    userDao.addUser(cid, cname);
  }

  public void login(Integer cid, String cname) throws IOException {

    userDao.findUserBycidAndName(1, "zhangsan");

  }

}
