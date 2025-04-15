package io.github.caioosm.libraryapi.controller;

import java.net.URI;
import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.caioosm.libraryapi.controller.dto.LivroDTO;
import io.github.caioosm.libraryapi.controller.dto.LivroResponseDTO;
import io.github.caioosm.libraryapi.controller.mappers.LivroMapper;
import io.github.caioosm.libraryapi.model.GeneroLivro;
import io.github.caioosm.libraryapi.model.Livro;
import io.github.caioosm.libraryapi.services.LivroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("livros")
@RequiredArgsConstructor
@Tag(name = "Livros")
public class LivroController implements GenericController {
    private final LivroService livroService;

    private final LivroMapper livroMapper;

    @PostMapping
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    @Operation(summary = "Cadastrar livro", description = "Cadastrar um novo livro")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Livro Cadastrado com sucesso!"),
            @ApiResponse(responseCode = "422", description = "Erro de validacao!"),
            @ApiResponse(responseCode = "409", description = "Livro ja existente!"),
    })
    public ResponseEntity<Object> cadastrarLivro(@RequestBody @Valid LivroDTO livroDTO) {
        //Mapear DTO para entidade
        Livro livro = livroMapper.toEntity(livroDTO);
        //enviar entidade para service validar e salvar na base
        livroService.salvarLivro(livro);
        //criar url para acesso dos dados do livro
        URI location = gerarHeaderLocation(livro.getId());
        return ResponseEntity.created(location).build();
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    @Operation(summary = "Buscar livro por id", description = "Retorna dados de um livro passando o ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Livro encontrado!"),
            @ApiResponse(responseCode = "404", description = "Livro nao encontrado!"),
    })
    public ResponseEntity<LivroResponseDTO> buscarLivroPorId(@PathVariable("id") String id) {
        return livroService.buscarLivroPorId(UUID.fromString(id))
                .map(livro -> {
                    var dto = livroMapper.toDTO(livro);
                    return ResponseEntity.ok(dto);
                }).orElseGet( () -> ResponseEntity.notFound().build() );
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    @Operation(summary = "Deletar Livro", description = "Deletar um livro na base de dados")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Livro Deletado com sucesso!"),
            @ApiResponse(responseCode = "404", description = "Livro nao encontrado!"),
    })
    public ResponseEntity<Object> deletarLivro(@PathVariable("id") String id) {
        return livroService.buscarLivroPorId(UUID.fromString(id))
                .map(livro -> {
                    livroService.deletarLivro(livro);
                    return ResponseEntity.noContent().build();
                }).orElseGet( () -> ResponseEntity.notFound().build() );
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    @Operation(summary = "Pesquisa de livros por parametro", description = "Buscar um livro na base passando parametros")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Livro(s) encontrado(s)!"),
            @ApiResponse(responseCode = "404", description = "Nenhum livro encontrado"),
    })
    public ResponseEntity<Page<LivroResponseDTO>> pesquisa(
            @RequestParam(value = "isbn", required = false)
            String isbn,
            @RequestParam(value = "titulo", required = false)
            String titulo,
            @RequestParam(value = "nome-autor", required = false)
            String nomeAutor,
            @RequestParam(value = "genero", required = false)
            GeneroLivro genero,
            @RequestParam(value = "ano-publicacao", required = false)
            Integer anoPublicacao,
            @RequestParam(value = "pagina", defaultValue = "0")
            Integer pagina,
            @RequestParam(value = "tamanho-pagina", defaultValue = "10")
            Integer tamanhoPagina
    ){
        Page<Livro> paginaResultado = livroService.pesquisa(isbn, titulo, nomeAutor, genero, anoPublicacao, pagina, tamanhoPagina);

        Page<LivroResponseDTO> resultado = paginaResultado.map(livroMapper::toDTO);

        return ResponseEntity.ok(resultado);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    @Operation(summary = "Atualizar Livro", description = "Atualizar livro existente na base")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Livro Atualizado com sucesso!"),
            @ApiResponse(responseCode = "404", description = "Livro nao encontrado!"),
            @ApiResponse(responseCode = "409", description = "Livro ja cadastrado!"),
    })
    public ResponseEntity<Object> atualizar(@PathVariable("id") String id, @RequestBody @Valid LivroDTO livroDTO) {
        return livroService.buscarLivroPorId(UUID.fromString(id))
                .map(livro -> {
                    Livro entityAux = livroMapper.toEntity(livroDTO);
                    livro.setDataPublicacao(entityAux.getDataPublicacao());
                    livro.setIsbn(entityAux.getIsbn());
                    livro.setPreco(entityAux.getPreco());
                    livro.setGenero(entityAux.getGenero());
                    livro.setTitulo(entityAux.getTitulo());
                    livro.setAutor(entityAux.getAutor());

                    livroService.atualizarLivro(livro);

                    return ResponseEntity.noContent().build();

                }).orElseGet( () -> ResponseEntity.notFound().build() );
    }
}
