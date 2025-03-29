package io.github.caioosm.libraryapi.controller.mappers;

import org.mapstruct.Mapper;

import io.github.caioosm.libraryapi.controller.dto.AutorDTO;
import io.github.caioosm.libraryapi.model.Autor;

@Mapper(componentModel = "spring")
public interface AutorMapper {

    Autor toEntity(AutorDTO dto);

    AutorDTO toDto(Autor autor);
}
