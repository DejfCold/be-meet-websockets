package cz.dejfcold.bemeetwebsockets.config

import cz.dejfcold.bemeetwebsockets.interceptor.CustomChannelInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.ChannelRegistration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@Configuration
@EnableWebSocketMessageBroker
class StompWebsocketConfig(
    private val interceptor: CustomChannelInterceptor
) : WebSocketMessageBrokerConfigurer {
    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        registry.enableSimpleBroker("/topic", "/queue")
        registry.setApplicationDestinationPrefixes("/app")
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/stomp/websocket")
    }

    override fun configureClientInboundChannel(registration: ChannelRegistration) {
        registration.interceptors(interceptor)
    }

    override fun configureClientOutboundChannel(registration: ChannelRegistration) {
        registration.interceptors(interceptor)
    }
}