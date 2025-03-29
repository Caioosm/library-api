package io.github.caioosm.libraryapi.services;

import io.github.caioosm.libraryapi.exceptions.OperacaoNaoPermitidaException;
import io.github.caioosm.libraryapi.model.Autor;
import io.github.caioosm.libraryapi.model.Usuario;
import io.github.caioosm.libraryapi.repositories.AutorRepository;
import io.github.caioosm.libraryapi.repositories.LivroRepository;
import io.github.caioosm.libraryapi.security.SecurityService;
import io.github.caioosm.libraryapi.validator.AutorValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AutorService {

    private final AutorRepository autorRepository;
    private final AutorValidator autorValidator;
    private final LivroRepository livroRepository;
    private final SecurityService securityService;

    public Autor salvar(Autor autor) {
        autorValidator.validar(autor);
        Usuario usuario = securityService.obterUsuarioLogado();
        autor.setUsuario(usuario);
        return autorRepository.save(autor);
    }

    public void atualizar(Autor autor) {
        if (autor.getId() == null) {
            throw new IllegalArgumentException("Para atualziar, eh necessario que o autor ja esteja salvo na base!");
        }
        autorValidator.validar(autor);
        autorRepository.save(autor);
    }

    public Optional<Autor> buscarPorId(UUID id) {
        return autorRepository.findById(id);
    }

    public void deletar(Autor autor) {
        if(possuiLivro(autor)){
            throw new OperacaoNaoPermitidaException("Nao foi permitida a exclusao! Autor possui livros cadastrados");
        }
        autorRepository.delete(autor);
    }

    /*
        metodo utilizado para testes no inicio da API
     */

    //    public List<Autor> pesquisa(String nome, String nacionalidade) {
//        if (nome != null && nacionalidade != null) {
//            return autorRepository.findByNomeAndNacionalidade(nome, nacionalidade);
//        }
//
//        if (nome != null) {
//            return autorRepository.findByNome(nome);
//        }
//
//        if (nacionalidade != null) {
//            return autorRepository.findByNacionalidade(nacionalidade);
//        }
//
//        return autorRepository.findAll();
//    }

    public List<Autor> pesquisaByExample(String nome, String nacionalidade){
        var autor = new Autor();
        autor.setNome(nome);
        autor.setNacionalidade(nacionalidade);

        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreNullValues()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        Example<Autor> autorExample = Example.of(autor, matcher);
        return autorRepository.findAll(autorExample);
    }

    public boolean possuiLivro(Autor autor) {
        return livroRepository.existsByAutor(autor);
    }
}
