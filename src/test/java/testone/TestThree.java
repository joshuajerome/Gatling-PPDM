package testone;
import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import java.util.List;


public class TestThree extends Simulation {
	
	private String getDataFile() {
		String datafile = System.getProperty("datafile");
		
		if (datafile == null || datafile.length() == 0) {
			datafile = "janet.csv";
		}
		return datafile;
	}

	List<TestSuite> tests = CSVReader.processFile(getDataFile());
	
    HttpProtocolBuilder httpProtocol = HttpDsl.http
            .baseUrl(tests.get(0).uri.toString())
            .acceptHeader("application/json")
            .userAgentHeader("Gatling Performance Test");


    ScenarioBuilder scn = scenario("Get API request")
            .exec(http("request 1").get("")
            .check(status().is(200))
            .check(jsonPath("$.data.first_name").is("Janet")))
            .pause(1);

    {
    	System.out.println(tests.get(0));
        setUp(scn.injectOpen(constantUsersPerSec(10).during(java.time.Duration.ofSeconds(5))))
                .protocols(httpProtocol);

    }
}