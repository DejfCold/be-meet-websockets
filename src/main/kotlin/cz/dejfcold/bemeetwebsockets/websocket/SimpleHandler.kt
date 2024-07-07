package cz.dejfcold.bemeetwebsockets.websocket

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.util.concurrent.CopyOnWriteArraySet


private val sessions = CopyOnWriteArraySet<WebSocketSession>()
private val logger = LoggerFactory.getLogger(SimpleHandler::class.java)

@Component
class SimpleHandler(
    private val objectMapper: ObjectMapper
) : TextWebSocketHandler() {

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val data = objectMapper.readValue(message.payload, DataIn::class.java)
        logger.info("Received message from: ${session.id}: $data")
        sessions.forEach {
            it.sendMessage(
                DataOut(
                    data = data.data,
                    sender = if (session.id == it.id) {
                        "you"
                    } else {
                        session.id
                    }
                ).toMessage()
            )
        }
        super.handleTextMessage(session, message)
    }

    override fun afterConnectionEstablished(session: WebSocketSession) {
        sessions.add(session)
        sessions.filterNot { it.id == session.id }.forEach {
            it.sendMessage(DataOut("User ${it.id} has joined the chat!", "server").toMessage())
        }
        session.sendMessage(DataOut("Connected!", "server").toMessage())
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        sessions.filterNot { it.id == session.id }.forEach {
            it.sendMessage(DataOut("User ${it.id} has left the chat!", "server").toMessage())
        }
        sessions.remove(session)
    }

    data class DataIn(val data: String)
    data class DataOut(val data: String, val sender: String) {
        fun toMessage(): TextMessage {
            return TextMessage(ObjectMapper().writeValueAsString(this))
        }
    }
}