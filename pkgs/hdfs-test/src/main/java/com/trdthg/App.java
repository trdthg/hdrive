package com.trdthg;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.BlockLocation;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.apache.hadoop.io.IOUtils;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        try {
            hdfs_iostream();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hdfs_iostream() throws IOException, InterruptedException, URISyntaxException {
        init();
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(new URI("hdfs://127.0.0.1:9000"), conf, "root");
        FileOutputStream fos = new FileOutputStream("./.cache/a_from_iostream.txt");
        FSDataInputStream fis = fs.open(new Path("/test/a.txt"));
        IOUtils.copyBytes(fis, fos, conf);
        IOUtils.closeStream(fis);
    }

    public static void init() throws IOException, InterruptedException, URISyntaxException {
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(new URI("hdfs://127.0.0.1:9000"), conf, "root");
        fs.delete(new Path("/test"), true);
        fs.mkdirs(new Path("/test"));
        fs.create(new Path("/test/a.txt"));
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
