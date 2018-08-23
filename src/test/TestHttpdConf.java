package test;

import java.io.FileNotFoundException;
import java.io.IOException;

import main.javaserver.confreaders.HttpdConf;

public class TestHttpdConf {
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        System.out.println(System.getProperty("user.dir") + "\\src\\main\\javaserver\\conf\\httpd.conf");

        HttpdConf httpdConf = new HttpdConf("src/main/javaserver/conf/httpd.conf");
        httpdConf.load();

        System.out.println(httpdConf.getConfigValue("Listen")[0]);
        System.out.println(httpdConf.getConfigValue("DocumentRoot")[0]);
        System.out.println(httpdConf.getConfigValue("/ab/")[0]);

    }
}