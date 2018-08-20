package main.javaserver.httpmessages;

import java.io.OutputStream;

public class Response {

    private int code;
    private String reasonPhrase;
    //private Resource resource;

    public Response() {

    }

    /*
    public Response (Resource _resource) {
        
    }
    */
    
    public void send(OutputStream clientSocketOut) {

    }
}