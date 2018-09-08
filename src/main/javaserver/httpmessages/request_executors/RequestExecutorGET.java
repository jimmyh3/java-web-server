package main.javaserver.httpmessages.request_executors;

import java.io.IOException;

import main.javaserver.confreaders.HttpdConf;
import main.javaserver.confreaders.MimeTypes;
import main.javaserver.httpmessages.Request;
import main.javaserver.httpmessages.Resource;
import main.javaserver.httpmessages.Response;
import main.javaserver.httpmessages.request_executors.RequestExecutor;

public class RequestExecutorGET extends RequestExecutor {
    
    @Override
    public Response serve(Response initializedResponse, Request request, Resource resource, MimeTypes mimeTypes) throws IOException {
        Response response = initializedResponse;
        response = super.loadResourceContent(response, resource, mimeTypes);

        response.setCode(200);
        response.setReasonPhrase("OK");

        return response;
    }

}