package test;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.lang.ProcessBuilder;
import java.lang.Process;

import main.javaserver.Logs;
import main.javaserver.confreaders.HttpdConf;
import main.javaserver.httpmessages.Resource;
import main.javaserver.httpmessages.Response;
import main.javaserver.httpmessages.request_executors.RequestExecutor;
import main.javaserver.httpmessages.request_executors.RequestExecutorGET;

public class TestGeneral {

    public void testLogs(String outputFilePath) throws IOException {
        Logs logs = new Logs(outputFilePath);
        logs.write("128.0.0.1", "", "jimmyh3", System.currentTimeMillis(), "GET", "/public_html/index.html", "HTTP/1.1", 200, 0);
    }

    public void testProcessBuilder() throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("perl", "C:/Users/jimmy/Documents/Programming Development/java-web-server/src/public_html/cgi-bin/perl_env");
        Process process = processBuilder.start();

        DataInputStream processStdout = new DataInputStream(new BufferedInputStream(process.getInputStream()));
        byte[] content = processStdout.readAllBytes();
        
        System.out.println(content.length);
        System.out.println(new String(content));
    }

    public void testSimpleDateFormat(String time) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMM yyyy hh:mm:ss z");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("PST"));

        Date date = simpleDateFormat.parse(time);
        long dateLong = date.getTime();

        System.out.println("Given: " + time);
        System.out.println("To Long: " + dateLong);
        System.out.println("Back to String: " + simpleDateFormat.format(date));

    }
    
    public void testCreateFile() throws IOException {
        File file = new File("C:/Users/jimmy//Desktop/test.txt");
        Path path = file.toPath();
        String text = "Hello world! You";
        byte[] fileData = text.getBytes();

        file.createNewFile();

        Files.write(path, fileData);
    }
    
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
    

    public static void main(String[] args) throws FileNotFoundException, IOException, ParseException {
        
        TestGeneral testGeneral = new TestGeneral();
        /*
        HttpdConf httpdConf = new HttpdConf("src/main/javaserver/conf/httpd.conf");
        httpdConf.load();

        testGeneral.testResource("/", httpdConf);
        testGeneral.testResource("/cgi-bin/", httpdConf);
        */
        //System.out.println(RequestExecutor.requestExecutors.get("GET").toString());
        //System.out.println("Testing Date Format: " + DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now()));
        //testGeneral.testCreateFile();
        //testGeneral.testProcessBuilder();
        testGeneral.testLogs("C:/Users/jimmy/Documents/Programming Development/java-web-server/src/main/javaserver/logs/logs.txt");
    }

}