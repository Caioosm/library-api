package io.github.caioosm.libraryapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.github.caioosm.libraryapi.model.Client;
import io.github.caioosm.libraryapi.services.ClientService;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("clients")
@Tag(name = "Clients")
@Slf4j
public class ClientController {
    
    private final ClientService service;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('GERENTE')")
    @Operation(summary = "Cadastrar Client", description = "Cadastrar um novo client na base de dados")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Client salvo com sucesso!"),
            @ApiResponse(responseCode = "409", description = "Client ja cadastrado!"),
            @ApiResponse(responseCode = "422", description = "Erro de validacao!")
    })
    public void salvar(@RequestBody Client client) {
        log.info("Registrando novo client: {}, com Scope: {}", client.getClientId(), client.getScope());
        service.salvar(client);
    }
    
}
