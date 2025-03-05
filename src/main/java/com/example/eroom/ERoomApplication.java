package com.example.eroom;

import com.example.eroom.domain.chat.repository.NotificationRepository;
import com.example.eroom.domain.chat.service.NotificationService;
import com.example.eroom.domain.entity.Member;
import com.example.eroom.domain.entity.NotificationType;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;

@SpringBootApplication
@EnableScheduling
//@EnableJpaAuditing
public class ERoomApplication {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
        SpringApplication.run(ERoomApplication.class, args);
    }

}
