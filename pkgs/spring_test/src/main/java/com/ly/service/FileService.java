package com.ly.service;

import com.ly.dao.FileDao;
import com.ly.po.HFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class FileService {
    @Autowired
    FileDao  fd;

    // 文件上传

    public  boolean uploadFIle(HFile hFile){


        return  fd.uploadFile(hFile);
    }

    public List<HFile> findList() throws IOException {

        return  fd.ListFiles();
    }

    public boolean deleteFile(String  nickname) {

        return  fd.delete(nickname);
    }

    public String findPath(String snickname) {

        return  fd.findPath(snickname);
    }

    //
}
