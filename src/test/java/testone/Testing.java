package testone;

/* Import Statements */
import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;
import java.util.stream.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/** TODO: 
 * 
 * Login only at start of test
 * Logout API
 */

public class Testing extends Simulation {
	
	/* VARS */
	
	private List<TestSuite> tests = CSVReader.processFile(getDataFile("datafile"));
	private List<PopulationBuilder> scnList= new ArrayList<>();
	private String credentials;
	private final AtomicInteger counter = new AtomicInteger(1);
	
	/* Builders */
	
	private HttpProtocolBuilder httpProtocol;
	private ChainBuilder get;
	private ChainBuilder login;
	private ScenarioBuilder loginScn;
	private ScenarioBuilder postScn;
	private ScenarioBuilder getScn;
	private String access_token = null;
	private String token_type = "";
	
	/* Get File */
	
	private String getDataFile(String name) {
		String datafile = System.getProperty(name);
		
		if (datafile == null || datafile.length() == 0) {
			throw new RuntimeException("*** No Given " + name + " ***");
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
	
	@SuppressWarnings("unchecked")
	private List<String> getNewPost(String currentPost) {
		String newPost;
		List<String> val = new ArrayList<>();
		try {
			Gson gson = new Gson();
			Map<?, ?> map = gson.fromJson(currentPost, Map.class);
			
			Map<String, String> agentRef = (Map<String, String>)map.get("agentRef");
			agentRef.put("id",UUID.randomUUID().toString());
			
			ArrayList<String> naturalIds = (ArrayList<String>)map.get("naturalIds");
			naturalIds.remove(0);
			naturalIds.add(UUID.randomUUID().toString());
			
			Map<String, String> temp = (Map<String, String>)map;
			temp.replace("name", "Application Host " + counter.getAndIncrement());			
			
			newPost = gson.toJson(map);
			val.add(agentRef.get("id"));
			val.add(newPost);
			System.out.println("Agent Id: " + agentRef.get("id") + "\n" + newPost);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return val;
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
					
					getScn = scenario("Test Suite # " 
									+ ts.id + "::" 
									+ ts.HTTPmethod
									+ " "
									+ ts.uri.toString())
									.exec(login,get);
					scnList.add(getScn.injectClosed(rampConcurrentUsers(0).to(ts.requestCount).during(java.time.Duration.ofSeconds(ts.testDuration)))
			         .protocols(httpProtocol));
					
					break;
					
				case POST:
					
					  Iterator<Map<String, Object>> bodyFeeder =
					  Stream.generate((Supplier<Map<String, Object>>) () -> {
					      String body = getNewPost(ts.requestBody).get(1);
					      return Collections.singletonMap("body", body);
					    }
					  ).iterator();
					  
					  Iterator<Map<String, Object>> authTokenFeeder =
							  Stream.generate((Supplier<Map<String, Object>>) () -> {
							      return Collections.singletonMap("token", access_token);
							    }
							  ).iterator();
					  
					  try {
							credentials = getCredentials();
						} catch (Exception e) {
							return;
						}
						
						loginScn = scenario("Login")
								.exec(
				    			http("login request")
				    			.post(":8443/api/v2/login")
				    			.header("content-type","application/json")
				    			.body(StringBody(credentials))
				    			.check(jmesPath("access_token").ofString().saveAs("access_token"))
				    		    .check(jmesPath("token_type").ofString().saveAs("token_type"))
				    			 )
								.exec(session -> {
									access_token = session.getString("access_token");
									token_type = session.getString("token_type");
								    return session;
						            });
						postScn = scenario("Test Suite # " 
								+ ts.id + "::" 
								+ ts.HTTPmethod
								+ " "
								+ ts.uri.toString())
								.repeat(ts.requestCount, "index").on(
										 feed(bodyFeeder)
										.feed(authTokenFeeder)
										.exec(http("HTTP Request: POST")
												.post(":" + ts.port + ts.restApiUri)
												.header("Authorization", "bearer" 
														+ " " + "#{token}")
												.body(StringBody("#{body}")).asJson()
												));
						scnList.add(loginScn.injectClosed(constantConcurrentUsers(1).during(java.time.Duration.ofSeconds(10)))
						  .andThen(
								  postScn.injectClosed(rampConcurrentUsers(1).to(ts.threadCount).during((java.time.Duration.ofSeconds(ts.testDuration))))
							         .protocols(httpProtocol)));
				
				break;
			}});
	}
	
	{
		runScenarios();	
		setUp(scnList).protocols(httpProtocol);
		
	}
}
