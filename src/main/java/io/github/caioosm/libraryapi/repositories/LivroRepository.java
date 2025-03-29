package io.github.caioosm.libraryapi.repositories;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import io.github.caioosm.libraryapi.model.Autor;
import io.github.caioosm.libraryapi.model.GeneroLivro;
import io.github.caioosm.libraryapi.model.Livro;

/**
 * @see LivroRepositoryTest
 */
public interface LivroRepository extends JpaRepository<Livro, UUID>, JpaSpecificationExecutor<Livro> {

    //Query methods
    // select * from livro where id_autor = id
    List<Livro> findByAutor(Autor autor);

    //select * from livro where titulo = titulo
    List<Livro> findByTitulo(String titulo);

    //select * from livro where isbn = isbn
    Optional<Livro> findByIsbn(String isbn);

    //select * from livro where titulo = ? and preco = ?
    List<Livro> findByTituloAndPreco(String titulo, BigDecimal preco);

    boolean existsByAutor(Autor autor);

    @Query(" select l from Livro as l order by l.titulo, l.preco ")
    List<Livro> listarTodos();

    @Query(" select a from Livro l join l.autor a")
    List<Autor> listarAutoresDosLivros();

    /*
     * select distinct
     * l.* from
     * livro l
     */
    @Query(" select distinct l.titulo from Livro l ")
    List<String> listarNomesDiferentesLivro();

    @Query("""
        select l.genero
        from Livro l
        join l.autor a
        where a.nacionalidade = 'brasileiro'
        order by l.genero
    """)
    List<String> listarGenerosAutoresBrasileiros();

    /*
     * @Query com parametrizacao
     * named parameters -> parametros nomeados
     */
    @Query(" select l from Livro l where l.genero = :generoLivro order by :ordenacao ")
    List<Livro> findByGenero( @Param("generoLivro") GeneroLivro genero, @Param("ordenacao") String propriedadeOrder );

    /*
     * @Query com parametrizacao
     * positional parameters -> parametros posicionados
     */
    @Query(" select l from Livro l where l.genero = ?1 order by ?2 ")
    List<Livro> findByGeneroPositionalParam(GeneroLivro genero, String propriedadeOrder );


    @Modifying
    @Transactional
    @Query(" delete from Livro where genero = ?1")
    void deleteByGenero(GeneroLivro genero);

    @Modifying
    @Transactional
    @Query(" update Livro set dataPublicacao = ?1 ")
    void updateDataPublicacao(LocalDate novaData);
}
