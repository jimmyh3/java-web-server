package main.javaserver.httpmessages.request_executors;

import main.javaserver.confreaders.MimeTypes;
import main.javaserver.httpmessages.Request;
import main.javaserver.httpmessages.Resource;
import main.javaserver.httpmessages.Response;
import main.javaserver.httpmessages.request_executors.RequestExecutor;

public class RequestExecutorPUT extends RequestExecutor {
    
    @Override
    protected Response serve(Response initializedResponse, Request request, Resource resource, MimeTypes mimeTypes) throws IOException {
        Response response = initializedResponse;

        

        return response;
    }
}