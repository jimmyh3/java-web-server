package main.javaserver.httpmessages;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
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

    public static Response getResponse(Request request, Resource resource, HttpdConf httpdConf, MimeTypes mimeTypes) throws IOException, NoSuchAlgorithmException, UnsupportedEncodingException, ParseException {
        Response response = new Response();

        RequestExecutor requestExecutor = requestExecutors.get(request.getVerb());
        if (requestExecutor != null) {
            response = requestExecutor.execute(request, resource, mimeTypes);
        } else {
            response.setCode(501);
            response.setReasonPhrase("Not Implemented");
        }
        
        return response;
    }

}