package io.github.caioosm.libraryapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.github.caioosm.libraryapi.controller.dto.UsuarioDTO;
import io.github.caioosm.libraryapi.controller.mappers.UsuarioMapper;
import io.github.caioosm.libraryapi.services.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuarios")
public class UsuarioController {

    private final UsuarioService service;
    private final UsuarioMapper usuarioMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastrar um usuario", description = "Cadastrar um novo usuario na base de dados")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuario salvo com sucesso!"),
            @ApiResponse(responseCode = "422", description = "Erro de validacao!"),
            @ApiResponse(responseCode = "409", description = "Usuario ja existente na base de dados")
    })
    public void salvar(@RequestBody @Valid UsuarioDTO usuarioDTO) {
        var usuario = usuarioMapper.toEntity(usuarioDTO);
        service.salvar(usuario);
    }
}
