package com.pm.notificationapp.controller;

import com.pm.notificationapp.dto.NotificationDto;
import com.pm.notificationapp.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public NotificationDto createNotification(@RequestBody NotificationDto dto) {
        return notificationService.save(dto);
    }

    @GetMapping("/{uid}")
    public NotificationDto getNotificationByUid(@PathVariable("uid") String uid) {
        return notificationService.getByUid(UUID.fromString(uid));
    }
}