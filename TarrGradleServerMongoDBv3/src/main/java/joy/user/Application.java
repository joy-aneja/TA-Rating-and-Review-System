package joy.user;

import joy.user.json.ResourcesMapper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.fasterxml.jackson.databind.ObjectMapper;


@EnableAutoConfiguration
@EnableMongoRepositories
@EnableWebMvc
@Configuration
@ComponentScan
public class Application extends RepositoryRestMvcConfiguration {
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		System.out.println(""
				+ "\n"
				+ "\n"
				+ "\n"
				+ "		    /$$$$$                           /$$$$$$$                      /$$                 /$$       /$$                           /$$\n" + 
				"		   |__  $$                          | $$__  $$                    | $$                | $$      |  $$                         /$$/\n" + 
				"		      | $$  /$$$$$$  /$$   /$$      | $$  \\ $$  /$$$$$$   /$$$$$$$| $$   /$$  /$$$$$$$| $$       \\  $$     /$$$$$$/$$$$      /$$/ \n" + 
				"		      | $$ /$$__  $$| $$  | $$      | $$$$$$$/ /$$__  $$ /$$_____/| $$  /$$/ /$$_____/| $$        \\  $$   | $$_  $$_  $$    /$$/  \n" + 
				"		 /$$  | $$| $$  \\ $$| $$  | $$      | $$__  $$| $$  \\ $$| $$      | $$$$$$/ |  $$$$$$ |__/         \\  $$  | $$ \\ $$ \\ $$   /$$/   \n" + 
				"		| $$  | $$| $$  | $$| $$  | $$      | $$  \\ $$| $$  | $$| $$      | $$_  $$  \\____  $$              \\  $$ | $$ | $$ | $$  /$$/    \n" + 
				"		|  $$$$$$/|  $$$$$$/|  $$$$$$$      | $$  | $$|  $$$$$$/|  $$$$$$$| $$ \\  $$ /$$$$$$$/ /$$           \\  $$| $$ | $$ | $$ /$$/     \n" + 
				"		 \\______/  \\______/  \\____  $$      |__/  |__/ \\______/  \\_______/|__/  \\__/|_______/ |__/            \\__/|__/ |__/ |__/|__/      \n" + 
				"		                     /$$  | $$                                                                                                    \n" + 
				"		                    |  $$$$$$/                                                                                                    \n" + 
				"		                     \\______/                                                                                                     \n" + 
				"");
	}
	
	@Override
	public ObjectMapper halObjectMapper(){
		return new ResourcesMapper();
	}

}
