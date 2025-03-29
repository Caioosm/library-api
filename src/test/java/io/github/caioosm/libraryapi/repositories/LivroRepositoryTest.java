package io.github.caioosm.libraryapi.repositories;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import io.github.caioosm.libraryapi.model.Autor;
import io.github.caioosm.libraryapi.model.GeneroLivro;
import io.github.caioosm.libraryapi.model.Livro;

/*
 * @see LivroRepository
 */

@SpringBootTest
class LivroRepositoryTest {

    @Autowired
    LivroRepository repository;

    @Autowired
    AutorRepository autorRepository;

    @Test
    void salvarTeste() {
        Livro livro = new Livro();
        livro.setIsbn("98989-98989");
        livro.setPreco(BigDecimal.valueOf(100));
        livro.setTitulo("como ter 20cm de piroca");
        livro.setGenero(GeneroLivro.ROMANCE);
        livro.setDataPublicacao(LocalDate.of(2025, 2, 2));

        Autor autor = autorRepository.findById(UUID.fromString("fff0b8a9-6154-44f1-b174-2ae551e9d53b")).orElse(null);

        livro.setAutor(autor);

        repository.save(livro);
    }

    @Test
    void salvarCascadeTeste() {
        Livro livro = new Livro();
        livro.setIsbn("98989-98989");
        livro.setPreco(BigDecimal.valueOf(100));
        livro.setTitulo("como mostrar a mulher pelada pra todo mundo e ir embora");
        livro.setGenero(GeneroLivro.CIENCIA);
        livro.setDataPublicacao(LocalDate.of(2025, 2, 2));

        Autor autor = new Autor();

		autor.setNome("penis");
		autor.setNacionalidade("corno");
		autor.setDataNascimento(LocalDate.of(1978, 2, 7));

        livro.setAutor(autor);

        repository.save(livro);
    }

    @Test
    void atualizarAutorDoLivro(){
        var livroParaAtt = repository.findById(UUID.fromString("2319b35b-4c16-4572-af43-77a7e8dd6934")).orElse(null);

        UUID idAutor = UUID.fromString("7d9bde16-3313-46d2-a2b7-15f8aaf4c686");
        Autor penis = autorRepository.findById(idAutor).orElse(null);

        livroParaAtt.setAutor(penis);

        repository.save(livroParaAtt);
    }

    @Test
    void deletar(){
        UUID idLivro = UUID.fromString("2319b35b-4c16-4572-af43-77a7e8dd6934");
        repository.deleteById(idLivro);
    }

    @Test
    @Transactional
    void buscarLivroTeste(){
        UUID id = UUID.fromString("e74713c3-63c8-4a4b-b872-fd2a2eee8a63");
        Livro livro = repository.findById(id).orElse(null);
        System.out.println("livro: ");
        System.out.println(livro.getTitulo());
        System.out.println("Autor: ");
        System.out.println(livro.getAutor().getNome());
    }

    @Test
    void pesquisaPorTituloTeste(){
        List<Livro> lista = repository.findByTitulo("Sexo de ladinho, a grande ciencia por tras");
        lista.forEach(System.out::println);
    }

    @Test
    void pesquisaPorISBNTeste(){
        Optional<Livro> livro = repository.findByIsbn("38569-13549");
        livro.ifPresent(System.out::println);
    }

    @Test
    void pesquisaPorTituloAndPrecoTeste(){
        // var preco = BigDecimal.valueOf(150.00);
        List<Livro> lista = repository.findByTituloAndPreco("Sexo a 3, como fazer", BigDecimal.valueOf(150.00));
        lista.forEach(System.out::println);
    }

    @Test
    void listarLivrosComQuery(){
        var resultado = repository.listarTodos();
        resultado.forEach(System.out::println);
    }

    @Test
    void listarAutoresLivroComQuery(){
        var resultado = repository.listarAutoresDosLivros();
        resultado.forEach(System.out::println);
    }

    @Test
    void listarTitulosNaoRepetidosDosLivros(){
        var resultado = repository.listarNomesDiferentesLivro();
        resultado.forEach(System.out::println);
    }

    @Test
    void listarGenerosDeLivrosAutoresBrasileiros(){
        var resultado = repository.listarGenerosAutoresBrasileiros();
        resultado.forEach(System.out::println);
    }

    @Test
    void listagemQueryComParametro(){
        var resultado = repository.findByGenero(GeneroLivro.ROMANCE, "dataPublicacao");
        resultado.forEach(System.out::println);
    }

    @Test
    void deletePorGeneroTeste(){
        repository.deleteByGenero(GeneroLivro.ROMANCE);
    }

    @Test
    void updateDataPublicacaoTeste(){
        repository.updateDataPublicacao(LocalDate.of(2000, 8, 1));
    }
}
