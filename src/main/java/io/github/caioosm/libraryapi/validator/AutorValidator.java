package io.github.caioosm.libraryapi.validator;

import io.github.caioosm.libraryapi.exceptions.RegistroDuplicadoException;
import io.github.caioosm.libraryapi.model.Autor;
import io.github.caioosm.libraryapi.repositories.AutorRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AutorValidator {
    private AutorRepository repository;

    public AutorValidator(AutorRepository repository) {
        this.repository = repository;
    }

    public void validar(Autor autor) {
        if(validacaoAutorCadastrado(autor)){
            throw new RegistroDuplicadoException("Registro Duplicado!");
        }
    }

    private boolean validacaoAutorCadastrado(Autor autor) {
        Optional<Autor> autorEncontrado = repository.findByNomeAndDataNascimentoAndNacionalidade(
                autor.getNome(), autor.getDataNascimento(), autor.getNacionalidade()
        );

        if(autor.getId() == null){
            return autorEncontrado.isPresent();
        }

        return !autor.getId().equals(autorEncontrado.get().getId()) && autorEncontrado.isPresent();
    }
}
