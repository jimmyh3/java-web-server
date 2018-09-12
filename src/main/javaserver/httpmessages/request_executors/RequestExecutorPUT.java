package main.javaserver.httpmessages.request_executors;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.List;

import main.javaserver.confreaders.MimeTypes;
import main.javaserver.httpmessages.Request;
import main.javaserver.httpmessages.Resource;
import main.javaserver.httpmessages.Response;
import main.javaserver.httpmessages.request_executors.RequestExecutor;

public class RequestExecutorPUT extends RequestExecutor {

    private void createWriteRequestFile(Request request, Resource resource) throws IOException {
        File file = new File(resource.getAbsolutePath());
        Path path = file.toPath();
        List<Byte> requestBody = request.getBody();
        byte[] fileData = new byte[requestBody.size()];

        for (int i = 0; i < requestBody.size(); i++) { 
            fileData[i] = requestBody.get(i);
        }

        file.createNewFile();
        Files.write(path, fileData);
    }
    
    @Override
    protected Response serve(Response initializedResponse, Request request, Resource resource, MimeTypes mimeTypes) throws IOException, ParseException {
        Response response = initializedResponse;

        createWriteRequestFile(request, resource);

        response.setCode(201);
        response.setReasonPhrase("Created");
        response.addHeaderValue("Location", request.getURI());
        
        return response;
    }
}