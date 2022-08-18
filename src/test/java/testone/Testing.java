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

/** TODO: 
 * Post of the Post
 * 
 * 
 */

public class Testing extends Simulation {
	
	/* VARS */
	
	/* Lists */
	private List<TestSuite> tests = CSVReader.processFile(getDataFile("datafile"));
	private List<PopulationBuilder> scnList= new ArrayList<>();
	
	/* Post Body Variables */
	private String access_token = null;
	private String secondPostID = null;
	private String credentials;
	private final AtomicInteger appHostCounter = new AtomicInteger(1);	
	private final AtomicInteger appSysCounter = new AtomicInteger(1);	
	private final AtomicInteger loginCounter = new AtomicInteger(1);	
	
	/* Builders */
	private HttpProtocolBuilder httpProtocol;
	private ScenarioBuilder loginScn;
	private ScenarioBuilder firstPostScn;
	private ScenarioBuilder secondPostScn;
	private ScenarioBuilder getScn;
	
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
	
	/* login */

	private void login() {
		  try {
				credentials = getCredentials();
			} catch (Exception e) {
				return;
			}
		
		/* Create login scenario */
		loginScn = scenario("Login " + loginCounter.getAndIncrement())
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
				    return session;
		            });
	}
	
	/* Create New (Unique) Post Body */
	
	@SuppressWarnings("unchecked")
	private String getNewPost(String currentPost) {
		String newPost = "";
		try {
			
			/* Converts JSON File to Java Map using GSON */
			Gson gson = new Gson();
			Map<?, ?> map = gson.fromJson(currentPost, Map.class);
			
			/* Creates unique agent ref ID */
			if (map.containsKey("agentRef")) {
				Map<String, String> agentRef = (Map<String, String>)map.get("agentRef");
				agentRef.put("id",UUID.randomUUID().toString());
				/* Changes Application Host Name */
				Map<String, String> temp = (Map<String, String>)map;
				temp.replace("name", "Application Host " + appHostCounter.getAndIncrement());	
			} else if (map.containsKey("applicationHostRef")) {
				Map<String, String> applicationHostRef = (Map<String, String>)map.get("applicationHostRef");
				applicationHostRef.put("id",secondPostID);
				/* Changes Application Host Name */
				Map<String, String> temp = (Map<String, String>)map;
				temp.replace("name", "Application System " + appSysCounter.getAndIncrement());
			}
			/* Creates & replaces unique naturalId */
			ArrayList<String> naturalIds = (ArrayList<String>)map.get("naturalIds");
			naturalIds.remove(0);
			naturalIds.add(UUID.randomUUID().toString());
						
			/* Converts Java Map back to JSON File */
			newPost = gson.toJson(map);
//			System.out.println("\n" + newPost + "\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newPost;
	}
	
	/* Main Gatling Test Script
	 * runs all testing scenarios given via csv config
	 */
	
	@SuppressWarnings({ "unchecked", "static-access" })
	private void runScenarios() {
		
		/* Streams on list of Test Suites */
		tests.stream().forEach(ts -> {
			
			/* Establish HTTP Connection */
			httpProtocol = HttpDsl.http
					.baseUrl("https://" + ts.ip)
					.acceptHeader("application/json")
					.userAgentHeader("Gatling Performance Test");
			
			/* Create Feeders */
		    Iterator<Map<String, Object>> authTokenFeeder =
				    Stream.generate((Supplier<Map<String, Object>>) () -> {
				        return Collections.singletonMap("token", access_token);
				      }
				    ).iterator();
		    
			Iterator<Map<String, Object>> appHostFeeder =
                    Stream.generate((Supplier<Map<String, Object>>) () -> {
                        String body = getNewPost(ts.requestBody.get(0));
                        return Collections.singletonMap("appHostBody", body);
                      }
                    ).iterator();
                    
            Iterator<Map<String, Object>> appSysFeeder =
                    Stream.generate((Supplier<Map<String, Object>>) () -> {
                        String body = ts.requestBody.get(1);
                        return Collections.singletonMap("appSysBody", body);
                      }
                    ).iterator();
            
            Iterator<Map<String, Object>> appSysBodyFeeder =
                    Stream.generate((Supplier<Map<String, Object>>) () -> {
                    	String appSysBody = getNewPost(ts.requestBody.get(1));
                        return Collections.singletonMap("appSysBody", appSysBody);
                      }
                    ).iterator();
	
			/* Switch on HTTP verb 
			 * Currently only supports GET and POST 
			 * Other verbs (PUT, DELETE, etc.) can be added via cases
			 */
			
			switch(ts.HTTPmethod) {
			
			/* HTTP verb: GET */
				case GET:
					  
					/* Creates unique scenario for each GET*/
					getScn = scenario("Test Suite # " 
									+ ts.id + "::" 
									+ ts.HTTPmethod
									+ " "
									+ ts.uri.toString())
									.repeat(ts.requestCount, "index").on(
										feed(authTokenFeeder)
										.exec(http("HTTP Request: GET")
											.get(ts.uri.toString())
											.header("Authorization", "bearer" 
													+ " " + "#{token}")));
					
					/* Accumulate scenario into Population Builder List*/
					scnList.add(loginScn.injectClosed(constantConcurrentUsers(1).during(java.time.Duration.ofSeconds(1)))
							  .andThen(
									  (getScn.injectClosed(rampConcurrentUsers(1).to(ts.requestCount).during(java.time.Duration.ofSeconds(ts.testDuration)))
										         .protocols(httpProtocol))));
					
					break;
					
				case POST:
						
						/* Create subsequent post scenario */
						firstPostScn = scenario("Test suite # "
			                  + ts.id + "::"
			                  + ts.HTTPmethod
			                  + " "
			                  + ts.uri.toString())
		                  		  .feed(appHostFeeder)
		                          .feed(appSysFeeder)
		                          .feed(authTokenFeeder)
		                          .exec(http("HTTP Request: Original POST")
		                                  .post(":" + ts.port + ts.restApiUri)
		                                  .header("Authorization", "bearer"
		                                          + " " + "#{token}")
		                                  .body(StringBody("#{appHostBody}")).asJson()
		                                  .check(jmesPath("agentRef.id").saveAs("secondPostID")))
		                          .exec(session -> {
		                        	  secondPostID = session.getString("secondPostID");
		                        	  return session;
		                          });
					
						/* Create subsequent post scenario */
						secondPostScn = scenario("Test Suite # " 
								+ ts.id + "::" 
								+ ts.HTTPmethod
								+ " "
								+ ts.uri.toString())
								.repeat(ts.requestCount, "index").on(
										 feed(appSysBodyFeeder)
										 .feed(authTokenFeeder)
										 .exec(http("HTTP Request: POST")
												.post(":" + ts.port + ts.restApiUri)
												.header("Authorization", "bearer" 
														+ " " + "#{token}")
												.body(StringBody("#{appSysBody}")).asJson()
												));
						
						/* Add login scenario and then accumulate post scenarios into Population Builder List */
						scnList.add(loginScn.injectClosed(constantConcurrentUsers(1).during(java.time.Duration.ofSeconds(1)))
						  .andThen(
								  firstPostScn.injectClosed(constantConcurrentUsers(1).during(java.time.Duration.ofSeconds(1)))
						  .andThen(
				        		  secondPostScn.injectClosed(rampConcurrentUsers(1).to(ts.threadCount).during((java.time.Duration.ofSeconds(ts.testDuration))))
							         .protocols(httpProtocol))
				        		   ));
				
					break;
				} // end switch
			} // end func within steam
		); // end stream
	} // end method
	
	/* Runs all scenario within PopulationBuilder list */
	{
		login();
		runScenarios();	
		setUp(scnList).protocols(httpProtocol);
	}
}
