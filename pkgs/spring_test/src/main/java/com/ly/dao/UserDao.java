package com.ly.dao;

import com.ly.dao.conn.HbaseConn;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

public class UserDao {

    // 向 hbase 中录入 用户信息
    public void addUser(Integer cid, String cname) throws IOException {

        UUID uuid = UUID.randomUUID();
        Table table = HbaseConn.getConn().getTable(TableName.valueOf("user"));

        // table.incrementColumnValue();

        // table.get
        Put put = new Put(String.valueOf(UUID.randomUUID()).getBytes(StandardCharsets.UTF_8));
        put.addColumn("info".getBytes(StandardCharsets.UTF_8), "cid".getBytes(StandardCharsets.UTF_8),
                String.valueOf(cid).getBytes(StandardCharsets.UTF_8));
        put.addColumn("info".getBytes(StandardCharsets.UTF_8), "cname".getBytes(StandardCharsets.UTF_8),
                cname.getBytes(StandardCharsets.UTF_8));
        table.put(put);

    }

    // 通过用户名和 id 进行数据查询 需要进行全表查询
    public void findUserBycidAndName(Integer cid, String cname) throws IOException {
        Table user = HbaseConn.getConn().getTable(TableName.valueOf("user"));

        ResultScanner results = user.getScanner(new Scan());

        for (Result result : results) {
            List<Cell> cells = result.listCells();

            for (Cell a : cells) {

                byte[] rowArray = a.getRowArray();
                byte[] familyArray = a.getFamilyArray();
                byte[] qualifierArray = a.getQualifierArray();
                byte[] valueArray = a.getValueArray();

                System.out.println("rowkey:" + new String(rowArray, a.getRowOffset(), a.getRowLength()));
                System.out.println("family:" + new String(familyArray, a.getFamilyOffset(), a.getFamilyLength()));
                System.out.println("列" + new String(qualifierArray, a.getQualifierOffset(), a.getQualifierLength()));
                System.out.println("值" + new String(valueArray, a.getValueOffset(), a.getValueLength()));

                if (cid == Integer.parseInt(new String(valueArray, a.getValueOffset(), a.getValueLength()))) {

                }
            }

        }

    }

}
