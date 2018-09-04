package main.javaserver.httpmessages;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import main.javaserver.WebServer;
import main.javaserver.confreaders.Htaccess;
import main.javaserver.confreaders.Htpassword;
import main.javaserver.confreaders.MimeTypes;
import main.javaserver.httpmessages.Request;
import main.javaserver.httpmessages.Resource;
import main.javaserver.httpmessages.Response;

public class ResponseFactory {

    private ResponseFactory() {}

    public static Response getResponse(Request request, Resource resource, MimeTypes mimeTypes) throws IOException {
        // Exception vs boolean; exceptions are for exceptional circumstances.
        Response response = new Response();

        if (requireAuth(resource) && !hasAuthHeader(request)) {
            //TODO: Create and return 401 Response.
        }

        if (requireAuth(resource) && hasAuthHeader(request) && !hasAuthAccess(request, resource)) {
            //TODO: Create and return 403 Response.
        }

        if (doesResourceExist(resource)) {
            //TODO: Create and return 404 Response.
        }
        
        if (resource.isScript()) {
            //TODO: Execute script; return 200 for success or 500 for failure
        } else {
            //TODO: Handle PUT, DELETE, POST, GET, HEAD
            if (request.getVerb().equals("GET")) {
                response = requestGetExecute(request, resource, mimeTypes);
            }
        }

        
        return response;
    }

    private static Response requestExecute(Request request, Resource resource) throws IOException {
        Response response = new Response();
        response.addHeaderValue("Date", DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now()));
        response.addHeaderValue("Server", "jimmyh3-java-web-server");

        return response;
    }

    private static Response requestGetExecute(Request request, Resource resource, MimeTypes mimeTypes) throws IOException {
        Response response = requestExecute(request, resource);
        File reqFile = new File(resource.getAbsolutePath());
        byte[] reqFileData = Files.readAllBytes(reqFile.toPath());
        List<Byte> responseBody = new ArrayList<>();
        for (byte b : reqFileData) { responseBody.add(b); } 

        response.setBody(responseBody);
        response.setCode(200);
        response.setReasonPhrase("OK");
        response.addHeaderValue("Content-Type", mimeTypes.getMimeType(resource.getAbsolutePath()));
        response.addHeaderValue("Content-Length", Integer.toString(reqFileData.length));

        return response;
    }

    private static boolean requireAuth(Resource resource) {
        return resource.isProtected();
    }

    private static boolean hasAuthHeader(Request request) {
        String authValue = request.getHeader("Authorization");

        return authValue != null;
    }

    private static boolean hasAuthAccess(Request request, Resource resource) {
        String[] authValue = request.getHeader("Authorization").split(" ");
        String authType = authValue[0].trim();
        String authEncoded = authValue[1].trim();
        Htaccess accessFile = WebServer.getHtaccess(resource.getAccessFilePath());
        Htpassword htpassword = accessFile.getUserFile();

        return htpassword.isAuthorized(authEncoded);
    }

    private static boolean doesResourceExist(Resource resource) {
        return (!(new File(resource.getAbsolutePath()).exists()));
    }

}