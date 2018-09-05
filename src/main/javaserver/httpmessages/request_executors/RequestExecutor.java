package main.javaserver.httpmessages.request_executors;

import java.util.HashMap;
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

public class RequestExecutor {
    
    public Response getResponse(Request request, Resource resource, HttpdConf httpdConf, MimeTypes mimeTypes) {
        Response response = new Response();
        //TODO: implement this to have the mandatory header + values in responses.
        return response;
    }
}