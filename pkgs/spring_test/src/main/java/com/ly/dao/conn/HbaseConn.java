package com.ly.dao.conn;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import com.ly.init.initDNS;

public class HbaseConn {
    private Configuration configuration = null;
    private Connection connection = null;

    private static class SingletonHolder {
        private static final HbaseConn INSTANCE = new HbaseConn();
    }

    private HbaseConn() {
        try {
            initDNS.init();
            configuration = HBaseConfiguration.create();
            String host = "127.0.0.1";
            configuration.set("hbase.zookeeper.quorum", host);
            configuration.set("hbase.zookeeper.property.clientPort", "2181");
            configuration.set("hbase.rootdir", "hdfs://" + host + ":9000/hbase");

            connection = ConnectionFactory.createConnection(configuration);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static final Connection getConn() {
        return SingletonHolder.INSTANCE.connection;
    }

}
