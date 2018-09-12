package main.javaserver.httpmessages;

public class BadRequestException extends Exception {

    private static final long serialVersionUID = 1L;

    public BadRequestException(String ex) {
        super(ex);
    }
}