package testone;

/* Import Statements */

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import java.util.List;



public class TestTwo extends Simulation {
	
	/* VARS */
	
	List<TestSuite> tests = CSVReader.processFile(getDataFile());
	
	/* Get File */
	
	private String getDataFile() {
		String datafile = System.getProperty("datafile");
		
		if (datafile == null || datafile.length() == 0) {
			datafile = "data.csv";
		}
		return datafile;
	}
	
	/* HTTP Protocol Builder */
	
	HttpProtocolBuilder httpProtocol = HttpDsl.http
			.baseUrl(tests.get(0).uri.toString())
			.acceptHeader("application/json")
			.userAgentHeader("Gatling Performance Test");
	
	/* HTTP Request: POST */
	
	ChainBuilder post = exec(
			http("HTTP Request: POST")
			.post(":" + tests.get(0).port + tests.get(0).restApiUri)
			.header("content-type", "application/json")
			);
			
	
	/* HTTP Request: GET */
	
	/**/
	
	{
		
	}
	
	
}
