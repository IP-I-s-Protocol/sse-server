package com.six.sse_server.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.six.sse_server.service.EventService;
import com.six.sse_server.values.EventServiceTestValues;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = EventController.class)
class EventControllerTest implements EventServiceTestValues {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    @Nested
    @DisplayName("이벤트 컨트롤러 테스트")
    class EventStreamingTest {

        @Test
        void 경매_이벤트_스트리밍_테스트() throws Exception {

            mockMvc.perform(get("/stream/auctions/{auctionId}", TEST_AUCTION1_ID)
                .accept(MediaType.TEXT_EVENT_STREAM_VALUE))
                .andExpect(status().isOk())
                .andExpect(request().asyncStarted())
                .andDo(print());
        }
    }
}