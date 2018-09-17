package main.javaserver;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import main.javaserver.confreaders.Htaccess;
import main.javaserver.confreaders.HttpdConf;
import main.javaserver.confreaders.MimeTypes;

public class WebServer {

    public static String serverProtocol = "HTTP/1.1";
    private int port = 8080;
    private int clientId = 0;
    private ServerSocket serverSocket;
    private ExecutorService threadPool;
    private HttpdConf httpdConf;
    private MimeTypes mimeTypes;
    private static Map<String, Htaccess> accessFiles = new HashMap<>();

    /**
     * Retrieve the Htaccess based on the given filepath(name included).
     * @param accessFile The Htaccess filepath to retrieve.
     * @return The Htaccess file or null if it does not exist in the internal hash map.
     * @see Htaccess
     */
    public static Htaccess getHtaccess(String accessFile) {
        return accessFiles.get(accessFile);
    }

    public static void addInitializeHtaccess(String accessFile) throws FileNotFoundException, IOException {
        if (accessFiles.get(accessFile) != null) return;
        
        Htaccess htaccess = new Htaccess(accessFile);
        htaccess.load();

        accessFiles.put(accessFile, htaccess);
    }

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
            httpdConf = new HttpdConf("src/main/javaserver/conf/httpd.conf");
            mimeTypes = new MimeTypes("src/main/javaserver/conf/mime.types");

            httpdConf.load();
            mimeTypes.load();
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
                WebClient client = new WebClient(clientId, clientSocket, httpdConf, mimeTypes);

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