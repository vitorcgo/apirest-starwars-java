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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import senac.tsi.starwars.entidades.Especie;
import senac.tsi.starwars.excecoes.RecursoNaoEncontradoException;
import senac.tsi.starwars.repositorios.EspecieRepositorio;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Tag(name = "especies", description = "Rota de Espécies")
@RestController
public class EspecieController {

    private final EspecieRepositorio especieRepositorio;
    private final PagedResourcesAssembler<Especie> pagedResourcesAssembler;

    @Autowired
    public EspecieController(EspecieRepositorio especieRepositorio,
                             PagedResourcesAssembler<Especie> pagedResourcesAssembler) {
        this.especieRepositorio = especieRepositorio;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @Operation(summary = "Listar todas as espécies", description = "Retorna todas as espécies cadastradas, paginadas.")
    @GetMapping("/especies")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PagedModel<EntityModel<Especie>>> listarEspecies(@ParameterObject Pageable pageable) {
        var especies = especieRepositorio.findAll(pageable);
        PagedModel<EntityModel<Especie>> pagedModel = pagedResourcesAssembler.toModel(especies);
        return ResponseEntity.ok(pagedModel);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Espécie encontrada",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Especie.class)) }),
            @ApiResponse(responseCode = "404", description = "Espécie não encontrada",
                    content = @Content) })
    @GetMapping("/especies/{id}")
    public EntityModel<Especie> buscarEspeciePorId(@PathVariable Long id) {
        var especie = especieRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("espécie", id));

        return EntityModel.of(especie,
                linkTo(methodOn(EspecieController.class).buscarEspeciePorId(id)).withSelfRel(),
                linkTo(methodOn(EspecieController.class).listarEspecies(Pageable.unpaged())).withRel("especies"));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Espécie criada com sucesso",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Especie.class)) }),
            @ApiResponse(responseCode = "400", description = "Dados inválidos") })
    @PostMapping("/especies")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Especie> criarEspecie(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Espécie a ser criada", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Especie.class),
                            examples = @ExampleObject(value = "{ \"nome\": \"Rodiano\", \"classificacao\": \"Humanóide\", \"linguagem\": \"Rodês\" }")))
            @RequestBody Especie novaEspecie) {
        especieRepositorio.save(novaEspecie);
        return ResponseEntity.created(URI.create("/especies/" + novaEspecie.getId()))
                .body(novaEspecie);
    }

    @PutMapping("/especies/{id}")
    public ResponseEntity<Especie> atualizarEspecie(@PathVariable Long id,
                                                    @RequestBody Especie especieAtualizada) {
        return especieRepositorio.findById(id).map(especie -> {
            especie.setNome(especieAtualizada.getNome());
            especie.setClassificacao(especieAtualizada.getClassificacao());
            especie.setLinguagem(especieAtualizada.getLinguagem());
            return ResponseEntity.ok(especieRepositorio.save(especie));
        }).orElseGet(() -> {
            return ResponseEntity.created(URI.create("/especies/" + especieAtualizada.getId()))
                    .body(especieRepositorio.save(especieAtualizada));
        });
    }

    @DeleteMapping("/especies/{id}")
    public ResponseEntity<Void> deletarEspecie(@PathVariable Long id) {
        return especieRepositorio.findById(id).map(especie -> {
            try {
                especieRepositorio.delete(especie);
                return ResponseEntity.noContent().build();
            } catch (DataIntegrityViolationException e) {
                // Retorna conflito caso exista FK que impeça exclusão
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
        }).orElse(ResponseEntity.notFound().build());
    }
}
