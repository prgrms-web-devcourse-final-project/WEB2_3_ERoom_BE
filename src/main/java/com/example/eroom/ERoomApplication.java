package com.example.eroom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
//@EnableJpaAuditing
public class ERoomApplication {

    public static void main(String[] args) {
        SpringApplication.run(ERoomApplication.class, args);
    }

}
