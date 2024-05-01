package com.six.sse_server.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = MainController.class)
class MainControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Nested
    @DisplayName("메인 컨트롤러 테스트")
    class TestMainController {

        @Test
        void 헬스체크_테스트() throws Exception {
            mockMvc.perform(get("/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("ok"));
        }

    }


}