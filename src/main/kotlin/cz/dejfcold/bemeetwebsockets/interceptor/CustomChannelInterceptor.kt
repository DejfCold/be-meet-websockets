package cz.dejfcold.bemeetwebsockets.interceptor

import org.slf4j.LoggerFactory
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.SimpMessageType
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.messaging.support.MessageBuilder
import org.springframework.messaging.support.MessageHeaderAccessor
import org.springframework.stereotype.Component
import java.lang.Exception

private val logger = LoggerFactory.getLogger(CustomChannelInterceptor::class.java)

@Component
class CustomChannelInterceptor(
) : ChannelInterceptor {
    override fun preSend(message: Message<*>, channel: MessageChannel): Message<*>? {
        val headers = MessageHeaderAccessor(message)

        val messageBuilder = if (headers.getHeader("simpMessageType") == SimpMessageType.CONNECT_ACK) {
            MessageBuilder.withPayload("Injected Message!".toByteArray()).setHeaders(headers)
        } else {
            MessageBuilder.fromMessage(message).setHeaders(headers)
        }

        val alteredMessage = messageBuilder.build()

        logger.info("PreSend message id: ${alteredMessage.headers.id}")
        return alteredMessage
    }

    override fun postSend(message: Message<*>, channel: MessageChannel, sent: Boolean) {
        logger.info("PostSend message id: ${message.headers.id}")
        super.postSend(message, channel, sent)
    }

    override fun afterSendCompletion(message: Message<*>, channel: MessageChannel, sent: Boolean, ex: Exception?) {
        logger.info("AfterSendCompletion message id: ${message.headers.id}")
        super.afterSendCompletion(message, channel, sent, ex)
    }

    override fun preReceive(channel: MessageChannel): Boolean {
        logger.info("PreReceive message: $channel")
        return super.preReceive(channel)
    }

    override fun postReceive(message: Message<*>, channel: MessageChannel): Message<*>? {
        logger.info("PostReceive message id: ${message.headers.id} : $message")
        return super.postReceive(message, channel)
    }

    override fun afterReceiveCompletion(message: Message<*>?, channel: MessageChannel, ex: Exception?) {
        logger.info("AfterReceiveCompletion message id: ${message?.headers?.id}")
        super.afterReceiveCompletion(message, channel, ex)
    }
}