package testone;

/* Import Statements */

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import java.util.List;



public class TestTwo extends Simulation {
	
	/* MISC */
	
	private String getDataFile() {
		String datafile = System.getProperty("datafile");
		
		if (datafile == null || datafile.length() == 0) {
			datafile = "data.csv";
		}
		return datafile;
	}
	
}
