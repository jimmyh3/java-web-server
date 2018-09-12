package main.javaserver.httpmessages.request_executors;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import main.javaserver.confreaders.HttpdConf;
import main.javaserver.confreaders.MimeTypes;
import main.javaserver.httpmessages.Request;
import main.javaserver.httpmessages.Resource;
import main.javaserver.httpmessages.Response;
import main.javaserver.httpmessages.request_executors.RequestExecutor;

public class RequestExecutorGET extends RequestExecutor {
    
    @Override
    public Response serve(Response initializedResponse, Request request, Resource resource, MimeTypes mimeTypes) throws IOException, ParseException {
        Response response = initializedResponse;

        String requestModSinceStr = request.getHeader("If-Modified-Since");
        if (requestModSinceStr != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMM yyyy hh:mm:ss z");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("PST"));
            Date requestModSinceDate = simpleDateFormat.parse(requestModSinceStr);
            long requestModSinceLong = requestModSinceDate.getTime();
            long resourceModSinceLong = (new File(resource.getAbsolutePath())).lastModified();

            if (requestModSinceLong < resourceModSinceLong) {
                response.setCode(200);
                response.setReasonPhrase("OK");
                response = super.loadResourceContent(response, resource, mimeTypes);
            } else {
                response.setCode(304);
                response.setReasonPhrase("Not Modified");
                response.addHeaderValue("Last-Modified", simpleDateFormat.format(new Date(resourceModSinceLong)));
            }
        } else {
            response.setCode(200);
            response.setReasonPhrase("OK");
            response = super.loadResourceContent(response, resource, mimeTypes);
        }

        return response;
    }

}