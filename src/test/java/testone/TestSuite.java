package testone;

import java.net.URI;
import java.net.URISyntaxException;

enum HTTPMethod {
	GET,
	POST;
}

// Test Suite Class

public class TestSuite {
	
	 int id;
	 String restApiUri;
	 int port;
	 HTTPMethod HTTPmethod;
	 int requestCount;
	 int threadCount;
	 String requestBody;
	 int testDuration;
	 String ip;
	 
	 URI uri;

	TestSuite (int id, String restApiUri, int port, HTTPMethod method, int requestCount, int threadCount, String body, int testDuration, String ip){
		this.id = id;
		this.restApiUri = restApiUri;
		this.port = port;
		this.ip = ip;
		this.requestCount = requestCount;
		this.threadCount = threadCount;
		this.HTTPmethod = method;
		this.requestBody = body;
		this.testDuration = testDuration;
		
		try {
			this.uri = new URI("https://" + this.ip + ":" + this.port + this.restApiUri);
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
		sb.append("Method:" + HTTPmethod.toString() + "\n");
		sb.append("Request Body:" + requestBody + "\n");
		return sb.toString();
	}
}
