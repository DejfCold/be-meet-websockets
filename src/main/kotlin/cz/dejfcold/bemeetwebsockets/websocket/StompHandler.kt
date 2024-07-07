package cz.dejfcold.bemeetwebsockets.websocket

import cz.dejfcold.bemeetwebsockets.dto.Greeting
import cz.dejfcold.bemeetwebsockets.dto.HelloMessage
import org.springframework.messaging.MessageHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.annotation.SendToUser
import org.springframework.stereotype.Controller
import org.springframework.web.util.HtmlUtils
import java.time.Duration
import java.util.Random

private val random = Random()

@Controller
class StompHandler {
    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    fun helloController(message: HelloMessage): Greeting {
        return Greeting("Hello, ${HtmlUtils.htmlEscape(message.name)}!")
    }

    @MessageMapping("/personal")
    @SendToUser("/queue/reply")
    fun personalMessage(@Payload message: HelloMessage, @Header("simpSessionId") sessionId: String, headers: MessageHeaders): Greeting {
        val sleep = random.nextLong(5, 10)
        Thread.sleep(Duration.ofSeconds(sleep))
        return Greeting("Slept for $sleep seconds for $sessionId. Echoing: ${HtmlUtils.htmlEscape(message.name)}")
    }
}