package me.kisters.ciweda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CiwedaApplication {

	public static void main(String[] args) {
		SpringApplication.run(CiwedaApplication.class, args);
	}

}
