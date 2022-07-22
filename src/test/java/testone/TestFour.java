package testone;

/* Import Statements */

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import java.util.ArrayList;
import java.util.List;

public class TestFour extends Simulation {
	
	/* VARS */
	
	private List<TestSuite> tests = CSVReader.processFile(getDataFile());
	private List<ScenarioBuilder> scnList = new ArrayList<>(); 
	
	/* Get File */
	
	private String getDataFile() {
		String datafile = System.getProperty("datafile");
		
		if (datafile == null || datafile.length() == 0) {
			throw new RuntimeException("*** EMPTY DATA FILE ARG ***");
		}
		return datafile;
	}
	
	/* Builders */
	
	private HttpProtocolBuilder httpProtocol;
	
	/* different HTTP requests */
	private ChainBuilder cb;
	
	@SuppressWarnings("unchecked")
	private void testIterate() {
		
		tests.stream().forEach(ts -> {
			
			scnList.add(scenario(
					"TEST SUITE #" 
					+ tests.indexOf(ts)
					+ "::" 
					+ ts.HTTPmethod 
					+ " "
					+ ts.uri.toString()));
			
			httpProtocol = HttpDsl.http
					.baseUrl("https://" + ts.ip)
					.acceptHeader("application/json")
					.userAgentHeader("Gatling Performance Test");
			
			switch(ts.HTTPmethod) {
				case GET:
					cb = exec(http("HTTP Request: GET")
							.get(ts.uri.toString())
							.header("Authorization", session -> session.getString("token_type") 
									+ " " + session.getString("access_token"))
							);
					break;
					
				case POST:
					cb = exec(http("HTTP Request: POST")
							.post(":" + ts.port + ts.restApiUri)
							.header("content-type", "application/json")
							);
					break;
			}
			scnList.get(scnList.size() - 1).exec(cb);
		});
	}
	
	{
		testIterate();
		scnList.stream().forEach(scenario -> 
			{
				setUp(scenario
						.injectOpen(constantUsersPerSec(10)
								.during(java.time.Duration.ofSeconds(5))))
				.protocols(httpProtocol);
			}
		);
		
		PopulationBuilder pb[] = {};
		setUp();
	}
	
}
