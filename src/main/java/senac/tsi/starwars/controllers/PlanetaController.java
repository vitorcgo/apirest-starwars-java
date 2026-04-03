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
import senac.tsi.starwars.entidades.Planeta;
import senac.tsi.starwars.excecoes.RecursoNaoEncontradoException;
import senac.tsi.starwars.repositorios.PlanetaRepositorio;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Tag(name = "planetas", description = "Rota de Planetas")
@RestController
public class PlanetaController {

    private final PlanetaRepositorio planetaRepositorio;
    private final PagedResourcesAssembler<Planeta> pagedResourcesAssembler;

    @Autowired
    public PlanetaController(PlanetaRepositorio planetaRepositorio,
                             PagedResourcesAssembler<Planeta> pagedResourcesAssembler) {
        this.planetaRepositorio = planetaRepositorio;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @Operation(summary = "Listar todos os planetas", description = "Retorna todos os planetas cadastrados, paginados.")
    @GetMapping("/planetas")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PagedModel<EntityModel<Planeta>>> listarPlanetas(@ParameterObject Pageable pageable) {
        var planetas = planetaRepositorio.findAll(pageable);
        PagedModel<EntityModel<Planeta>> pagedModel = pagedResourcesAssembler.toModel(planetas);
        return ResponseEntity.ok(pagedModel);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Planeta encontrado",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Planeta.class)) }),
            @ApiResponse(responseCode = "404", description = "Planeta não encontrado",
                    content = @Content) })
    @GetMapping("/planetas/{id}")
    public EntityModel<Planeta> buscarPlanetaPorId(@PathVariable Long id) {
        var planeta = planetaRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("planeta", id));

        return EntityModel.of(planeta,
                linkTo(methodOn(PlanetaController.class).buscarPlanetaPorId(id)).withSelfRel(),
                linkTo(methodOn(PlanetaController.class).listarPlanetas(Pageable.unpaged())).withRel("planetas"));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Planeta criado com sucesso",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Planeta.class)) }),
            @ApiResponse(responseCode = "400", description = "Dados inválidos") })
    @PostMapping("/planetas")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Planeta> criarPlaneta(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Planeta a ser criado", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Planeta.class),
                            examples = @ExampleObject(value = "{ \"nome\": \"Hoth\", \"clima\": \"Ártico\", \"terreno\": \"Neve e gelo\" }")))
            @RequestBody Planeta novoPlaneta) {
        planetaRepositorio.save(novoPlaneta);
        return ResponseEntity.created(URI.create("/planetas/" + novoPlaneta.getId()))
                .body(novoPlaneta);
    }

    @PutMapping("/planetas/{id}")
    public ResponseEntity<Planeta> atualizarPlaneta(@PathVariable Long id,
                                                    @RequestBody Planeta planetaAtualizado) {
        return planetaRepositorio.findById(id).map(planeta -> {
            planeta.setNome(planetaAtualizado.getNome());
            planeta.setClima(planetaAtualizado.getClima());
            planeta.setTerreno(planetaAtualizado.getTerreno());
            return ResponseEntity.ok(planetaRepositorio.save(planeta));
        }).orElseGet(() -> {
            return ResponseEntity.created(URI.create("/planetas/" + planetaAtualizado.getId()))
                    .body(planetaRepositorio.save(planetaAtualizado));
        });
    }

    @DeleteMapping("/planetas/{id}")
    public ResponseEntity<Void> deletarPlaneta(@PathVariable Long id) {
        var planeta = planetaRepositorio.findById(id).orElse(null);
        if (planeta == null)
            return ResponseEntity.notFound().build();

        planetaRepositorio.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
