package io.github.caioosm.libraryapi.controller.mappers;

import io.github.caioosm.libraryapi.controller.dto.UsuarioDTO;
import io.github.caioosm.libraryapi.model.Usuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    Usuario toEntity(UsuarioDTO usuarioDTO);
}
