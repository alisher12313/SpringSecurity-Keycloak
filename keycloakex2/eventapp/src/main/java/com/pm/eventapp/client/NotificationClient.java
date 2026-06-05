package com.pm.eventapp.client;

import com.pm.eventapp.client.configuration.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "notification-service", url = "${notificationapp.url}", configuration = FeignClientConfig.class)
@Component
public interface NotificationClient {
    @PostMapping("/internal/api/v1/notifications")
    void sendNotification(@RequestBody Map<String, Object> payload);
}
