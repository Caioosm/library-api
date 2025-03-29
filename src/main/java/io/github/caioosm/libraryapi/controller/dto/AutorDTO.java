package io.github.caioosm.libraryapi.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

public record AutorDTO(
        UUID id,
        @NotBlank(message = "Nome e obrigatorio!")
        @Size(min = 2, max = 100, message = "Campo fora do padrao de tamanho!")
        String nome,
        @NotNull(message = "Data de nascimento e obrigatoria!")
        @Past(message = "Data nao pode ser futura!")
        LocalDate dataNascimento,
        @NotBlank(message = "O campo nacionalidade e obrigatorio!")
        @Size(min = 2, max = 50, message = "Campo fora do padrao de tamanho")
        String nacionalidade) {

}
