package main.javaserver.httpmessages.request_executors;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import main.javaserver.confreaders.MimeTypes;
import main.javaserver.httpmessages.Request;
import main.javaserver.httpmessages.Resource;
import main.javaserver.httpmessages.Response;
import main.javaserver.httpmessages.request_executors.RequestExecutor;

public class RequestExecutorDELETE extends RequestExecutor {
    
    @Override
    protected Response serve(Response initializedResponse, Request request, Resource resource, MimeTypes mimeTypes) throws IOException, ParseException {
        Response response = initializedResponse;
        File file = new File(resource.getAbsolutePath());
        
        if (file.delete()) {
            response.setCode(204);
            response.setReasonPhrase("No Content");
        }

        return response;
    }
}