package io.github.caioosm.libraryapi.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.caioosm.libraryapi.model.Autor;
import io.github.caioosm.libraryapi.model.GeneroLivro;
import io.github.caioosm.libraryapi.model.Livro;
import io.github.caioosm.libraryapi.repositories.AutorRepository;
import io.github.caioosm.libraryapi.repositories.LivroRepository;

@Service
public class TransacaoService {

    @Autowired
    AutorRepository autorRepository;

    @Autowired
    LivroRepository livroRepository;

    @Transactional
    public void atualizacaoSemAtualizar() {
        var livro = livroRepository.findById(UUID.fromString("e74713c3-63c8-4a4b-b872-fd2a2eee8a63")).orElse(null);

        livro.setDataPublicacao(LocalDate.of(2000, 2, 3));
    }

    @Transactional
    public void executar() {
        Autor autor = new Autor();
        autor.setNome("penis");
        autor.setNacionalidade("brasileira");
        autor.setDataNascimento(LocalDate.of(1978, 2, 7));

        autorRepository.save(autor);

        Livro livro = new Livro();
        livro.setIsbn("98989-98989");
        livro.setPreco(BigDecimal.valueOf(100));
        livro.setTitulo("livro de viado");
        livro.setGenero(GeneroLivro.CIENCIA);
        livro.setDataPublicacao(LocalDate.of(2025, 2, 2));
        livro.setAutor(autor);

        livroRepository.save(livro);

        if (autor.getNome().equals("penis")) {
            throw new RuntimeException("Rollback!");
        }
    }
}
