package com.learnway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    // 메시지 브로커 설정
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 메시지 브로커가 구독할 대상 설정
        config.enableSimpleBroker("/topic", "/sub");
        // 클라이언트에서 메시지를 서버로 보낼 때의 prefix 설정
        config.setApplicationDestinationPrefixes("/app", "/pub");
    }

    // WebSocket 엔드포인트
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // WebSocket 엔드포인트 설정 및 SockJS 설정
        registry.addEndpoint("/stomp/chat")
                .setAllowedOriginPatterns("*") // 접근을 허용할 origin 패턴 설정
                .setHandshakeHandler(handshakeHandler()) // HandshakeHandler 설정
                .withSockJS(); // SockJS를 사용하여 WebSocket을 지원하도록 설정

        // HTTPS를 위한 WebSocket 엔드포인트 설정
        registry.addEndpoint("/signaling/video")
                .setAllowedOriginPatterns("*") // 접근을 허용할 origin 패턴 설정
                .setHandshakeHandler(handshakeHandler()) // HandshakeHandler 설정
                .withSockJS(); // SockJS를 사용하여 WebSocket을 지원하도록 설정
    }

    // HandshakeHandler를 빈으로 등록하는 메서드
    @Bean
    public DefaultHandshakeHandler handshakeHandler() {
        return new DefaultHandshakeHandler(new TomcatRequestUpgradeStrategy());
    }

    // TomcatRequestUpgradeStrategy 클래스 확장을 위한 내부 정적 클래스
    // (필요한 경우 TomcatRequestUpgradeStrategy를 커스터마이징할 때 사용)
    public static class TomcatRequestUpgradeStrategy extends org.springframework.web.socket.server.standard.TomcatRequestUpgradeStrategy {
        public TomcatRequestUpgradeStrategy() {
            // 필요한 경우 초기화 코드 추가
        }
    }
}
