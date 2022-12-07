package com.ly.dao;

import com.ly.po.Employee;
import com.ly.dao.conn.HbaseConn;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Repository
public class EmployeeDao {

    public void addEmp(Employee employee) throws IOException {

        Connection conn = HbaseConn.getConn();

        Table emp_table = conn.getTable(TableName.valueOf("emp_table"));
        Put put = new Put(employee.getNickname().getBytes(StandardCharsets.UTF_8));

        put.addColumn("info".getBytes(StandardCharsets.UTF_8), "age".getBytes(StandardCharsets.UTF_8),
                String.valueOf(employee.getEage()).getBytes(StandardCharsets.UTF_8));
        put.addColumn("info".getBytes(StandardCharsets.UTF_8), "name".getBytes(StandardCharsets.UTF_8),
                employee.getEname().getBytes(StandardCharsets.UTF_8));
        put.addColumn("info".getBytes(StandardCharsets.UTF_8), "pass".getBytes(StandardCharsets.UTF_8),
                employee.getEpass().getBytes(StandardCharsets.UTF_8));

        emp_table.put(put);

    }

    public boolean checkRowKey(String rowkey) throws IOException {

        Connection conn = HbaseConn.getConn();

        Table emp_table = conn.getTable(TableName.valueOf("emp_table"));

        Result result = emp_table.get(new Get(rowkey.getBytes(StandardCharsets.UTF_8)));

        return result.isEmpty();

    }

    public Result getEmp(String nickName, String pass) throws IOException {

        Connection conn = HbaseConn.getConn();
        Table emp_table = conn.getTable(TableName.valueOf("emp_table"));

        Result result = emp_table.get(new Get(nickName.getBytes(StandardCharsets.UTF_8)));

        if (!result.isEmpty()) {
            List<Cell> cells = result.listCells();
            for (Cell a : cells) {

                byte[] qualifierArray = a.getQualifierArray();
                byte[] valueArray = a.getValueArray();
                String epass = new String(new String(valueArray, a.getValueOffset(), a.getValueLength()));

                if (epass.equals(pass)) {
                    return result;
                }

            }

        }

        return null;
    }

}
