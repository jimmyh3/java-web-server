package main.javaserver;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.io.IOException;

public class Logs {
    
    private File outputFile;

    public Logs(String outputFilePath) throws IOException {
        setOutputFile(outputFilePath);
    }

    public void setOutputFile(String outputFilePath) throws IOException {
        File tempFile = new File(outputFilePath);
        tempFile.createNewFile();

        if (tempFile.isFile()) {
            outputFile = tempFile;
        } else {
            System.out.println("Logs file is not a valid path");
        }
    }

    /**
     * Write this log statement in common log format (https://en.wikipedia.org/wiki/Common_Log_Format)
     * An empty string given to any argument is set as a hyphen to indicate missing information. 
     * 
     * @param userIp IP address of the client (remote host) which made the request to the server.
     * @param userId user-identifier is the RFC 1413 identity of the client.
     * @param userName the person requesting the document.
     * @param requestReceivedTime time of request received as a long number. This will be formatted as for example: [10/Oct/2000:13:55:36 -0700].
     * @param requestVerb the HTTP request verb.
     * @param requestURI the HTTP URI requested.
     * @param serverProtocol the server protocol. For example: HTTP/1.1
     * @param responseCode the response code sent back to the client.
     * @param responseBodySize the response content length if any.
     */
    public void write(String userIp, String userId, String userName, long requestReceivedTime, 
                                    String requestVerb, String requestURI, String serverProtocol, 
                                                    int responseCode, int responseBodySize) throws IOException {

        if (userIp == null || userIp.isEmpty()) { userIp = "-"; }
        if (userId == null || userId.isEmpty()) { userId = "-"; }
        if (userName == null || userName.isEmpty()) { userName = "-"; }
        if (requestVerb == null || requestVerb.isEmpty()) { requestVerb = "-"; }
        if (requestURI == null || requestURI.isEmpty()) { requestURI = "-"; }
        if (serverProtocol == null || serverProtocol.isEmpty()) { serverProtocol = "-"; }

        
        PrintWriter outputFileStream = null;

        try {
            outputFileStream = new PrintWriter(new BufferedOutputStream(new FileOutputStream(outputFile, true)));
            outputFileStream.println(String.format("%s %s %s [%s] \"%s %s %s\" %d %d", 
                                                    userIp, userId, userName, getTimeAsDateFormat(requestReceivedTime),
                                                                            requestVerb, requestURI, serverProtocol, responseCode, responseBodySize));
            outputFileStream.flush();
        } catch (IOException ex) {
            System.err.println(outputFile.getPath() + " is not a file!");
        } finally {
            if (outputFileStream != null) {
                outputFileStream.close();
            }
        }
    }

    private String getTimeAsDateFormat(long requestReceivedTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMM yyyy hh:mm:ss z");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("PST"));
        Date date = new Date(requestReceivedTime);

        return simpleDateFormat.format(date);
    }

}