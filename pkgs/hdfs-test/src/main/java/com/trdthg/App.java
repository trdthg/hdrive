package com.trdthg;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Properties;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.BlockLocation;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.apache.hadoop.fs.permission.FsAction;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.google.protobuf.ServiceException;

import io.leopard.javahost.JavaHost;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) throws IOException {
        loadDns();
        try {
            hdfs_basic();
            hdfs_iostream();
            hbase_basic();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void loadDns() throws IOException {
        ClassPathResource hosts = new ClassPathResource("/hosts.properties");
        Properties properties = PropertiesLoaderUtils.loadProperties(hosts);
        JavaHost.updateVirtualDns(properties);
        JavaHost.printAllVirtualDns();
    }

    public static void hbase_basic() throws IOException {
        Configuration conf = HBaseConfiguration.create();
        String host = "127.0.0.1";
        conf.set("hbase.zookeeper.quorum", host);
        conf.set("hbase.zookeeper.property.clientPort", "2181");
        conf.set("hbase.rootdir", "hdfs://" + host + ":9000/hbase");

        Connection connection = ConnectionFactory.createConnection(conf);
        System.out.println(connection);

        // 表管理对象
        Admin admin = connection.getAdmin();

        System.out.println(connection);
        try {
            HBaseAdmin.checkHBaseAvailable(conf);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        System.out.println(connection);

        TableName tmpName = TableName.valueOf("tmppp");
        if (admin.tableExists(tmpName)) {
            admin.disableTable(tmpName);
            admin.deleteTable(tmpName);
        }
        admin.createTable(
                // 创建一个表的描述对象
                new HTableDescriptor(TableName.valueOf("tmppp"))
                        // 创建列族
                        .addFamily(
                                new HColumnDescriptor("info")
                                        // 设置列族中存储数据的最大版数
                                        .setMaxVersions(3))
                        // 创建列族
                        .addFamily(
                                new HColumnDescriptor("info2")
                                        // 设置列族中存储数据的最大版数
                                        .setMaxVersions(3)));

        TableName[] tableNames = admin.listTableNames();
        for (TableName tableName : tableNames) {
            System.out.println(tableName);
            System.out.println(tableName.getNameAsString());
        }

        tmpName = TableName.valueOf("user");
        if (admin.tableExists(tmpName)) {
            admin.disableTable(tmpName);
            admin.deleteTable(tmpName);
        }
        // 创建表
        admin.createTable(
                // 创建一个表的描述对象
                new HTableDescriptor(TableName.valueOf("user"))
                        // 创建列族
                        .addFamily(
                                new HColumnDescriptor("info")
                                        // 设置列族中存储数据的最大版数
                                        .setMaxVersions(3))
                        // 创建列族
                        .addFamily(
                                new HColumnDescriptor("info2")
                                        // 设置列族中存储数据的最大版数
                                        .setMaxVersions(3)));

        // 获取表
        Table user = connection.getTable(TableName.valueOf("user"));
        user.put(
                // 创建 Put, 参数为 row key
                new Put("rk001".getBytes(StandardCharsets.UTF_8))
                        // 添加列族，列名，值
                        .addColumn(
                                "info".getBytes(StandardCharsets.UTF_8),
                                "uname".getBytes(StandardCharsets.UTF_8),
                                "zhangsan".getBytes(StandardCharsets.UTF_8)));
        //
        Get get = new Get("rk001".getBytes(StandardCharsets.UTF_8));
        Result result = user.get(get);
        List<Cell> cells = result.listCells();
        cells.forEach(c -> {
            // 行键 字节数组
            byte[] rowArray = c.getRowArray();
            // 列族名的字节数组
            byte[] familyArray = c.getFamilyArray();
            // 列名的字节数组
            byte[] qualifierArray = c.getQualifierArray();
            // value 的字节数组
            byte[] valueArray = c.getValueArray();

            // System.out.println(String.format("rowkey:%s,family:%s,key:%s,value:%s"),
            // new String(rowArray,a.getRowOffset(),a.getRowLength()),
            // new String(familyArray,a.getFamilyOffset(),a.getFamilyLength()),
            // new String(qualifierArray,a.getQualifierOffset(),a.getQualifierLength()),
            // new String(valueArray,a.getValueOffset(),a.getValueLength())
            // );
            System.out.println(new String(rowArray, c.getRowOffset(), c.getRowLength()));
            System.out.println(new String(familyArray, c.getFamilyOffset(), c.getFamilyLength()));
            System.out.println(new String(qualifierArray, c.getQualifierOffset(), c.getQualifierLength()));
            System.out.println(new String(valueArray, c.getValueOffset(), c.getValueLength()));

        });
        admin.close();

    }

    public static void hdfs_iostream() throws IOException, InterruptedException, URISyntaxException {
        init();
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(new URI("hdfs://127.0.0.1:9000"), conf, "root");
        FileOutputStream fos = new FileOutputStream("./.cache/a_from_iostream.txt");
        FSDataInputStream fis = fs.open(new Path("/test/a.txt"));
        IOUtils.copyBytes(fis, fos, conf);
        IOUtils.closeStream(fis);
        IOUtils.closeStream(fos);
        fs.close();
    }

    public static void init() throws IOException, InterruptedException, URISyntaxException {
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(new URI("hdfs://127.0.0.1:9000"), conf, "root");

        try {
            fs.access(new Path("/test/a.txt"), FsAction.READ);
            fs.delete(new Path("/test"), true);
        } catch (Exception e) {
        }
        fs.mkdirs(new Path("/test"));
        fs.create(new Path("/test/a.txt"));
        fs.close();
    }

    public static void hdfs_basic() throws IOException, InterruptedException, URISyntaxException {
        init();

        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(new URI("hdfs://127.0.0.1:9000"), conf, "root");

        fs.copyFromLocalFile(
                new Path("pom.xml"),
                new Path("/test/pom.xml"));

        fs.copyToLocalFile(
                new Path("/test/a.txt"),
                new Path("./.cache"));

        RemoteIterator<LocatedFileStatus> iter = fs.listFiles(new Path("/"), true);
        while (iter.hasNext()) {
            LocatedFileStatus status = iter.next();
            System.out.println("File: " +
                    status.getPath().getName() + " " +
                    status.getLen() + " " +
                    status.getPermission() + " " +
                    status.getGroup());
            BlockLocation[] bLocations = status.getBlockLocations();
            for (BlockLocation bLocation : bLocations) {
                System.out.println("BlockLocation: " +
                        bLocation.getOffset() + " " +
                        bLocation.getLength() + " ");
                String[] hosts = bLocation.getHosts();
                for (String host : hosts) {
                    System.out.println("Host: " + host);
                }
            }
        }

        fs.close();
    }

}
