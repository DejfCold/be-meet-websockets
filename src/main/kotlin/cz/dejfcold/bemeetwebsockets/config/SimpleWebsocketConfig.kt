package cz.dejfcold.bemeetwebsockets.config

import cz.dejfcold.bemeetwebsockets.websocket.SimpleHandler
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry

@Configuration
@EnableWebSocket
class SimpleWebsocketConfig(
    private val handler: SimpleHandler
) : WebSocketConfigurer {

    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(handler, "/simple/websocket")
    }
}