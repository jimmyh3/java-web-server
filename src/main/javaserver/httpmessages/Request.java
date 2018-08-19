package main.javaserver.httpmessages;

import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Request {

    private String uri;
    private String verb;
    private String httpVersion;
    private List<Byte> body;
    private Map<String, String> headers;

    public Request(Socket _clientSocket) {
        init(_clientSocket);
    }

    private void init(Socket _clientSocket) {
        try {
            BufferedReader clientSocketIn = new BufferedReader(new InputStreamReader(_clientSocket.getInputStream()));
            headers = new HashMap<>();
            body = new ArrayList<>();

            getSetStartLine(clientSocketIn);
            getSetHeaders(clientSocketIn);
            getSetBody(_clientSocket.getInputStream());
        } catch (BadRequestException | IOException | ArrayIndexOutOfBoundsException ex) {
            System.err.println("Bad Request: " + ex.getMessage());
            uri = "/";
            verb = "BAD";
            httpVersion = "HTTP/1.1";
        }
    }

    private void getSetStartLine(BufferedReader clientSocketIn) throws BadRequestException, IOException {
        String startLine = clientSocketIn.readLine();
        String[] startLineSplit = startLine.split(" ");

        if (startLineSplit.length != 3)  {
            throw new BadRequestException("Bad Request");
        } else {
            verb = startLineSplit[0];
            uri = startLineSplit[1];
            httpVersion = startLineSplit[2];
        }
    }

    private void getSetHeaders(BufferedReader clientSocketIn) throws IOException {
        String headerLine = "";
        
        while (!(headerLine = clientSocketIn.readLine()).isEmpty()) {
            String[] headerLineSplit = headerLine.split(":");
            String headerName = headerLineSplit[0];
            String headerValue = headerLineSplit[1];

            headers.put(headerName, headerValue);
        }
    }

    private void getSetBody(InputStream clientSocketIn) throws IOException {
        if (headers.containsKey("Content-Length")) {
            int contentLength = Integer.parseInt(headers.get("Content-Length").trim());

            for (int i = 0; i < contentLength; i++) {
                body.add((byte)clientSocketIn.read());
            }
        }
    }

    public String getURI() {
        return uri;
    }

    public String getVerb() {
        return verb;
    }

    public String getHttpVersion() {
        return httpVersion;
    }
}