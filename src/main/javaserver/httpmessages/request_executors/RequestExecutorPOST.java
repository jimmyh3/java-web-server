package main.javaserver.httpmessages.request_executors;

import java.io.IOException;
import java.text.ParseException;

import main.javaserver.confreaders.HttpdConf;
import main.javaserver.confreaders.MimeTypes;
import main.javaserver.httpmessages.Request;
import main.javaserver.httpmessages.Response;
import main.javaserver.httpmessages.Resource;
import main.javaserver.httpmessages.request_executors.RequestExecutor;

public class RequestExecutorPOST extends RequestExecutor {
    
    @Override
    protected Response serve(Response initializedResponse, Request request, Resource resource, MimeTypes mimeTypes) throws IOException, ParseException {
        Response response = initializedResponse;
        response = super.handleScriptExecution(response, request, resource, mimeTypes);
        response.setCode(200);
        response.setReasonPhrase("OK");

        return response;
    }

}