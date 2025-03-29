package io.github.caioosm.libraryapi.repositories;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.github.caioosm.libraryapi.model.Autor;
import io.github.caioosm.libraryapi.model.GeneroLivro;
import io.github.caioosm.libraryapi.model.Livro;

@SpringBootTest
public class AutorRepositoryTest {
    
    @Autowired
    AutorRepository repository;

    @Autowired
    LivroRepository livroRepository;
    
    @Test
    public void salvarTeste(){
        Autor autor = new Autor();

		autor.setNome("Kaini Uesti");
		autor.setNacionalidade("corno");
		autor.setDataNascimento(LocalDate.of(1978, 2, 7));

		var autorSalvo = repository.save(autor);
		System.out.println("Autor salvo: " + autorSalvo);
    }

    @Test
    public void atualizarTeste(){
        var id = UUID.fromString("c393ed10-3a76-4b5b-a978-20f92e97d439");
        Optional<Autor> possivelAutor = repository.findById(id);

        if (possivelAutor.isPresent()) {

            Autor autorEncontrado = possivelAutor.get();
            
            System.out.println("dados do autor: ");
            System.out.println(autorEncontrado);

            autorEncontrado.setDataNascimento(LocalDate.of(2000, 10, 12));
            autorEncontrado.setNome("Joseph Pussy");
            autorEncontrado.setNacionalidade("americano");

            repository.save(autorEncontrado);
        }
    }

    @Test
    public void listarTeste(){
        List<Autor> autores = repository.findAll();
        autores.forEach(System.out::println);
    }

    @Test
    public void countTeste(){
        System.out.println("Contagem de autores: " + repository.count());
    }

    @Test
    public void deletePorIdTeste(){
        var id = UUID.fromString("1c96258e-36d6-42ec-81af-436d7b6ca725");
        repository.deleteById(id);
    }

    @Test
    public void deleteTeste(){
        var id = UUID.fromString("1a6aa4b1-4ea5-4f86-9dfc-c362f36810d5");
        var joseph = repository.findById(id).get();
        repository.delete(joseph);
    }

    @Test
    void salvarAutorComLivrosTest(){
        Autor autor = new Autor();

		autor.setNome("Sebastiao");
		autor.setNacionalidade("brasileiro");
		autor.setDataNascimento(LocalDate.of(1950, 10, 8));

        Livro livro = new Livro();
        livro.setIsbn("38569-13549");
        livro.setPreco(BigDecimal.valueOf(150));
        livro.setTitulo("Sexo a 3, como fazer");
        livro.setGenero(GeneroLivro.ROMANCE);
        livro.setDataPublicacao(LocalDate.of(1980, 8, 20));
        livro.setAutor(autor);
        
        Livro livro2 = new Livro();
        livro2.setIsbn("12369-45649");
        livro2.setPreco(BigDecimal.valueOf(1080));
        livro2.setTitulo("Sexo de ladinho, a grande ciencia por tras");
        livro2.setGenero(GeneroLivro.CIENCIA);
        livro2.setDataPublicacao(LocalDate.of(1999, 10, 20));
        livro2.setAutor(autor);

        autor.setLivros(new ArrayList<>());
        autor.getLivros().add(livro);
        autor.getLivros().add(livro2);

        repository.save(autor);

        // livroRepository.saveAll(autor.getLivros());
    }

    @Test
    void mostrarLivros(){
        var id = UUID.fromString("fff0b8a9-6154-44f1-b174-2ae551e9d53b");
        var autor = repository.findById(id).get();

        List<Livro> listaLivros = livroRepository.findByAutor(autor);
        autor.setLivros(listaLivros);

        autor.getLivros().forEach(System.out::println);
    }

}
