package main.javaserver.httpmessages.request_executors;

import java.text.ParseException;

import com.sun.xml.internal.messaging.saaj.packaging.mime.util.QDecoderStream;

import main.javaserver.confreaders.HttpdConf;
import main.javaserver.confreaders.MimeTypes;
import main.javaserver.httpmessages.Request;
import main.javaserver.httpmessages.Response;
import main.javaserver.httpmessages.request_executors.RequestExecutor;

public class RequestExecutorPOST extends RequestExecutor {
    
    
    @Override
    protected Response serve(Response initializedResponse, Request request, Resource resource, MimeTypes mimeTypes) throws IOException {
        // NOTE: POST body is being handled by RequestExecutor as a Script execution call.
        Response response = initializedResponse;
        response.setCode(200);
        response.setReasonPhrase("OK");

        return response;
    }

}