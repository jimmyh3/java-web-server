package main.javaserver.httpmessages.request_executors;

import com.sun.xml.internal.messaging.saaj.packaging.mime.util.QDecoderStream;

import main.javaserver.confreaders.HttpdConf;
import main.javaserver.confreaders.MimeTypes;
import main.javaserver.httpmessages.Request;
import main.javaserver.httpmessages.Response;
import main.javaserver.httpmessages.request_executors.RequestExecutor;

public class RequestExecutorPOST extends RequestExecutor {

    private List<Byte> getScriptContent(Request request, Resource resource, HttpdConf httpdConf, MimeTypes mimeTypes) throws IOException {
        //TODO: run the script and pass the POST request data to it.
        //NOTE: scripts will handle the Response heads + content body as output. (right?)
    }
    
    
    public Response execute(Request request, Resource resource, HttpdConf httpdConf, MimeTypes mimeTypes) throws IOException {
        Response response = super.getInitializedResponse(request, resource, httpdConf, mimetypes);
        response = getScriptContent(request, resource, httpdConf, mimeTypes);

        response.setCode(200);
        response.setReasonPhrase("OK");

        return response;
    }

}