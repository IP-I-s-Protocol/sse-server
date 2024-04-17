package com.six.sse_server.controller;

import com.six.sse_server.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@Slf4j
public class EventController {

    private final EventService eventService;

    @GetMapping(value = "/stream/auctions/{auctionId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamAuctions(@PathVariable String auctionId) {
        SseEmitter emitter = new SseEmitter(5 * 60 * 1000L);
        emitter.onTimeout(emitter::complete);
        emitter.onError(emitter::completeWithError);

        eventService.subscribeAuctionEvent(auctionId, emitter);

        return emitter;
    }
}