package main.javaserver.httpmessages.request_executors;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.javaserver.WebServer;
import main.javaserver.confreaders.Htaccess;
import main.javaserver.confreaders.Htpassword;
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
     */
    protected abstract Response serve(Response initializedResponse, Request request, Resource resource, MimeTypes mimeTypes) throws IOException;

    public Response execute(Request request, Resource resource, MimeTypes mimeTypes) throws IOException, NoSuchAlgorithmException, UnsupportedEncodingException {
        Response response = new Response();
        response.addHeaderValue("Date", DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now()));
        response.addHeaderValue("Server", "jimmyh3-java-web-server");

        if ((isAuthRequired(resource) && !hasAuthHeader(request)) || (isAuthRequired(resource) && hasAuthHeader(request) && !hasAuthAccess(request, resource))) {
            System.out.println("Require authorization but authorization headers not found!");
            response = getUnauthorizedResponse(response, resource);
        } else if (doesResourceExist(resource)) {
            System.out.println("Requested resource does not exist!");
        } else {
            response = serve(response, request, resource, mimeTypes);
        }

        return response; 
    }

    private Response getUnauthorizedResponse(Response initializedResponse, Resource resource) {
        Htaccess htaccess = resource.getAccessFile();
        initializedResponse.setCode(401);
        initializedResponse.setReasonPhrase("Unauthorized");
        initializedResponse.addHeaderValue("WWW-Authenticate", String.format("%s realm=\"%s\"", htaccess.getAuthType(), htaccess.getAuthName()));

        return initializedResponse;
    }

    private boolean isAuthRequired(Resource resource) {
        return resource.isProtected();
    }

    private boolean hasAuthHeader(Request request) {
        String authValue = request.getHeader("Authorization");

        return authValue != null;
    }

    private boolean hasAuthAccess(Request request, Resource resource) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String[] authValue = request.getHeader("Authorization").split(" ");
        String authEncoded = authValue[1].trim();
        Htaccess accessFile = WebServer.getHtaccess(resource.getAccessFilePath());
        Htpassword htpassword = accessFile.getUserFile();

        return htpassword.isAuthorized(authEncoded);
    }

    private boolean doesResourceExist(Resource resource) {
        return (!(new File(resource.getAbsolutePath()).exists()));
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