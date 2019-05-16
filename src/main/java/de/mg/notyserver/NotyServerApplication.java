package de.mg.notyserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NotyServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotyServerApplication.class, args);
    }

}
