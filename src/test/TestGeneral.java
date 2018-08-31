package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import main.javaserver.confreaders.HttpdConf;
import main.javaserver.httpmessages.Resource;

public class TestGeneral {
    
    public void testResource(String uri, HttpdConf httpdConf) throws FileNotFoundException, IOException {
        Resource resource = new Resource(uri, httpdConf);
        System.out.println("testResource testing for URI: " + uri + " Real path: " + resource.getAbsolutePath());
        System.out.println("testResource testing for isScript: " + resource.isScript());
        System.out.println("testResource testing for isProtected: " + resource.isProtected());
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
        TestGeneral testGeneral = new TestGeneral();

        HttpdConf httpdConf = new HttpdConf("src/main/javaserver/conf/httpd.conf");
        httpdConf.load();

        testGeneral.testResource("/", httpdConf);
        testGeneral.testResource("/cgi-bin/", httpdConf);
    }

}