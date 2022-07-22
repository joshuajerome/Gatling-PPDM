package testone;

/* Import Statements */

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import io.gatling.javaapi.core.PopulationBuilder.*;


import java.util.List;
import java.util.ArrayList;

public class TestTwo extends Simulation {
	
	/* VARS */
	static String username = "admin";
	static String password = "Changeme@1";
    static String credentials = "{\"username\":\"" + username
    		+ "\",\"password\":\"" 
    		+ password + "\"}";
	private List<TestSuite> tests = CSVReader.processFile(getDataFile());
	
	/* Get File */
	
	private String getDataFile() {
		String datafile = System.getProperty("datafile");
		
		if (datafile == null || datafile.length() == 0) {
			datafile = "data.csv";
		}
		return datafile;
	}
	
	/* Builders */
	
	private HttpProtocolBuilder httpProtocol;
	private ChainBuilder get;
	private ChainBuilder post;
	private ChainBuilder login;
	private ScenarioBuilder scn;
	private List<PopulationBuilder> scnList= new ArrayList<>();
	
	@SuppressWarnings("unchecked")
	private void testIterate() {
		
		
		tests.stream().forEach(ts -> {
			
			httpProtocol = HttpDsl.http
					.baseUrl("https://" + ts.ip)
					.acceptHeader("application/json")
					.userAgentHeader("Gatling Performance Test");
			
			switch(ts.HTTPmethod) {
				case GET:
					login = exec(
			    			http("login request")
			    			.post(":8443/api/v2/login")
			    			.header("content-type","application/json")
			    			.body(StringBody(credentials))
			    			//.check(status().is(200))
			    			.check(jmesPath("access_token").ofString().saveAs("access_token"))
			    		    .check(jmesPath("token_type").ofString().saveAs("token_type"))
			    			 );
					
					get = exec(http("HTTP Request: GET")
							.get(ts.uri.toString())
							.header("Authorization", session -> session.getString("token_type") 
									+ " " + session.getString("access_token"))
							);
					scn = scenario("Test Suite # " 
									+ ts.id + ":: " 
									+ ts.HTTPmethod
									+ " "
									+ ts.uri.toString())
									.exec(login,get);
					scnList.add(scn.injectOpen(constantUsersPerSec(1).during(java.time.Duration.ofSeconds(1)))
			         .protocols(httpProtocol));
					
					break;
					
				case POST:
					login = exec(
			    			http("login request")
			    			.post(":8443/api/v2/login")
			    			.header("content-type","application/json")
			    			.body(StringBody(credentials))
			    			//.check(status().is(200))
			    			.check(jmesPath("access_token").ofString().saveAs("access_token"))
			    		    .check(jmesPath("token_type").ofString().saveAs("token_type"))
			    			 );
					
					post = exec(http("HTTP Request: POST")
							.post(":" + ts.port + ts.restApiUri)
							.header("Authorization", session -> session.getString("token_type") 
									+ " " + session.getString("access_token"))
							.body(RawFileBody("postBody.json")).asJson()
							);
					scn = scenario("Test Suite # " 
							+ ts.id + ":: " 
							+ ts.HTTPmethod
							+ " "
							+ ts.uri.toString())
							.exec(login,post);
					scnList.add(scn.injectOpen(constantUsersPerSec(1).during(java.time.Duration.ofSeconds(1)))
			         .protocols(httpProtocol));
					
					break;
			}
			
		});
	}
	
	{

		testIterate();
		setUp(scnList)
        .protocols(httpProtocol);
		
	}
	
	
}
