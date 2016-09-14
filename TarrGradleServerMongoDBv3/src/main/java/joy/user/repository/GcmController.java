package joy.user.repository;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;


@Controller
public class GcmController {

	
	@Autowired
	private UserRepository userRepo;
	
	@RequestMapping(value = "/sendGcm", method = RequestMethod.POST)
	public @ResponseBody Boolean sendGcm(@RequestBody String regid, @RequestParam("title") String title, 
			                             @RequestParam("data") String message)
	{  
		  String apiKey = "AIzaSyDHEaIjPPlwkG-5tmfuWmdQ_qHWbqFmLn4";
		  System.out.println("regid"+regid);
	      Content content = createContent(regid, title, message);
	      try{

	          // 1. URL
	          URL url = new URL("https://android.googleapis.com/gcm/send");

	          // 2. Open connection
	          HttpURLConnection conn = (HttpURLConnection) url.openConnection();

	          // 3. Specify POST method
	          conn.setRequestMethod("POST");

	          // 4. Set the headers
	          conn.setRequestProperty("Content-Type", "application/json");
	          conn.setRequestProperty("Authorization", "key="+apiKey);

	          conn.setDoOutput(true);

	              // 5. Add JSON data into POST request body

	              //`5.1 Use Jackson object mapper to convert Contnet object into JSON
	              ObjectMapper mapper = new ObjectMapper();

	              // 5.2 Get connection output stream
	              DataOutputStream wr = new DataOutputStream(conn.getOutputStream());

	              // 5.3 Copy Content "JSON" into
	              mapper.writeValue(wr, content);

	              // 5.4 Send the request
	              wr.flush();

	              // 5.5 close
	              wr.close();

	              // 6. Get the response
	              int responseCode = conn.getResponseCode();
	              System.out.println("\nSending 'POST' request to URL : " + url);
	              System.out.println("Response Code : " + responseCode);

	              BufferedReader in = new BufferedReader(
	                      new InputStreamReader(conn.getInputStream()));
	              String inputLine;
	              StringBuffer response = new StringBuffer();

	              while ((inputLine = in.readLine()) != null) {
	                  response.append(inputLine);
	              }
	              in.close();

	              // 7. Print result
	              System.out.println(response.toString());

	              } catch (MalformedURLException e) {
	                  e.printStackTrace();
	              } catch (IOException e) {
	                  e.printStackTrace();
	              }
	          
	      
	     return true;
	}
	
	public static Content createContent(String regid, String title, String message){

        Content c = new Content();

        c.addRegId(regid);
        c.createData(title, message);

        return c;
    }


	
	
	
}
