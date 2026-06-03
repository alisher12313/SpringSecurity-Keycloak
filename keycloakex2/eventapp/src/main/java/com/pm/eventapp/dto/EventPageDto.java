package com.pm.eventapp.dto;

import java.util.List;

public record EventPageDto(
        List<EventDto> content,
        int page,
        int size,
        long totalElements,
        int totalPages
) {}
