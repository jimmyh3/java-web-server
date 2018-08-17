package main.javaserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebServer {

    private int port = 8080;
    private int clientId = 0;
    private ServerSocket serverSocket;
    private ExecutorService threadPool;

    public WebServer(int _port) {
        init(_port);
    }

    /**
     * Initializes default values of the web server.
     * @param port port number to have the server socket listen on.
     */
    private void init(int _port) {
        try {
            port = _port;
            serverSocket = new ServerSocket(port);
            threadPool = Executors.newCachedThreadPool();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    /**
     * Begin running the web server. Listen for client requests and handle the responses.
     */
    public void run() {
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                WebClient client = new WebClient(clientId, clientSocket);

                System.out.println("Client connected: " + clientId);

                threadPool.execute(client);
                clientId++;
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            } 
        }
    }
 
    public static void main(String[] args) {
        WebServer webServer = new WebServer(8080);
        webServer.run();
    }
}