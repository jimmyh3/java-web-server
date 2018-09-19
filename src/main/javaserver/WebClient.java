package main.javaserver;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import main.javaserver.Logs;
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
    private boolean isRunning;
    private Logs logs;

    public WebClient(int _id, Socket _clientSocket, HttpdConf _httpdConf, MimeTypes _mimeTypes) throws IOException {
        id = _id;
        clientSocket = _clientSocket;
        httpdConf = _httpdConf;
        mimeTypes = _mimeTypes;
        isRunning = true;
        logs = new Logs(httpdConf.getLogFile());
    }
 
    /**
     * Get the ID associated with this web client.
     * @return the id.
     */
    public int getId() {
        return id;
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                Request request = new Request(clientSocket);
                Resource resource = new Resource(request.getURI(), httpdConf);
                Response response = ResponseFactory.getResponse(request, resource, httpdConf, mimeTypes);
                
                response.send(clientSocket.getOutputStream());
                logs.write(clientSocket.getRemoteSocketAddress().toString(), "", request.getUserName(), request.getReceivedTime(),
                                                        request.getVerb(), request.getURI(), request.getHttpVersion(), response.getCode(), response.getBody().length);
            } catch (FileNotFoundException ex) {
                System.err.println(ex.getMessage());    
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            } catch (NoSuchAlgorithmException ex) {
                System.err.println(ex.getMessage());
            } catch (ParseException ex) {
                System.err.println(ex.getMessage());
            } 
        }
    }

}