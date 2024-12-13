package hr.fer.rassus.lab.prvi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class RassusPrviLabApplication {

	public static void main(String[] args) {
		SpringApplication.run(RassusPrviLabApplication.class, args);
	}

}
