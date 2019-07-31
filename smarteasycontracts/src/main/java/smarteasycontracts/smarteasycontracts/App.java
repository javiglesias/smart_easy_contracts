package smarteasycontracts.smarteasycontracts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import it.ozimov.springboot.mail.configuration.EnableEmailTools;
/**
 * @author Javier Iglesias Sanz
 * @version 1.0
 * @since 2019-02-21
 * Run the SpringBoot app, then comes the fireworks.
 */
@SpringBootApplication
@EnableEmailTools
public class App {
    public static void main( String[] args ) {
    	SpringApplication.run(App.class, args); 
    }
}
