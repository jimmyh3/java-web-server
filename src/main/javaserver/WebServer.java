package main.javaserver;

import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;

public class WebServer {

    private int port = 8080;
    private ServerSocket serverSocket;
    private ExecutorService threadPool;

    public WebServer(int port) {
        this.port = port;
    }
 
    public static void main(String[] args) {
        WebServer webServer = new WebServer(8080);
    }
}