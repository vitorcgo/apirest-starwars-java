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
import senac.tsi.starwars.entidades.Nave;
import senac.tsi.starwars.excecoes.RecursoNaoEncontradoException;
import senac.tsi.starwars.repositorios.NaveRepositorio;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Tag(name = "naves", description = "Rota de Naves")
@RestController
public class NaveController {

    private final NaveRepositorio naveRepositorio;
    private final PagedResourcesAssembler<Nave> pagedResourcesAssembler;

    @Autowired
    public NaveController(NaveRepositorio naveRepositorio,
                          PagedResourcesAssembler<Nave> pagedResourcesAssembler) {
        this.naveRepositorio = naveRepositorio;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @Operation(summary = "Listar todas as naves", description = "Retorna todas as naves cadastradas, paginadas.")
    @GetMapping("/naves")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PagedModel<EntityModel<Nave>>> listarNaves(@ParameterObject Pageable pageable) {
        var naves = naveRepositorio.findAll(pageable);
        PagedModel<EntityModel<Nave>> pagedModel = pagedResourcesAssembler.toModel(naves);
        return ResponseEntity.ok(pagedModel);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Nave encontrada",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Nave.class)) }),
            @ApiResponse(responseCode = "404", description = "Nave não encontrada",
                    content = @Content) })
    @GetMapping("/naves/{id}")
    public EntityModel<Nave> buscarNavePorId(@PathVariable Long id) {
        var nave = naveRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("nave", id));

        return EntityModel.of(nave,
                linkTo(methodOn(NaveController.class).buscarNavePorId(id)).withSelfRel(),
                linkTo(methodOn(NaveController.class).listarNaves(Pageable.unpaged())).withRel("naves"));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Nave criada com sucesso",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Nave.class)) }),
            @ApiResponse(responseCode = "400", description = "Dados inválidos") })
    @PostMapping("/naves")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Nave> criarNave(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Nave a ser criada. Informe o id do piloto.", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Nave.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "nome": "Slave I",
                                      "modelo": "Firespray-31",
                                      "fabricante": "Kuat Systems Engineering",
                                      "piloto": { "id": 1 }
                                    }
                                    """)))
            @RequestBody Nave novaNave) {
        naveRepositorio.save(novaNave);
        return ResponseEntity.created(URI.create("/naves/" + novaNave.getId()))
                .body(novaNave);
    }

    @PutMapping("/naves/{id}")
    public ResponseEntity<Nave> atualizarNave(@PathVariable Long id,
                                              @RequestBody Nave naveAtualizada) {
        return naveRepositorio.findById(id).map(nave -> {
            nave.setNome(naveAtualizada.getNome());
            nave.setModelo(naveAtualizada.getModelo());
            nave.setFabricante(naveAtualizada.getFabricante());
            nave.setPiloto(naveAtualizada.getPiloto());
            return ResponseEntity.ok(naveRepositorio.save(nave));
        }).orElseGet(() -> {
            return ResponseEntity.created(URI.create("/naves/" + naveAtualizada.getId()))
                    .body(naveRepositorio.save(naveAtualizada));
        });
    }

    @DeleteMapping("/naves/{id}")
    public ResponseEntity<Void> deletarNave(@PathVariable Long id) {
        var nave = naveRepositorio.findById(id).orElse(null);
        if (nave == null)
            return ResponseEntity.notFound().build();

        naveRepositorio.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
