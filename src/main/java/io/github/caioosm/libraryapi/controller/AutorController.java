package io.github.caioosm.libraryapi.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
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

import io.github.caioosm.libraryapi.controller.dto.AutorDTO;
import io.github.caioosm.libraryapi.controller.mappers.AutorMapper;
import io.github.caioosm.libraryapi.model.Autor;
import io.github.caioosm.libraryapi.services.AutorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

// http://localhost:8080/autores
@RestController
@RequestMapping("/autores")
@RequiredArgsConstructor
@Tag(name = "Autores")
@Slf4j
public class AutorController implements GenericController {

    private final AutorMapper autorMapper;
    private final AutorService autorService;
    
    @PostMapping
    @PreAuthorize("hasRole('GERENTE')")
    @Operation(summary = "Cadastrar Autor", description = "Cadastrar novo Autor")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cadastrado com sucesso!"),
            @ApiResponse(responseCode = "422", description = "Erro de validacao!"),
            @ApiResponse(responseCode = "409", description = "Autor duplicado/ja cadastrado!")
    })
    public ResponseEntity<Void> save(@RequestBody @Valid AutorDTO autorDTO) {
        Autor autor = autorMapper.toEntity(autorDTO);
        autorService.salvar(autor);
        // http://localhost:8080/autores/id
        URI location = gerarHeaderLocation(autor.getId());
        return ResponseEntity.created(location).build();
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    @Operation(summary = "Obter detalhes", description = "Retorna os dados do autor pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Autor encontrado!"),
            @ApiResponse(responseCode = "404", description = "Not Found!"),
    })
    public ResponseEntity<AutorDTO> obterDetalhes(@PathVariable("id") String id) {
        var idAutor = UUID.fromString(id);
        @SuppressWarnings("unused")
        Optional<Autor> autorOptional = autorService.buscarPorId(idAutor);

        return autorService
                .buscarPorId(idAutor)
                .map(autor -> {
                    AutorDTO dto = autorMapper.toDto(autor);
                    return ResponseEntity.ok(dto);
                }).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('GERENTE')")
    @Operation(summary = "Deletar Autor", description = "Remover autor informando o ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Autor Deletado com sucesso!"),
            @ApiResponse(responseCode = "404", description = "Autor nao encontrado!"),
            @ApiResponse(responseCode = "400", description = "Autor possui livro cadastrado!"),
    })
    public ResponseEntity<Void> remover(@PathVariable("id") String id) {
        var idAutor = UUID.fromString(id);
        Optional<Autor> autorOptional = autorService.buscarPorId(idAutor);
        if (autorOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        autorService.deletar(autorOptional.get());
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    @Operation(summary = "Pesquisa de autores por parametro", description = "Obtem autores por parametros")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sucesso!"),
    })
    public ResponseEntity<List<AutorDTO>> pesquisar(
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "nacionalidade", required = false) String nacionalidade) {

        log.trace("Pesquisando autores por parametros");
        log.debug("Pesquisando autores por parametros");
        log.info("Pesquisando autores por parametros");
        log.warn("Pesquisando autores por parametros");
        log.error("Pesquisando autores por parametros");
        List<Autor> resultado = autorService.pesquisaByExample(nome, nacionalidade);
        List<AutorDTO> resultadoDTO = resultado
                .stream()
                .map(autorMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(resultadoDTO);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('GERENTE')")
    @Operation(summary = "Atualizar autor", description = "Metodo para atualizar um autor existente")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Autor Atualizado com sucesso!"),
            @ApiResponse(responseCode = "404", description = "Autor nao encontrado!"),
            @ApiResponse(responseCode = "409", description = "Autor ja cadastrado!"),
    })
    public ResponseEntity<Void> atualizar(@RequestBody @Valid AutorDTO autorDTO, @PathVariable("id") String id) {
        var idAutor = UUID.fromString(id);
        Optional<Autor> autorOptional = autorService.buscarPorId(idAutor);
        if (autorOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var autor = autorOptional.get();
        autor.setNome(autorDTO.nome());
        autor.setNacionalidade(autorDTO.nacionalidade());
        autor.setDataNascimento(autorDTO.dataNascimento());
        autorService.atualizar(autor);
        return ResponseEntity.noContent().build();
    }
}
