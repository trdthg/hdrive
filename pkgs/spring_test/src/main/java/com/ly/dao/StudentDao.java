package com.ly.dao;

import com.ly.dao.conn.HbaseConn;
import com.ly.po.Student;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Repository
public class StudentDao {


    public Student findStudenBuNameAndpasword(String nickName, String spass) throws IOException {
        Table stu_table = HbaseConn.getConn().getTable(TableName.valueOf("stu_table"));

        Result result = stu_table.get(new Get(nickName.getBytes(StandardCharsets.UTF_8)));


        if (!result.isEmpty()){

            Student student=new Student();
            byte[] snickname = result.getRow();
            byte[] sname1 = result.getValue(Bytes.toBytes("cf1"), Bytes.toBytes("sname"));
            byte[] sgender1 = result.getValue(Bytes.toBytes("cf1"), Bytes.toBytes("sgender"));
            byte[] spass1 = result.getValue(Bytes.toBytes("cf1"), Bytes.toBytes("spass"));



            if(spass.equals(Bytes.toString(spass1))){
                student.setSnickname(Bytes.toString(snickname));
                student.setSname(Bytes.toString(sname1));
                student.setSgender(Bytes.toString(sgender1));
                student.setSpass(Bytes.toString(spass1));
            }else{

                return  null;

            }

//            List<Cell> cells = result.listCells();
//
//
//
//            student.setSnickname(nickName);
//
//            for (Cell a : cells) {
//
//                byte[] qualifierArray = a.getQualifierArray();
//                byte[] valueArray = a.getValueArray();
//
//                String s = new String(qualifierArray,a.getQualifierOffset(),a.getQualifierLength());
//
//                String spwd=new String(valueArray,a.getValueOffset(),a.getValueLength());
//                System.out.println(s);
//
//                if(s.equals("sname")){
//
//                    student.setSname(new String(new String(valueArray,a.getValueOffset(),a.getValueLength())));
//
//                }else if(s.equals("sgender")){
//
//                    student.setSgender(new String(new String(valueArray,a.getValueOffset(),a.getValueLength())));
//
//                }else  if(s.equals("spass")){
//                    System.out.println("spass==="+spass);
//                    System.out.println(new String(valueArray,a.getValueOffset(),a.getValueLength()));
//                    if(spwd.equals(spass)){
//                        System.out.println(new String(valueArray,a.getValueOffset(),a.getValueLength())+"======");
//
//                    student.setSpass(new String(new String(valueArray,a.getValueOffset(),a.getValueLength())));
//                    }else {
//
//                        return null;
//                    }
//                }
//
//
//
//            }


            System.out.println("循环结束=========");
            return  student;

        }

        return null;

    }

    public List<Student> finAllstus() throws IOException {

        List<Student> list=new ArrayList<>();

        Connection conn = HbaseConn.getConn();

        Table stu_table = conn.getTable(TableName.valueOf("stu_table"));

        ResultScanner results = stu_table.getScanner(new Scan());

        for (Result result : results) {
            Student student=new Student();
            byte[] row = result.getRow();// 行键
            byte[] snames = result.getValue(Bytes.toBytes("cf1"), Bytes.toBytes("sname"));
            byte[] sgenders = result.getValue(Bytes.toBytes("cf1"), Bytes.toBytes("sgender"));
            byte[] spasss = result.getValue(Bytes.toBytes("cf1"), Bytes.toBytes("spass"));
            student.setSpass(Bytes.toString(spasss));
            student.setSnickname(Bytes.toString(row));
            student.setSname(Bytes.toString(snames));
            student.setSgender(Bytes.toString(sgenders));
//            System.out.println(Bytes.toString(row)+"row===========");
//            System.out.println(Bytes.toString(snames)+"newname1=========");
//            System.out.println(Bytes.toString(sgenders)+"newname2=========");
//            System.out.println(Bytes.toString(spasss)+"newname=3========");


//            List<Cell> cells = result.listCells();
////            Student  student=new Student();
//            for (Cell a : cells) {
//                student.setSnickname(new String(a.getRowArray(),a.getRowOffset(),a.getRowLength()));
//                byte[] qualifierArray = a.getQualifierArray();
//                byte[] valueArray = a.getValueArray();
//
//                String s = new String(qualifierArray,a.getQualifierOffset(),a.getQualifierLength());
//
//                String spwd=new String(valueArray,a.getValueOffset(),a.getValueLength());
//                System.out.println(s);
////                String s1 = new String
////
////                (new String(valueArray, a.getValueOffset(), a.getValueLength()));
////                String s2 = gbktoUtf8(s1.getBytes("gbk").toString());
////
////                System.out.println(s2);
//
//                String s2=new String(valueArray,a.getValueOffset(),a.getValueLength());
//                String  s3=new String(s2.getBytes("gbk"),"utf-8");
//
//                if(s.equals("sname")){
//
//                        student.setSname(s3);
////                    student.setSname(new String(new String(valueArray,a.getValueOffset(),a.getValueLength()).getBytes("gbk"),"utf-8"));
//
//                }else if(s.equals("sgender")){
//                        student.setSgender(s3);
////                    student.setSgender(new String(new String(valueArray,a.getValueOffset(),a.getValueLength()).getBytes("gbk"),"utf-8"));
//
//                }else  if(s.equals("spass")){
//                        student.setSpass(s3);
////                    student.setSpass(new String(new String(valueArray,a.getValueOffset(),a.getValueLength()).getBytes("gbk"),"utf-8"));
//                }
//
//            }
            list.add(student);


        }


          return  list;
    }

    public boolean deleteByNickName(String snickname) {
        boolean flag=false;
        Connection conn = HbaseConn.getConn();

        Table table = null;
        try {
            table = conn.getTable(TableName.valueOf("stu_table"));
            Delete delete=new Delete(snickname.getBytes(StandardCharsets.UTF_8));
            table.delete(delete);
            flag=true;
        } catch (IOException e) {
           flag=false;
        }


         return  flag;
    }

    public boolean inputStu(Student student) {

        boolean flag=false;

        Connection conn = HbaseConn.getConn();
        Table stu_table = null;

        try {
            stu_table= conn.getTable(TableName.valueOf("stu_table"));

            Put put=new Put(student.getSnickname().getBytes(StandardCharsets.UTF_8));

            put.addColumn(Bytes.toBytes("cf1"),Bytes.toBytes("sname"), Bytes.toBytes(student.getSname()));
            put.addColumn(Bytes.toBytes("cf1"),Bytes.toBytes("sgender"),Bytes.toBytes(student.getSgender()));
            put.addColumn(Bytes.toBytes("cf1"),Bytes.toBytes("spass"),Bytes.toBytes(student.getSpass()));
//            put.add("cf1".getBytes(StandardCharsets.UTF_8),"spass".getBytes(StandardCharsets.UTF_8),student.getSpass().getBytes(StandardCharsets.UTF_8));

            stu_table.put(put);

            flag=true;

            return  flag;
        } catch (IOException e) {

            return  flag;
        }
    }

    public boolean checkNick(String snickname) {
        Table stu_table = null;
        try {
            stu_table = HbaseConn.getConn().getTable(TableName.valueOf("stu_table"));
            Result result = stu_table.get(new Get(snickname.getBytes(StandardCharsets.UTF_8)));

            return  result.isEmpty();
        } catch (IOException e) {

            return  false;
        }




    }


    public  String gbktoUtf8(String s) throws UnsupportedEncodingException {
        char[] c = s.toCharArray();
        byte[] fullByte = new byte[3*c.length];
        for (int i=0; i<c.length; i++) {
            String binary = Integer.toBinaryString(c[i]);
            StringBuffer sb = new StringBuffer();
            int len = 16 - binary.length();
            //前面补零
            for(int j=0; j<len; j++){
                sb.append("0");
            }
            sb.append(binary);
            //增加位，达到到24位3个字节
            sb.insert(0, "1110");
            sb.insert(8, "10");
            sb.insert(16, "10");
            fullByte[i*3] = Integer.valueOf(sb.substring(0, 8), 2).byteValue();//二进制字符串创建整型
            fullByte[i*3+1] = Integer.valueOf(sb.substring(8, 16), 2).byteValue();
            fullByte[i*3+2] = Integer.valueOf(sb.substring(16, 24), 2).byteValue();
        }

         return  new String(fullByte,"utf-8");

    }

    public List<Student> likeName(String nickname) throws IOException {

        List<Student> list=new ArrayList<>();

        Connection conn = HbaseConn.getConn();

        Table stu_table = conn.getTable(TableName.valueOf("stu_table"));

        Scan  scan=new Scan();

        RowFilter rowFilter=new RowFilter(CompareFilter.CompareOp.EQUAL,new RegexStringComparator(".*"+nickname+".*"));
        scan.setFilter(rowFilter);

        ResultScanner results = stu_table.getScanner(scan);

        for (Result result : results) {
            Student student = new Student();
            byte[] row = result.getRow();// 行键
            byte[] snames = result.getValue(Bytes.toBytes("cf1"), Bytes.toBytes("sname"));
            byte[] sgenders = result.getValue(Bytes.toBytes("cf1"), Bytes.toBytes("sgender"));
            byte[] spasss = result.getValue(Bytes.toBytes("cf1"), Bytes.toBytes("spass"));
            student.setSpass(Bytes.toString(spasss));
            student.setSnickname(Bytes.toString(row));
            student.setSname(Bytes.toString(snames));
            student.setSgender(Bytes.toString(sgenders));
            list.add(student);
        }

        return list;
    }
}
