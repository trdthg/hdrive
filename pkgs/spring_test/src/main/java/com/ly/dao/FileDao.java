package com.ly.dao;

import com.ly.dao.conn.HbaseConn;
import com.ly.po.HFile;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FileDao {


//      文件上传 录入到hbase 中
       public  boolean  uploadFile(HFile hFile)  {

           boolean flag=false;
           Table stu_table = null;
           try {
               stu_table = HbaseConn.getConn().getTable(TableName.valueOf("stu_table"));
               Put put=new Put(hFile.getSnickname().getBytes(StandardCharsets.UTF_8));

               put.addColumn(Bytes.toBytes("cf1"),Bytes.toBytes("filepath"),Bytes.toBytes(hFile.getFilepath()));

               stu_table.put(put);

               flag=true;
           } catch (IOException e) {
               e.printStackTrace();
           }

           return  flag;
       }

    public List<HFile> ListFiles() throws IOException {

          List<HFile> list=new ArrayList<>();
        Table stu_table = HbaseConn.getConn().getTable(TableName.valueOf("stu_table"));

        Scan  scan=new Scan();

        ResultScanner results = stu_table.getScanner(scan);

        for (Result rs :results) {
            HFile hFile=new HFile();
            byte[] row = rs.getRow();
            byte[] value = rs.getValue(Bytes.toBytes("cf1"), Bytes.toBytes("filepath"));
             if(value!=null){
                 hFile.setSnickname(Bytes.toString(row));
                 hFile.setFilepath(Bytes.toString(value));

                 list.add(hFile);
             }

        }

         return  list;
    }

    public boolean delete(String nickname) {
        boolean flag=false;
        Table table = null;
        try {
            table = HbaseConn.getConn().getTable(TableName.valueOf("stu_table"));
            Delete delete=new Delete(nickname.getBytes(StandardCharsets.UTF_8));
            delete.addColumn(Bytes.toBytes("cf1"),Bytes.toBytes("filepath"));
            table.delete(delete);
            flag=true;
        } catch (IOException e) {
            e.printStackTrace();
        }

            return  flag;
    }

    public String findPath(String snickname) {
        String s=null;

        Table stu_table = null;
        try {
            stu_table = HbaseConn.getConn().getTable(TableName.valueOf("stu_table"));
            Get get=new Get(snickname.getBytes(StandardCharsets.UTF_8));


            Result result = stu_table.get(get);


            byte[] value = result.getValue(Bytes.toBytes("cf1"), Bytes.toBytes("filepath"));

             s= Bytes.toString(value);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return s;


    }
//

}
