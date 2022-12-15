package com.ly.init;

import java.io.IOException;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import io.leopard.javahost.JavaHost;

public class initDNS {
  public static void init() {
    try {
      initDNS.loadDns();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void loadDns() throws IOException {
    ClassPathResource hosts = new ClassPathResource("/hosts.properties");
    Properties properties = PropertiesLoaderUtils.loadProperties(hosts);
    JavaHost.updateVirtualDns(properties);
    JavaHost.printAllVirtualDns();
  }

}
