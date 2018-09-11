package main.javaserver.httpmessages;

import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import main.javaserver.httpmessages.BadRequestException;

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
            DataInputStream clientSocketIn = new DataInputStream(new BufferedInputStream(_clientSocket.getInputStream()));
            headers = new HashMap<>();
            body = new ArrayList<>();

            getSetStartLine(clientSocketIn);
            getSetHeaders(clientSocketIn);
            getSetBody(clientSocketIn);
        } catch (BadRequestException | IOException | ArrayIndexOutOfBoundsException ex) {
            System.err.println("Bad Request: " + ex.getMessage());
            uri = "/";
            verb = "BAD";
            httpVersion = "HTTP/1.1";
        }
    }

    private void getSetStartLine(DataInputStream clientSocketIn) throws BadRequestException, IOException {
        String startLine = null;

        do {
            startLine = clientSocketIn.readLine();
        } while (startLine == null || startLine.isEmpty());

        getSetStartLine(startLine);
    }

    private void getSetStartLine(String startLine) throws BadRequestException, IOException {
        String[] startLineSplit = startLine.split(" ");

        if (startLineSplit.length != 3)  {
            throw new BadRequestException("Bad Request");
        } else {
            verb = startLineSplit[0];
            uri = startLineSplit[1];
            httpVersion = startLineSplit[2];
        }
    }

    private void getSetHeaders(DataInputStream clientSocketIn) throws IOException {
        String headerLine = "";
        
        while (!(headerLine = clientSocketIn.readLine()).isEmpty()) {
            String[] headerLineSplit = headerLine.split(":");
            String headerName = headerLineSplit[0].trim();
            String headerValue = headerLineSplit[1].trim();

            headers.put(headerName, headerValue);
        }
    }

    private void getSetBody(DataInputStream clientSocketIn) throws IOException {
        if (headers.containsKey("Content-Length")) {
            int contentLength = Integer.parseInt(headers.get("Content-Length").trim());
            byte[] bodyBytes = new byte[contentLength];

            clientSocketIn.read(bodyBytes);

            for (byte b : bodyBytes) {
                body.add(b);
            }
        }
    }

    public String getHeader(String fieldName) {
        return headers.get(fieldName);
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

    public List<Byte> getBody() {
        return body;
    }

}