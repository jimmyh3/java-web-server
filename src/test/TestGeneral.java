package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import main.javaserver.confreaders.HttpdConf;
import main.javaserver.httpmessages.Resource;

public class TestGeneral {
    
    public void testResource(String uri, HttpdConf httpdConf) throws FileNotFoundException, IOException {
        Resource resource = new Resource(uri, httpdConf);
        System.out.println("testResource testing for URI: " + uri + " Real path: " + resource.getAbsolutePath());
        System.out.println("testResource testing for isScript: " + resource.isScript());
        System.out.println("testResource testing for isProtected: " + resource.isProtected());
    }

    public Response testGetResponse() {
        Response response = new Response();

        Map<String, String> headers = response.getHeaders();
        List<Byte> body = response.getBody();
        String htmlBody = "<html> <header><title>This is title</title></header><body> Hello world </body></html>";
        byte[] htmlBodyBytes = htmlBody.getBytes();
        int htmlBodySize = htmlBodyBytes.length;

        response.setHttpVersion("HTTP/1.1");
        response.setCode(200);
        response.setReasonPhrase("OK");
        headers.put("Date", "Mon, 27 Jul 2009 12:28:53 GMT");
        headers.put("Server", "Apache/2.2.14 (Win32)");
        headers.put("Connection", "Closed");
        headers.put("Content-Length", Integer.toString(htmlBodySize));
        headers.put("Content-Type", "Closed");

        for (int i = 0; i < htmlBodySize; i++) {
            body.add(htmlBodyBytes[i]);
        }

        return response;
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
        TestGeneral testGeneral = new TestGeneral();

        HttpdConf httpdConf = new HttpdConf("src/main/javaserver/conf/httpd.conf");
        httpdConf.load();

        testGeneral.testResource("/", httpdConf);
        testGeneral.testResource("/cgi-bin/", httpdConf);
        System.out.println("Testing Date Format: " + DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now()));
    }

}