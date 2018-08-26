package test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.javaserver.confreaders.Htaccess;
import main.javaserver.confreaders.Htpassword;
import main.javaserver.confreaders.HttpdConf;
import main.javaserver.confreaders.MimeTypes;

public class TestConfigurationReaders {

    public void testHttpConf(String fileName) throws FileNotFoundException, IOException {
        HttpdConf httpdConf = new HttpdConf(fileName);
        httpdConf.load();

        System.out.println("testHttpConf.getConfigValue('Listen')  = " + httpdConf.getConfigValue("Listen")[0]);
        System.out.println("testHttpConf.getConfigValue('DocumentRoot')  = " + httpdConf.getConfigValue("DocumentRoot")[0]);
        System.out.println("testHttpConf.getConfigValue('/ab/')  = " + httpdConf.getConfigValue("/ab/")[0]);
    }

    public void testMimeTypes(String fileName) throws FileNotFoundException, IOException {
        MimeTypes mimeTypes = new MimeTypes(fileName);
        mimeTypes.load();

        System.out.println("testMimeTypes.getMimeType(jpeg) " + mimeTypes.getMimeType("jpeg"));
        System.out.println("testMimeTypes.getMimeType(m3u) " + mimeTypes.getMimeType("m3u"));
        System.out.println("testMimeTypes.getMimeType(djvu) " + mimeTypes.getMimeType("djvu"));
    }

    public void testHtaccess(String fileName) throws FileNotFoundException, IOException {
        Htaccess htaccess = new Htaccess(fileName);
        htaccess.load();

        System.out.println("testHtaccess.getAuthName(): " + htaccess.getAuthName());
        System.out.println("testHtaccess.getAuthType(): " + htaccess.getAuthType());
        System.out.println("testHtaccess.getRequire(): " + htaccess.getRequire());
    }

    public void testHtpassword(String fileName) throws FileNotFoundException, IOException, NoSuchAlgorithmException {
        Htpassword htpassword = new Htpassword(fileName);
        htpassword.load();

        System.out.println("testHtpassword.isAuthorized('jimmy', 'password1234'): " + htpassword.isAuthorized("jimmy", "password1234"));
    }
    
    public static void main(String[] args) throws FileNotFoundException, IOException, NoSuchAlgorithmException {
        TestConfigurationReaders testConfigurationReaders = new TestConfigurationReaders();
        System.out.println("TESTING ------------ TestConfigurationReaders ------------ TESTING");
        testConfigurationReaders.testHttpConf("src/main/javaserver/conf/httpd.conf");
        testConfigurationReaders.testMimeTypes("src/main/javaserver/conf/mime.types");
        testConfigurationReaders.testHtaccess("src/main/javaserver/conf/.htaccess");
        testConfigurationReaders.testHtpassword("src/main/javaserver/conf/.htpasswd");
        //String str = "AuthUserFile \"/Users/jrob/workspace/server/public_html/.htpasswd\"";

        System.out.println("END TEST ------------ TestConfigurationReaders ------------ END TEST");
    }
}