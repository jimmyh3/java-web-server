package main.javaserver.httpmessages.request_executors;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.ParseException;

import main.javaserver.confreaders.HttpdConf;
import main.javaserver.confreaders.MimeTypes;
import main.javaserver.httpmessages.Request;
import main.javaserver.httpmessages.Resource;
import main.javaserver.httpmessages.Response;
import main.javaserver.httpmessages.request_executors.RequestExecutor;

public class RequestExecutorHEAD extends RequestExecutor {
    
    private Response getResourceHeaders(Response response, Request request, Resource resource, MimeTypes mimeTypes) throws IOException, ParseException {
        File reqFile = new File(resource.getAbsolutePath());
        byte[] reqFileData = Files.readAllBytes(reqFile.toPath());

        response.addHeaderValue("Content-Type", mimeTypes.getMimeType(resource.getAbsolutePath()));
        response.addHeaderValue("Content-Length", Integer.toString(reqFileData.length));

        return response;
    }

    @Override
    public Response serve(Response initializedResponse, Request request, Resource resource, MimeTypes mimeTypes) throws IOException, ParseException {
        Response response = initializedResponse;
        response = getResourceHeaders(response, request, resource, mimeTypes);
        
        response.setCode(200);
        response.setReasonPhrase("OK");

        return response;
    }
    
}