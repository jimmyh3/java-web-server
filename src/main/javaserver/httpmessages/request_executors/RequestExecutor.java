package main.javaserver.httpmessages.request_executors;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.javaserver.confreaders.HttpdConf;
import main.javaserver.confreaders.MimeTypes;
import main.javaserver.httpmessages.Request;
import main.javaserver.httpmessages.Resource;
import main.javaserver.httpmessages.Response;

import main.javaserver.httpmessages.request_executors.RequestExecutorGET;
import main.javaserver.httpmessages.request_executors.RequestExecutorPOST;
import main.javaserver.httpmessages.request_executors.RequestExecutorDELETE;
import main.javaserver.httpmessages.request_executors.RequestExecutorHEAD;
import main.javaserver.httpmessages.request_executors.RequestExecutorPUT;

public abstract class RequestExecutor {
    
    /**
     * Executes the given HTTP request and returns a HTTP response.
     * @param request The HTTP request to serve.
     * @param resource The resource desired by the HTTP request.
     * @param mimeTypes The mime.types for determining content type.
     * @return The Response object representating a HTTP response to be sent back to the client.
     * @throws IOException
     * @see Response 
     * @see Request 
     * @see HttpdConf 
     * @see Mimetypes
     */
    protected abstract Response serve(Request request, Resource resource, MimeTypes mimeTypes) throws IOException;

    public Response execute(Request request, Resource resource, MimeTypes mimeTypes) throws IOException {
        Response response = new Response();
        boolean requireAuth = false;        //TODO: implement
        boolean noAuthHeaders = false;      //TODO: implement
        boolean invalidAuth = false;        //TODO: implement

        if (requireAuth && noAuthHeaders) {

        } else if (requireAuth && !noAuthHeaders && invalidAuth) {

        } else {
            response = serve(request, resource, mimeTypes);
        }

        return response; 
    }

    /**
     * Returns a Response object with default values loaded.
     * @param request The HTTP request to serve.
     * @param resource The resource desired by the HTTP request.
     * @param mimeTypes The mime.types for determining content type.
     * @return The Response object representating a HTTP response to be sent back to the client.
     * @see Response 
     * @see Request 
     * @see HttpdConf 
     * @see Mimetypes
     */
    protected Response getInitializedResponse(Request request, Resource resource, MimeTypes mimetypes) {
        Response response = new Response();
        response.addHeaderValue("Date", DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now()));
        response.addHeaderValue("Server", "jimmyh3-java-web-server");

        return response;
    }

    protected Response loadResourceContent(Response response, Resource resource, MimeTypes mimeTypes) throws IOException {
        File reqFile = new File(resource.getAbsolutePath());
        byte[] reqFileData = Files.readAllBytes(reqFile.toPath());
        List<Byte> responseBody = new ArrayList<>();
        for (byte b : reqFileData) { responseBody.add(b); } 

        response.setBody(responseBody);
        response.addHeaderValue("Content-Type", mimeTypes.getMimeType(resource.getAbsolutePath()));
        response.addHeaderValue("Content-Length", Integer.toString(reqFileData.length));

        return response;
    }
}