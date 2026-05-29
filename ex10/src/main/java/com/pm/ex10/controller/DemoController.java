package com.pm.ex10.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DemoController {

    private final OAuth2AuthorizedClientManager auth2AuthorizedClientManager; //proxy

    @GetMapping("/token")
    public String token() {
        OAuth2AuthorizeRequest request = OAuth2AuthorizeRequest.withClientRegistrationId("1")
                .principal("client")
                .build();

        OAuth2AuthorizedClient client = auth2AuthorizedClientManager.authorize(request);
        return client != null ? client.getAccessToken().getTokenValue() : null;
    }
}
