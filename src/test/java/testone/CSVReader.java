package testone;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CSVReader {
	
	private static List<TestSuite> tests;
	
	/** 
	 * arr[0] = id
	 * arr[1] = restApiUri
	 * arr[2] = port
	 * arr[3] = ip
	 * arr[4] = requestCount
	 * arr[5] = threadCount
	 * arr[6] = HTTP Method
	 * arr[7] = body
	 */
	
	public static List<TestSuite> processFile (String filename) {
		Pattern pattern = Pattern.compile(",");
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		InputStream  fis = cl.getResourceAsStream(filename);
		try (Stream<String> lines = new BufferedReader(new InputStreamReader(fis)).lines()) {
				tests = lines.map(line -> {
				String[] arr = pattern.split(line);
				return new TestSuite(
					Integer.parseInt(arr[0]),
					arr[1],
					Integer.parseInt(arr[2]),
					arr[3],
					Integer.parseInt(arr[4]),
					Integer.parseInt(arr[5]),
					HTTPMethod.valueOf(arr[6]),
					arr[7]
				);
			}).collect(Collectors.toList());
//			tests.forEach(System.out::println);
		} catch (Exception e) {
			System.out.println("Caught exception: " + e.getMessage());
		}
		return tests;
	}

}
