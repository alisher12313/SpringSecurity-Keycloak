package com.pm.ex8.repository;

import com.pm.ex8.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    @Query("""
        select c from clients c where c.clientId = :clientId
    """)
    Optional<Client> findByClientId(@Param("clientId") String clientId);

    String id(Long id);
}
