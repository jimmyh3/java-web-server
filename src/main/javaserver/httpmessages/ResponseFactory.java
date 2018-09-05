package main.javaserver.httpmessages;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.javaserver.WebServer;
import main.javaserver.confreaders.Htaccess;
import main.javaserver.confreaders.Htpassword;
import main.javaserver.confreaders.MimeTypes;
import main.javaserver.confreaders.HttpdConf;
import main.javaserver.httpmessages.Request;
import main.javaserver.httpmessages.Resource;
import main.javaserver.httpmessages.Response;
import main.javaserver.httpmessages.request_executors.RequestExecutor;
import main.javaserver.httpmessages.request_executors.RequestExecutorGET;
import main.javaserver.httpmessages.request_executors.RequestExecutorPOST;
import main.javaserver.httpmessages.request_executors.RequestExecutorPUT;
import main.javaserver.httpmessages.request_executors.RequestExecutorDELETE;
import main.javaserver.httpmessages.request_executors.RequestExecutorHEAD;

public class ResponseFactory {

    public static Map<String, RequestExecutor> requestExecutors = new HashMap<>();
    
    static {
        requestExecutors.put("GET", new RequestExecutorGET());
        requestExecutors.put("POST", new RequestExecutorPOST());
        requestExecutors.put("HEAD", new RequestExecutorHEAD());
        requestExecutors.put("PUT", new RequestExecutorPUT());
        requestExecutors.put("DELETE", new RequestExecutorDELETE());
    }

    private ResponseFactory() {}

    public static Response getResponse(Request request, Resource resource, HttpdConf httpdConf, MimeTypes mimeTypes) throws IOException {
        // Exception vs boolean; exceptions are for exceptional circumstances.
        Response response = new Response();

        if (requireAuth(resource) && !hasAuthHeader(request)) {
            //TODO: Create and return 401 Response.
            System.out.println("Require authorization but has no auth headers!");
        }

        if (requireAuth(resource) && hasAuthHeader(request) && !hasAuthAccess(request, resource)) {
            //TODO: Create and return 403 Response.
            System.out.println("Require authorization and has headers but invalid credentials!");
        }

        if (doesResourceExist(resource)) {
            //TODO: Create and return 404 Response.
            System.out.println("Requested resource does no exist!");
        }
        
        if (resource.isScript()) {
            //TODO: Execute script; return 200 for success or 500 for failure
            System.out.println("Requested resource is a script!");
        } else {
            //TODO: Handle PUT, DELETE, POST, GET, HEAD
            RequestExecutor requestExecutor = requestExecutors.get(request.getVerb());
            if (requestExecutor != null) {
                response = requestExecutor.execute(request, resource, httpdConf, mimeTypes);
            }
        }

        
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