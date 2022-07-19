package testone;

/* Import Statements */

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import java.util.List;

public class TestTwo extends Simulation {
	
	/* VARS */
	
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
	private ScenarioBuilder scn;
	
	@SuppressWarnings("unchecked")
	private void testIterate() {
		for(TestSuite ts : tests) {
			
			httpProtocol = HttpDsl.http
					.baseUrl("https://" + ts.ip)
					.acceptHeader("application/json")
					.userAgentHeader("Gatling Performance Test");

			switch(ts.HTTPmethod) {
				case GET:
					get = exec(
							http("HTTP Request: GET")
							.get(ts.uri.toString())
							.header("Authorization", session -> session.getString("token_type") 
									+ " " + session.getString("access_token"))
							);
					scn = scenario("GET request").exec(get);
					break;
				case POST:
					post = exec(
							http("HTTP Request: POST")
							.post(":" + ts.port + ts.restApiUri)
							.header("content-type", "application/json")
							);
					scn = scenario("POST request").exec(post);
					break;
			}
		}
	}
	
	{

		testIterate();
		setUp(scn.injectOpen(constantUsersPerSec(10).during(java.time.Duration.ofSeconds(5))))
         .protocols(httpProtocol);
		
	}
	
	
}
