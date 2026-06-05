package com.pm.eventapp.client.configuration;

import feign.RequestInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;

@Configuration
@RequiredArgsConstructor
public class FeignClientConfig {

    private final OAuth2AuthorizedClientManager provider;

    @Bean
    public RequestInterceptor oauth2FeignRequestInterceptor() {
        return requestTemplate -> {
            OAuth2AuthorizeRequest request = OAuth2AuthorizeRequest
                    .withClientRegistrationId("notification-m2m")
                    .principal("eventapp")
                    .build();

            OAuth2AuthorizedClient client = provider.authorize(request);

            requestTemplate.header("Authorization", "Bearer " + client.getAccessToken().getTokenValue());
        };
    }
}
