package com.six.sse_server.service;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class EventService {

    private final RedisMessageListenerContainer redisContainer;
    private final static String EVENT_TOPIC = "auction-price:";

    public void subscribeAuctionEvent(String auctionId, SseEmitter emitter) {
        ChannelTopic topic = new ChannelTopic(EVENT_TOPIC + auctionId);

        MessageListener listener = (message, pattern) -> {
            try {
                String messageBody = new String(message.getBody());
                emitter.send(SseEmitter.event().data(messageBody));
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        };
        redisContainer.addMessageListener(new MessageListenerAdapter(listener), topic);
    }

}
