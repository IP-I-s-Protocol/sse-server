package com.six.sse_server.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.six.sse_server.values.EventServiceTestValues;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.DefaultMessage;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@ExtendWith(MockitoExtension.class)
class EventServiceTest implements EventServiceTestValues {

    @InjectMocks
    private EventService eventService;

    @Mock
    private RedisMessageListenerContainer redisContainer;

    @Mock
    private SseEmitter sseEmitter;

    @Nested
    @DisplayName("옥션 이벤트 구독 테스트")
    class SubScribeEventAuctionServiceTest {

        @Test
        void 옥션_이벤트_구독_성공_테스트() {
            //when
            eventService.subscribeAuctionEvent(TEST_AUCTION1_ID, TEST_AUCTION1_EMITTER);
            //then
            then(redisContainer).should(times(1))
                .addMessageListener(any(MessageListenerAdapter.class), any(ChannelTopic.class));
        }

        @Test
        void 옥션_이벤트_구독_실패_테스트() throws IOException {
            //given
            ChannelTopic topic = new ChannelTopic(EVENT_TOPIC + TEST_AUCTION1_ID);

            doThrow(IOException.class).when(sseEmitter).send(anyString());
            //when
            eventService.subscribeAuctionEvent(TEST_AUCTION1_ID, sseEmitter);

            ArgumentCaptor<MessageListener> listenerCaptor = ArgumentCaptor.forClass(MessageListener.class);
            verify(redisContainer).addMessageListener(listenerCaptor.capture(), eq(topic));

            MessageListener listener = listenerCaptor.getValue();
            byte[] messageBody = "Test message".getBytes();
            Message message = new DefaultMessage(topic.getTopic().getBytes(), messageBody);
            //then
            try {
                listener.onMessage(message, topic.getTopic().getBytes());
            } catch (Exception e) {
                verify(sseEmitter).completeWithError(any(IOException.class));
            }
        }


    }
}