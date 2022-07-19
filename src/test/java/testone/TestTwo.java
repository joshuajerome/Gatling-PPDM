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
	
	private ScenarioBuilder scn;
	
	/* Get File */
	
	private String getDataFile() {
		String datafile = System.getProperty("datafile");
		
		if (datafile == null || datafile.length() == 0) {
			datafile = "data.csv";
		}
		return datafile;
	}
	
	/* HTTP Protocol Builder */
	
	HttpProtocolBuilder httpProtocol;
	
//	HttpProtocolBuilder httpProtocol = HttpDsl.http
//			.baseUrl(tests.get(iterator).uri.toString())
//			.acceptHeader("application/json")
//			.userAgentHeader("Gatling Performance Test");
	
	/* HTTP Request: POST */
	
//	ChainBuilder post = exec(
//			http("HTTP Request: POST")
//			.post(":" + tests.get(iterator).port + tests.get(iterator).restApiUri)
//			.header("content-type", "application/json")
//			);
	
	/* HTTP Request: GET */
	
//	ChainBuilder get = exec(
//			http("HTTP Request: GET")
//			.get(tests.get(iterator).uri.toString())
//			.header("Authorization", session -> session.getString("token_type") 
//					+ " " + session.getString("access_token"))
//			);
	
	@SuppressWarnings("unchecked")
	private void testIterate() {
		for(TestSuite ts : tests) {
			
			httpProtocol = HttpDsl.http
					.baseUrl(ts.uri.toString())
					.acceptHeader("application/json")
					.userAgentHeader("Gatling Performance Test");
			
			HTTPMethod type = ts.method;
			switch(type) {
				case GET:
					ChainBuilder get = exec(
							http("HTTP Request: GET")
							.get(ts.uri.toString())
							.header("Authorization", session -> session.getString("token_type") 
									+ " " + session.getString("access_token"))
							);
					scn = scenario("GET request").exec(get);
					break;
				case POST:
					ChainBuilder post = exec(
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
