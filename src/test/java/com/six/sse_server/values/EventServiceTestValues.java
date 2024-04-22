package com.six.sse_server.values;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface EventServiceTestValues {

    String EVENT_TOPIC = "auction-price:";

    String TEST_AUCTION1_ID = "1";

    SseEmitter TEST_AUCTION1_EMITTER = new SseEmitter();


}
