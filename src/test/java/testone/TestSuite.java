package testone;

import java.awt.datatransfer.SystemFlavorMap;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Base64;



import com.google.gson.Gson;
import com.google.gson.GsonBuilder;



/* Can incorporate several HTTP Verbs */
enum HTTPMethod {
    GET,
    POST,
    POSTPOST;
}



/* TestSuite Class
* Decodes PostBody given as Base64 encryption
* Constructs TestSuite Object with unique CSV fields
*/



public class TestSuite {
    
     int id;
     String restApiUri;
     int port;
     HTTPMethod HTTPmethod;
     int requestCount;
     int threadCount;
     ArrayList<String> requestBody = new ArrayList<String>();
     int testDuration;
     String ip;
     URI uri;
     
    @SuppressWarnings("null")
    TestSuite (int id, String restApiUri, int port, HTTPMethod method, int requestCount, int threadCount, String body, int testDuration, String ip){
        this.id = id;
        this.restApiUri = restApiUri;
        this.port = port;
        this.ip = ip;
        this.requestCount = requestCount;
        this.threadCount = threadCount;
        this.HTTPmethod = method;
        this.testDuration = testDuration;
        
        /* Get base URI*/
        try {
            this.uri = new URI("https://" + this.ip + ":" + this.port + this.restApiUri);    
            
             /* Base 64 Encryption of Post Body
              *
        eyJuYXR1cmFsSWRzIjogWyI3N3o5MjI3ZC05NjRhLTQwNjktYTk1OC04MWI0OTcxYTA0MzU5Il0sIm5hbWUiOiAiQXBwbGljYXRpb24gSG9zdCAxIiwidHlwZSI6ICJHRU5FUklDX0FQUExJQ0FUSU9OX0hPU1QiLCJjYXRlZ29yaWVzIjogWyJBUFBMSUNBVElPTl9IT1NUIiwiQVBQTElDQVRJT05fU1lTVEVNIl0sInZlbmRvciI6ICJHRU5FUklDIiwidmVyc2lvbiI6ICJjdXJyZW50IHZlcnNpb24iLCJkaXNjb3ZlcmVkQXQiOiAiMjAyMS0wMy0yOVQyMzowNDowMS4wMDdaIiwiZGlzY292ZXJ5U3RhdHVzIjogIkFWQUlMQUJMRSIsImhvc3RuYW1lIjoidGVzdC5hc2wubGFiLmVtYy5jb20iLCJhZGRyZXNzZXMiOiBbeyJ2YWx1ZSI6ICIxMC4yNS4xNDUuMTIiLCJ0eXBlIjogIklQVjQifV0sImF2YWlsYWJpbGl0eVN0YXR1cyI6IHsidmFsdWUiOiAiQVZBSUxBQkxFIiwiY2hhbmdlZEF0IjogbnVsbH0sImFnZW50UmVmIjogeyJpZCI6ICI3N3o5MjI3ZC05NjRhLTQwNjktYTk1OC04MWI0OTcxYTA0Nzc4In19
              *
              */
            
            /* Given Base 64 Encyption of Post Body, TODO this:
             * byte[] decodedBytes = Base64.getDecoder().decode(body);
             * this.requestBody = new String(decodedBytes);
             *
             * If implementing this approach:
             * make sure to change the postBody field in data.csv to Base64 encryption
             */
            
        } catch (URISyntaxException e) {
            e.printStackTrace();
         }

       /* Get postBody via location passed via csv */
        try {
            String[] numBodies = body.split("/");
            for(int i = 0; i < numBodies.length; i++) {
                
                StringBuilder sb = new StringBuilder();
                ClassLoader cl = Thread.currentThread().getContextClassLoader();
                InputStream  is = cl.getResourceAsStream(numBodies[i]);    
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String read;
                try {
                    while((read = br.readLine()) != null){
                        sb.append(read);
                    }
                    this.requestBody.add(sb.toString());
                }
                catch(Exception e) {
                        e.printStackTrace();
                }
                }
            }
        catch(Exception e) {
            e.printStackTrace();
        }
   }



   @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Test Suite #" + this.id + "\n");
        sb.append("Rest Api Uri: " + this.uri.toString() + "\n");
        sb.append("Request Count:" + requestCount + "\n");
        sb.append("Thread Count:" + threadCount + "\n");    
        sb.append("Method:" + HTTPmethod.toString() + "\n");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        sb.append("Request Body:" + gson.toJson(requestBody) + "\n");
        return sb.toString();
    }
}