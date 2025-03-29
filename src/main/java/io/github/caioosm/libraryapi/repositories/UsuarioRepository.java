package io.github.caioosm.libraryapi.repositories;

import io.github.caioosm.libraryapi.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    Usuario findByLogin(String login);
    Usuario findByEmail(String email);
}
