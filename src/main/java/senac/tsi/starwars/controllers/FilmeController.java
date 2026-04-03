package senac.tsi.starwars.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import senac.tsi.starwars.entidades.Filme;
import senac.tsi.starwars.excecoes.RecursoNaoEncontradoException;
import senac.tsi.starwars.repositorios.FilmeRepositorio;
import senac.tsi.starwars.repositorios.NaveRepositorio;
import senac.tsi.starwars.repositorios.PersonagemRepositorio;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Tag(name = "filmes", description = "Rota de Filmes")
@RestController
public class FilmeController {

    private final FilmeRepositorio filmeRepositorio;
    private final PersonagemRepositorio personagemRepositorio;
    private final NaveRepositorio naveRepositorio;
    private final PagedResourcesAssembler<Filme> pagedResourcesAssembler;

    @Autowired
    public FilmeController(FilmeRepositorio filmeRepositorio,
                           PersonagemRepositorio personagemRepositorio,
                           NaveRepositorio naveRepositorio,
                           PagedResourcesAssembler<Filme> pagedResourcesAssembler) {
        this.filmeRepositorio = filmeRepositorio;
        this.personagemRepositorio = personagemRepositorio;
        this.naveRepositorio = naveRepositorio;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @Operation(summary = "Listar todos os filmes", description = "Retorna todos os filmes cadastrados, paginados.")
    @GetMapping("/filmes")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PagedModel<EntityModel<Filme>>> listarFilmes(@ParameterObject Pageable pageable) {
        var filmes = filmeRepositorio.findAll(pageable);
        PagedModel<EntityModel<Filme>> pagedModel = pagedResourcesAssembler.toModel(filmes);
        return ResponseEntity.ok(pagedModel);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Filme encontrado",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Filme.class)) }),
            @ApiResponse(responseCode = "404", description = "Filme não encontrado",
                    content = @Content) })
    @GetMapping("/filmes/{id}")
    public EntityModel<Filme> buscarFilmePorId(@PathVariable Long id) {
        var filme = filmeRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("filme", id));

        return EntityModel.of(filme,
                linkTo(methodOn(FilmeController.class).buscarFilmePorId(id)).withSelfRel(),
                linkTo(methodOn(FilmeController.class).listarFilmes(Pageable.unpaged())).withRel("filmes"));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Filme criado com sucesso",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Filme.class)) }),
            @ApiResponse(responseCode = "400", description = "Dados inválidos") })
    @PostMapping("/filmes")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Filme> criarFilme(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Filme a ser criado.", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Filme.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "titulo": "A Ameaça Fantasma",
                                      "episodio": 1,
                                      "diretor": "George Lucas",
                                      "anoLancamento": 1999
                                    }
                                    """)))
            @RequestBody Filme novoFilme) {
        filmeRepositorio.save(novoFilme);
        return ResponseEntity.created(URI.create("/filmes/" + novoFilme.getId()))
                .body(novoFilme);
    }

    @PutMapping("/filmes/{id}")
    public ResponseEntity<Filme> atualizarFilme(@PathVariable Long id,
                                                @RequestBody Filme filmeAtualizado) {
        return filmeRepositorio.findById(id).map(filme -> {
            filme.setTitulo(filmeAtualizado.getTitulo());
            filme.setEpisodio(filmeAtualizado.getEpisodio());
            filme.setDiretor(filmeAtualizado.getDiretor());
            filme.setAnoLancamento(filmeAtualizado.getAnoLancamento());
            return ResponseEntity.ok(filmeRepositorio.save(filme));
        }).orElseGet(() -> {
            return ResponseEntity.created(URI.create("/filmes/" + filmeAtualizado.getId()))
                    .body(filmeRepositorio.save(filmeAtualizado));
        });
    }

    @DeleteMapping("/filmes/{id}")
    public ResponseEntity<Void> deletarFilme(@PathVariable Long id) {
        var filme = filmeRepositorio.findById(id).orElse(null);
        if (filme == null)
            return ResponseEntity.notFound().build();

        filmeRepositorio.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // --- Gerenciamento dos relacionamentos ManyToMany ---

    @Operation(summary = "Adicionar personagem ao filme")
    @PostMapping("/filmes/{filmeId}/personagens/{personagemId}")
    public ResponseEntity<Filme> adicionarPersonagem(@PathVariable Long filmeId,
                                                     @PathVariable Long personagemId) {
        var filme = filmeRepositorio.findById(filmeId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("filme", filmeId));

        var personagem = personagemRepositorio.findById(personagemId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("personagem", personagemId));

        filme.getPersonagens().add(personagem);
        return ResponseEntity.ok(filmeRepositorio.save(filme));
    }

    @Operation(summary = "Remover personagem do filme")
    @DeleteMapping("/filmes/{filmeId}/personagens/{personagemId}")
    public ResponseEntity<Filme> removerPersonagem(@PathVariable Long filmeId,
                                                   @PathVariable Long personagemId) {
        var filme = filmeRepositorio.findById(filmeId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("filme", filmeId));

        filme.getPersonagens().removeIf(p -> p.getId().equals(personagemId));
        return ResponseEntity.ok(filmeRepositorio.save(filme));
    }

    @Operation(summary = "Adicionar nave ao filme")
    @PostMapping("/filmes/{filmeId}/naves/{naveId}")
    public ResponseEntity<Filme> adicionarNave(@PathVariable Long filmeId,
                                               @PathVariable Long naveId) {
        var filme = filmeRepositorio.findById(filmeId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("filme", filmeId));

        var nave = naveRepositorio.findById(naveId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("nave", naveId));

        filme.getNaves().add(nave);
        return ResponseEntity.ok(filmeRepositorio.save(filme));
    }

    @Operation(summary = "Remover nave do filme")
    @DeleteMapping("/filmes/{filmeId}/naves/{naveId}")
    public ResponseEntity<Filme> removerNave(@PathVariable Long filmeId,
                                             @PathVariable Long naveId) {
        var filme = filmeRepositorio.findById(filmeId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("filme", filmeId));

        filme.getNaves().removeIf(n -> n.getId().equals(naveId));
        return ResponseEntity.ok(filmeRepositorio.save(filme));
    }
}
