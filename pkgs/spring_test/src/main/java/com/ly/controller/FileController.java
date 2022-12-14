package com.ly.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ly.dao.conn.HdfsConn;
import com.ly.po.HFile;
import com.ly.service.FileService;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
public class FileController {

    @Autowired
    FileService fs;


    @RequestMapping("fileupload.do")
    @ResponseBody
    public  String  uploadFile(String snickname,@RequestParam("file") MultipartFile file) throws IOException {

        JSONObject jsonObject=new JSONObject();

        System.out.println(snickname+"====="+file.getOriginalFilename());

//     通过file 文件可以获取 输入流；
        InputStream ins = file.getInputStream();

        FileSystem fileSystem = HdfsConn.getFileSystem();
        String s = new String(file.getOriginalFilename().getBytes("iso8859-1"),"utf-8");
        System.out.println(s+"========");
        String  pth="/"+snickname+"/"+s;
        Path path=new Path(pth);
        FSDataOutputStream fos = fileSystem.create(path);

        IOUtils.copyBytes(ins,fos,HdfsConn.getConfiguration());

        IOUtils.closeStream(ins);
        IOUtils.closeStream(fos);


        boolean b = fs.uploadFIle(new HFile(snickname, pth));

        System.out.println(b+"=======");

        if(b){
            jsonObject.put("msg","ok");
        }else{

            jsonObject.put("msg","failed");
        }

        System.out.println("========数据======");

        return jsonObject.toJSONString();

    }

    @RequestMapping("listfile.do")
    @ResponseBody
    public String  showFiles() throws IOException {

       List<HFile> list= fs.findList();
        String s = JSONArray.toJSONString(list);

        return s;
    }

    @RequestMapping("deletefile.do")
    @ResponseBody
    public  String  deletefile(String snickname,String filepath) throws IOException {

        JSONObject jsonObject=new JSONObject();
        System.out.println(filepath+"========");
        Path path=new Path(filepath);
        boolean delete = HdfsConn.getFileSystem().delete(path);
        System.out.println(delete+"======");

         if (delete){
             fs.deleteFile(snickname);
             jsonObject.put("flag","ok");
         }else {

             jsonObject.put("flag","false");
         }




        return jsonObject.toJSONString();
    }

    //  首先通过 nickname 获得原始的 文件的目录
//       将新的目录录入到数据库中

    @RequestMapping("updatefile.do")
    @ResponseBody
    public  String  updateFile(HFile hFile) throws IOException {

       String oldpath= fs.findPath(hFile.getSnickname());

        FileSystem fileSystem = HdfsConn.getFileSystem();
//
        Path op=new Path(oldpath);
        Path np=new Path(hFile.getFilepath());
        boolean rename = fileSystem.rename(op, np);

        JSONObject jsonObject=new JSONObject();

        if(rename){

            fs.uploadFIle(hFile);
            jsonObject.put("flag","ok");
        }else {

            jsonObject.put("flag","failed");
        }

        return  jsonObject.toJSONString();

    }
}
