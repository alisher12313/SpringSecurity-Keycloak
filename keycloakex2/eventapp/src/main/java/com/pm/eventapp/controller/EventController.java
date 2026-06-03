package com.pm.eventapp.controller;

import com.pm.eventapp.dto.EventDto;
import com.pm.eventapp.dto.EventPageDto;
import com.pm.eventapp.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/events")
public class EventController {

    private final EventService eventService;

    @GetMapping
    public EventPageDto getEvents(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        return eventService.getEvents(page, size);
    }

    @PostMapping
    public EventDto createEvent(@RequestBody EventDto dto){
        return eventService.save(dto);
    }
}
