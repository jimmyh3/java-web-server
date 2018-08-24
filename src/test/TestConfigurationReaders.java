package test;

import java.io.FileNotFoundException;
import java.io.IOException;

import main.javaserver.confreaders.Htaccess;
import main.javaserver.confreaders.HttpdConf;
import main.javaserver.confreaders.MimeTypes;

public class TestConfigurationReaders {

    public void testHttpConf(String fileName) throws FileNotFoundException, IOException {
        HttpdConf httpdConf = new HttpdConf(fileName);
        httpdConf.load();

        System.out.println(httpdConf.getConfigValue("Listen")[0]);
        System.out.println(httpdConf.getConfigValue("DocumentRoot")[0]);
        System.out.println(httpdConf.getConfigValue("/ab/")[0]);
    }

    public void testMimeTypes(String fileName) throws FileNotFoundException, IOException {
        MimeTypes mimeTypes = new MimeTypes(fileName);
        mimeTypes.load();

        System.out.println(mimeTypes.getMimeType("jpeg"));
        System.out.println(mimeTypes.getMimeType("m3u"));
        System.out.println(mimeTypes.getMimeType("djvu"));
    }

    public void testHtaccess(String fileName) throws FileNotFoundException, IOException {
        Htaccess htaccess = new Htaccess(fileName);
        htaccess.load();

        System.out.println(htaccess.getAuthName());
        System.out.println(htaccess.getAuthType());
        System.out.println(htaccess.getRequire());
    }
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        TestConfigurationReaders testConfigurationReaders = new TestConfigurationReaders();
        //testConfigurationReaders.testHttpConf("src/main/javaserver/conf/httpd.conf");
        testConfigurationReaders.testMimeTypes("src/main/javaserver/conf/mime.types");
        testConfigurationReaders.testHtaccess("src/main/javaserver/conf/.htaccess");
    }
}