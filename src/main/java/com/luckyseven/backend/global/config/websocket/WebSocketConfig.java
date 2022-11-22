package com.luckyseven.backend.global.config.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final AuthChannelInterceptor channelInterceptor;

    public WebSocketConfig(AuthChannelInterceptor channelInterceptor) {
        this.channelInterceptor = channelInterceptor;
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        // Add our interceptor for authentication/authorization
        registration.interceptors(channelInterceptor);
    }

    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // SockJS 연결 주소
        registry.addEndpoint("/ws").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 클라이언트에서 보낸 메시지를 받을 prefix
        registry.setApplicationDestinationPrefixes("/app");

        // 해당 주소를 구독하고 있는 클라이언트들에게 메시지 전달
        registry.enableSimpleBroker("/topic");
    }
}