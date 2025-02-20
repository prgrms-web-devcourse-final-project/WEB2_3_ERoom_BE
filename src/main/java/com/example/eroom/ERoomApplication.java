package com.example.eroom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
//@EnableJpaAuditing
public class ERoomApplication {

    public static void main(String[] args) {
        SpringApplication.run(ERoomApplication.class, args);
    }

}
