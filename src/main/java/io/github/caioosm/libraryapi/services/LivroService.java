package io.github.caioosm.libraryapi.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import io.github.caioosm.libraryapi.model.GeneroLivro;
import io.github.caioosm.libraryapi.model.Livro;
import io.github.caioosm.libraryapi.model.Usuario;
import io.github.caioosm.libraryapi.repositories.LivroRepository;
import static io.github.caioosm.libraryapi.repositories.specs.LivroSpecs.anoPublicacaoEqual;
import static io.github.caioosm.libraryapi.repositories.specs.LivroSpecs.generoEquals;
import static io.github.caioosm.libraryapi.repositories.specs.LivroSpecs.isbnEqual;
import static io.github.caioosm.libraryapi.repositories.specs.LivroSpecs.nomeAutorLike;
import static io.github.caioosm.libraryapi.repositories.specs.LivroSpecs.tituloLike;
import io.github.caioosm.libraryapi.security.SecurityService;
import io.github.caioosm.libraryapi.validator.LivroValidator;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LivroService {

    private final LivroRepository repository;
    private final SecurityService securityService;
    private final LivroValidator validator;

    public Livro salvarLivro(Livro livro) {
        validator.validar(livro);
        Usuario usuario = securityService.obterUsuarioLogado();
        livro.setUsuario(usuario);
        return repository.save(livro);
    }

    public Optional<Livro> buscarLivroPorId(UUID id) {return repository.findById(id);}

    public void deletarLivro(Livro livro) {repository.delete(livro);}


    public Page<Livro> pesquisa(
            String isbn,
            String titulo,
            String nomeAutor,
            GeneroLivro genero,
            Integer anoPublicacao,
            Integer pagina,
            Integer tamanhoPagina) {

//        Specification<Livro> specs = Specification
//                .where(LivroSpecs.isbnEqual(isbn))
//                .and(LivroSpecs.tituloLike(titulo))
//                .and(LivroSpecs.generoEquals(genero))
//                ;

        //select * from livro where 0 = 0
        Specification<Livro> specs = Specification.where((root, query, cb) -> cb.conjunction());

        if (isbn != null) {
            specs = specs.and(isbnEqual(isbn));
        }
        if (titulo != null) {
            specs = specs.and(tituloLike(titulo));
        }
        if (genero != null) {
            specs = specs.and(generoEquals(genero));
        }
        if (anoPublicacao != null) {
            specs = specs.and(anoPublicacaoEqual(anoPublicacao));
        }
        if (nomeAutor != null) {
            specs = specs.and(nomeAutorLike(nomeAutor));
        }

        Pageable pageRequest = PageRequest.of(pagina, tamanhoPagina);

        return repository.findAll(specs, pageRequest);
    }

    public void atualizarLivro(Livro livro) {
        validator.validar(livro);

        if(livro.getId() == null) {
            throw new IllegalArgumentException("Para atualizar eh necessario que o livro ja esteja salvo na base!");
        }

        repository.save(livro);
    }
}
