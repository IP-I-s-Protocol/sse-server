package com.six.sse_server.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;

import com.six.sse_server.values.EventServiceTestValues;
import java.io.IOException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.ReactiveSubscription.Message;
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

//        @Test
//        void 옥션_이벤트_구독_실패_테스트() throws IOException {
//            doThrow(IOException.class).when(sseEmitter).send(SseEmitter.event().data(anyString()));
//            assertThrows(IOException.class,
//                () -> eventService.subscribeAuctionEvent(TEST_AUCTION1_ID, TEST_AUCTION1_EMITTER));
//        }
    }
}