package io.github.caioosm.libraryapi.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.caioosm.libraryapi.model.Client;

public interface ClientRepository extends JpaRepository<Client, UUID> {

    Client findByClientId(String clientId);
    
}
