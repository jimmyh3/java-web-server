package main.javaserver;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Map;

import main.javaserver.confreaders.HttpdConf;
import main.javaserver.confreaders.MimeTypes;
import main.javaserver.httpmessages.Request;
import main.javaserver.httpmessages.Resource;
import main.javaserver.httpmessages.Response;
import main.javaserver.httpmessages.ResponseFactory;

public class WebClient implements Runnable {

    private int id;
    private Socket clientSocket;
    private HttpdConf httpdConf;
    private MimeTypes mimeTypes;

    public WebClient(int _id, Socket _clientSocket, HttpdConf _httpdConf, MimeTypes _mimeTypes) {
        id = _id;
        clientSocket = _clientSocket;
        httpdConf = _httpdConf;
        mimeTypes = _mimeTypes;
    }

    @Override
    public void run() {
        Request request = new Request(clientSocket);
        Response response = testGetResponse();
        
        try {
            response.send(clientSocket.getOutputStream());
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }


    private Response testGetResponse() {
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

}