package com.example.eroom.notification.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/queue");  // 메시지를 구독할 주소 (클라이언트 → 서버)
        registry.setApplicationDestinationPrefixes("/app"); // 클라이언트에서 서버로 보낼 메시지 prefix 설정
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws") // WebSocket 연결을 위한 엔드포인트
                .setAllowedOrigins("*") // CORS 허용 (프론트엔드 도메인 설정 가능)
                .withSockJS(); // WebSocket을 지원하지 않는 브라우저를 위한 SockJS 사용
    }
}
