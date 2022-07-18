package testone;

import java.net.URI;
import java.net.URISyntaxException;

enum HTTPMethod {
	GET,
	POST;
}

public class TestSuite {
	
	 int id;
	 String restApiUri;
	 int port;
	 String ip;
	 int requestCount;
	 int threadCount;
	 HTTPMethod method;
	 String requestBody;
	
	 URI uri;

	TestSuite (int id, String restApiUri, int port, String ip, int requestCount, int threadCount, HTTPMethod method, String body){
		this.id = id;
		this.restApiUri = restApiUri;
		this.port = port;
		this.ip = ip;
		this.requestCount = requestCount;
		this.threadCount = threadCount;
		this.method = method;
		this.requestBody = body;
		
		try {
			this.uri = new URI( "https://" + this.ip + ":" + this.port + this.restApiUri);
		} catch (URISyntaxException e) {
			e.printStackTrace();
	 	}
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Test Suite #" + this.id + "\n");
		sb.append("Rest Api Uri: " + this.uri.toString() + "\n");
		sb.append("Request Count:" + requestCount + "\n");
		sb.append("Thread Count:" + threadCount + "\n");
		sb.append("Method:" + method.toString() + "\n");
		sb.append("Request Body:" + requestBody + "\n");
		return sb.toString();
	}
}
