package testone;
import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;


public class TestOne extends Simulation {
	
	static String username = "admin";
	static String password = "Changeme@1";
    static String credentials = "{\"username\":\"" + username
    		+ "\",\"password\":\"" 
    		+ password + "\"}";
    static String FN = "sampleCSV";
    
    HttpProtocolBuilder httpProtocol = HttpDsl.http
            .baseUrl("https://10.235.46.217")
            .acceptHeader("application/json")
            ;
           
    ChainBuilder post = exec(
    			http("post request")
    			.post(":8443/api/v2/login")
    			.header("content-type","application/json")
    			.body(StringBody(credentials))
    			//.check(status().is(200))
    			.check(jmesPath("access_token").ofString().saveAs("access_token"))
    		    .check(jmesPath("token_type").ofString().saveAs("token_type"))
    			 );
    
    ChainBuilder get = exec(
				http("get request")
				.get(":8580/api/v3/infrastructure-objects")
				.header("Authorization", session -> session.getString("token_type") 
						+ " " + session.getString("access_token"))
				// .check(bodyString().saveAs("body_string"))  
				);
    
    ChainBuilder debug = exec(
    	     session -> {
    	         System.out.println(session.getString("access_token"));
    	          return session;
    	      }
    	   );
    

    @SuppressWarnings("unchecked")
    ScenarioBuilder scn = scenario("request")
    		.exec(post)
    		.exec(get);
    {
    	setUp(
    			scn.injectOpen(constantUsersPerSec(1000)
    			.during(java.time.Duration.ofSeconds(60)))).protocols(httpProtocol);
    }
}