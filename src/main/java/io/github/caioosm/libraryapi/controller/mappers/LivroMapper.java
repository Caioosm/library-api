package io.github.caioosm.libraryapi.controller.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import io.github.caioosm.libraryapi.controller.dto.LivroDTO;
import io.github.caioosm.libraryapi.controller.dto.LivroResponseDTO;
import io.github.caioosm.libraryapi.model.Livro;
import io.github.caioosm.libraryapi.repositories.AutorRepository;

@Mapper(componentModel = "spring", uses = AutorMapper.class)
public abstract class LivroMapper {

    @Autowired
    AutorRepository autorRepository;

    @Mapping(target = "autor", expression = "java( autorRepository.findById(livroDTO.idAutor()).orElse(null) )")
    public abstract Livro toEntity(LivroDTO livroDTO);

    public abstract LivroResponseDTO toDTO(Livro livro);
}
