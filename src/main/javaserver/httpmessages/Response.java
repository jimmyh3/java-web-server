package main.javaserver.httpmessages;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Response {

	private String httpVersion;
    private int code;
	private String reasonPhrase;
	private Map<String, String> headers;
	private List<Byte> body;
    private Resource resource;

    public Response() {
		httpVersion = "HTTP/1.1";
		code = 500;
		reasonPhrase = "Internal Server Error";
		headers = new HashMap<>();
		body = new ArrayList<>();
	}

	public Response(Resource _resource) {
		this();
		resource = _resource;
	}

	// Getters and setters
	public String getHttpVersion() { return httpVersion; }
	public void setHttpVersion(String httpVersion) { this.httpVersion = httpVersion; }
	public int getCode() { return code; }
	public void setCode(int code) { this.code = code; }
	public String getReasonPhrase() {return reasonPhrase;}
	public void setReasonPhrase(String reasonPhrase) { this.reasonPhrase = reasonPhrase; }
	public Map<String, String> getHeaders() { return headers; }
	public void setHeaders(Map<String, String> headers) { this.headers = headers; }
	public List<Byte> getBody() { return body; }
	public void setBody(List<Byte> body) { this.body = body; }

	public void send(OutputStream clientSocketOut) throws IOException {
		BufferedOutputStream bfo = new BufferedOutputStream(clientSocketOut);
		String startLine = String.format("%s %d %s\n", httpVersion, code, reasonPhrase);
		String headerStr = getResponseHeaderString(headers);

		bfo.write(startLine.getBytes());
		bfo.write(headerStr.getBytes());
		bfo.write("\n".getBytes());
		
		for (Byte bite : body) {
			bfo.write(bite);
		}

		bfo.flush();
	}

	public void addHeaderValue(String header, String value) {
		headers.put(header, value);
	}
	
	private String getResponseHeaderString(Map<String, String> _headers) {
		String headerStr = "";

		for (Map.Entry<String, String> entry : _headers.entrySet()) {
			headerStr += String.format("%s:%s\n", entry.getKey(), entry.getValue());
		}

		return headerStr;
	}
}