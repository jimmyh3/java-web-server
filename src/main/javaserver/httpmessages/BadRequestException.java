package main.javaserver.httpmessages;

public class BadRequestException extends Exception {

    public BadRequestException(String ex) {
        super(ex);
    }
}