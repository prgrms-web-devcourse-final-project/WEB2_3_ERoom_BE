package com.example.eroom.domain.chat.config;

import com.example.eroom.domain.auth.security.JwtTokenProvider;
import com.example.eroom.domain.chat.interceptor.WebSocketAuthChannelInterceptor;
import com.example.eroom.domain.chat.interceptor.WebSocketAuthInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/queue", "/notifications");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins("http://localhost:5173", "https://errom.netlify.app")
                .addInterceptors(new HttpHandshakeInterceptor()) // interceptor
                .withSockJS();
    }

    // 웹 소켓 메시지 인증
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new WebSocketAuthChannelInterceptor(jwtTokenProvider));
    }

    /**
     * WebSocket 핸드셰이크 요청 시 JWT 인증을 수행하는 인터셉터
     */
    private class HttpHandshakeInterceptor implements HandshakeInterceptor {
        @Override
        public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                       WebSocketHandler wsHandler, Map<String, Object> attributes) {
            if (request instanceof ServletServerHttpRequest servletRequest) {
                HttpServletRequest httpRequest = servletRequest.getServletRequest();
                String token = httpRequest.getHeader("Authorization");

                if (token != null && token.startsWith("Bearer ")) {
                    token = token.substring(7);
                    if (jwtTokenProvider.validateToken(token)) {
                        attributes.put("user", jwtTokenProvider.getAuthentication(token));
                        return true;
                    }
                }
            }
            System.out.println("[websocket] : 웹소켓 인증 실패");
            return true; // 인증 실패 시 / 일단 무조건 연결 허용
        }

        @Override
        public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Exception exception) {

        }

    }
}
