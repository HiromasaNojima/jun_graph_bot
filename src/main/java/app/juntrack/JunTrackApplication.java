package app.juntrack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@PropertySource("classpath:app.properties")
public class JunTrackApplication {

	public static void main(String[] args) {
		SpringApplication.run(JunTrackApplication.class, args);
	}

}
