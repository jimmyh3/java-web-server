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
     * @param userIp
     * @param userId
     * @param userName
     * @param requestReceivedTime
     * @param requestVerb
     * @param requestURI
     * @param serverProtocol
     * @param responseCode
     * @param responseBodySize
     */
    public void write(String userIp, String userId, String userName, long requestReceivedTime, 
                                    String requestVerb, String requestURI, String serverProtocol, 
                                                    int responseCode, int responseBodySize) throws IOException {

        if (userIp.isEmpty()) { userIp = "-"; }
        if (userId.isEmpty()) { userId = "-"; }
        if (userName.isEmpty()) { userName = "-"; }
        if (requestVerb.isEmpty()) { requestVerb = "-"; }
        if (requestURI.isEmpty()) { requestURI = "-"; }
        if (serverProtocol.isEmpty()) { serverProtocol = "-"; }

        
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