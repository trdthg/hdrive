package com.ly.dao.conn;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;

import com.ly.init.initDNS;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class HdfsConn {
	private FileSystem fileSystem = null;
	private Configuration configuration = null;

	private static class SingletonHolder {
		private static final HdfsConn INSTANCE = new HdfsConn();
	}

	private HdfsConn() {
		try {
			initDNS.init();
			configuration = new Configuration();
			// configuration.set("fs.defaultFS", "hdfs://192.168.150.128:9000/");
			fileSystem = FileSystem.get(new URI("hdfs://127.0.0.1:9000"), configuration, "root");
		} catch (IOException | URISyntaxException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static FileSystem getFileSystem() {
		return SingletonHolder.INSTANCE.fileSystem;
	}

	public static Configuration getConfiguration() {
		return SingletonHolder.INSTANCE.configuration;
	}

	public static void main(String[] args) {
		System.out.println(getFileSystem());
	}
}
