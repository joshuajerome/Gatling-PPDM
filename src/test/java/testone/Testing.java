package testone;

/* Import Statements */

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import java.util.List;
import java.util.ArrayList;

/** TODO: 
 * 
 * Login only at start of test
 * Logout API
 * Not Hardcoding Login Credentials
 */

public class Testing extends Simulation {
	
	/* VARS */
	
	private List<TestSuite> tests = CSVReader.processFile(getDataFile());
	private List<PopulationBuilder> scnList= new ArrayList<>();
	private String credentials;
	
	/* Builders */
	
	private HttpProtocolBuilder httpProtocol;
	private ChainBuilder get;
	private ChainBuilder post;
	private ChainBuilder login;
	private ScenarioBuilder scn;
	
	/* Get File */
	
	private String getDataFile() {
		String datafile = System.getProperty("datafile");
		
		if (datafile == null || datafile.length() == 0) {
			datafile = "data.csv";
		}
		return datafile;
	}
	
	/* Get Login Credentials */
	
	private String getCredentials() throws Exception {
		String username = System.getProperty("username");
		String password = System.getProperty("password");
		if (username == null || password == null) {
			throw new Exception("missing credentials");
		}
		String credentials = "{\"username\":\"" + username
	    		+ "\",\"password\":\"" 
	    		+ password + "\"}";
		return credentials;
	}
	
	private void login() {
		
		try {
			credentials = getCredentials();
		} catch (Exception e) {
			System.out.println("Failed to Login");
			return;
		}
		
		login = exec(
    			http("login request")
    			.post(":8443/api/v2/login")
    			.header("content-type","application/json")
    			.body(StringBody(credentials))
    			.check(jmesPath("access_token").ofString().saveAs("access_token"))
    		    .check(jmesPath("token_type").ofString().saveAs("token_type"))
    			 );
	}
	
	@SuppressWarnings("unchecked")
	private void runScenarios() {
		
		
		tests.stream().forEach(ts -> {
			
			httpProtocol = HttpDsl.http
					.baseUrl("https://" + ts.ip)
					.acceptHeader("application/json")
					.userAgentHeader("Gatling Performance Test");
			
			switch(ts.HTTPmethod) {
				case GET:
					
					get = exec(http("HTTP Request: GET")
							.get(ts.uri.toString())
							.header("Authorization", session -> session.getString("token_type") 
									+ " " + session.getString("access_token"))
							);
					
					scn = scenario("Test Suite # " 
									+ ts.id + "::" 
									+ ts.HTTPmethod
									+ " "
									+ ts.uri.toString())
									.exec(login,get);
					scnList.add(scn.injectClosed(constantConcurrentUsers(ts.requestCount).during(java.time.Duration.ofSeconds(ts.testDuration)))
			         .protocols(httpProtocol));
					
					break;
					
				case POST:
					
					post = exec(http("HTTP Request: POST")
							.post(":" + ts.port + ts.restApiUri)
							.header("Authorization", session -> session.getString("token_type") 
									+ " " + session.getString("access_token"))
							.body(RawFileBody("postBody.json")).asJson()
							);
					
					scn = scenario("Test Suite # " 
							+ ts.id + "::" 
							+ ts.HTTPmethod
							+ " "
							+ ts.uri.toString())
							.exec(login,post);
					scnList.add(scn.injectClosed(constantConcurrentUsers(ts.requestCount).during(java.time.Duration.ofSeconds(ts.testDuration)))
			         .protocols(httpProtocol));
					
					break;
			}
			
		});
	}
	
	{
		login();
		runScenarios();
		setUp(scnList)
        .protocols(httpProtocol);
		
	}
	
	
}
