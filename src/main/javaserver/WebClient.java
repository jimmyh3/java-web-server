package main.javaserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class WebClient implements Runnable {

    private int id;
    private Socket clientSocket;
    private PrintWriter clientSocketOut;    // output to client via output stream.
    private BufferedReader clientSocketIn;  // get client input via input stream.

    public WebClient(int _id, Socket _clientSocket) {
        init(_id, _clientSocket);
    }

    private void init(int _id, Socket _clientSocket) {
        try {
            id = _id;
            clientSocket = _clientSocket;
            clientSocketOut = new PrintWriter(clientSocket.getOutputStream());
            clientSocketIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    @Override
    public void run() {
        boolean clientIsActive = true;

        while (clientIsActive) {
            try {
                String inputLine = "";

                while ((inputLine = clientSocketIn.readLine()) != null) {
                    System.out.println(inputLine);
                }
            } catch (IOException ex) {
                // TODO: failure here indicates bad request from client.
            }
        }
    }
}