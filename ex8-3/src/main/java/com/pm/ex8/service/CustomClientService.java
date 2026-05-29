package com.pm.ex8.service;

import com.pm.ex8.entity.Client;
import com.pm.ex8.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomClientService implements RegisteredClientRepository{

    private final ClientRepository clientRepository;

    @Override
    public void save(RegisteredClient registeredClient) {
        clientRepository.save(Client.from(registeredClient));
    }

    @Override
    public RegisteredClient findById(String id) {
        return Client.from(clientRepository.findById(Long.valueOf(id)).orElseThrow());
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        return Client.from(clientRepository.findByClientId(clientId).orElseThrow());
    }
}
