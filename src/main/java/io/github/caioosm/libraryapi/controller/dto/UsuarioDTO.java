package io.github.caioosm.libraryapi.controller.dto;

import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UsuarioDTO(
    @NotBlank(message="Campo obrigatorio!") 
    String login, 
    @NotBlank(message="Campo obrigatorio!")
    String senha, 
    @NotBlank(message="Campo obrigatorio!")
    @Email(message="email invalido!")
    String email, 
    List<String> roles) {
}
