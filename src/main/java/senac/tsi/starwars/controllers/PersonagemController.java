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
import senac.tsi.starwars.entidades.Personagem;
import senac.tsi.starwars.excecoes.RecursoNaoEncontradoException;
import senac.tsi.starwars.repositorios.PersonagemRepositorio;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Tag(name = "personagens", description = "Rota de Personagens")
@RestController
public class PersonagemController {

    private final PersonagemRepositorio personagemRepositorio;
    private final PagedResourcesAssembler<Personagem> pagedResourcesAssembler;

    @Autowired
    public PersonagemController(PersonagemRepositorio personagemRepositorio,
                                PagedResourcesAssembler<Personagem> pagedResourcesAssembler) {
        this.personagemRepositorio = personagemRepositorio;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @Operation(summary = "Listar todos os personagens", description = "Retorna todos os personagens cadastrados, paginados.")
    @GetMapping("/personagens")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PagedModel<EntityModel<Personagem>>> listarPersonagens(@ParameterObject Pageable pageable) {
        var personagens = personagemRepositorio.findAll(pageable);
        PagedModel<EntityModel<Personagem>> pagedModel = pagedResourcesAssembler.toModel(personagens);
        return ResponseEntity.ok(pagedModel);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Personagem encontrado",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Personagem.class)) }),
            @ApiResponse(responseCode = "404", description = "Personagem não encontrado",
                    content = @Content) })
    @GetMapping("/personagens/{id}")
    public EntityModel<Personagem> buscarPersonagemPorId(@PathVariable Long id) {
        var personagem = personagemRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("personagem", id));

        return EntityModel.of(personagem,
                linkTo(methodOn(PersonagemController.class).buscarPersonagemPorId(id)).withSelfRel(),
                linkTo(methodOn(PersonagemController.class).listarPersonagens(Pageable.unpaged())).withRel("personagens"));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Personagem criado com sucesso",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Personagem.class)) }),
            @ApiResponse(responseCode = "400", description = "Dados inválidos") })
    @PostMapping("/personagens")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Personagem> criarPersonagem(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Personagem a ser criado. Informe o id do planeta e da espécie.", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Personagem.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "nome": "Obi-Wan Kenobi",
                                      "genero": "Masculino",
                                      "planetaNatal": { "id": 1 },
                                      "especie": { "id": 1 }
                                    }
                                    """)))
            @RequestBody Personagem novoPersonagem) {
        personagemRepositorio.save(novoPersonagem);
        return ResponseEntity.created(URI.create("/personagens/" + novoPersonagem.getId()))
                .body(novoPersonagem);
    }

    @PutMapping("/personagens/{id}")
    public ResponseEntity<Personagem> atualizarPersonagem(@PathVariable Long id,
                                                          @RequestBody Personagem personagemAtualizado) {
        return personagemRepositorio.findById(id).map(personagem -> {
            personagem.setNome(personagemAtualizado.getNome());
            personagem.setGenero(personagemAtualizado.getGenero());
            personagem.setPlanetaNatal(personagemAtualizado.getPlanetaNatal());
            personagem.setEspecie(personagemAtualizado.getEspecie());
            return ResponseEntity.ok(personagemRepositorio.save(personagem));
        }).orElseGet(() -> {
            return ResponseEntity.created(URI.create("/personagens/" + personagemAtualizado.getId()))
                    .body(personagemRepositorio.save(personagemAtualizado));
        });
    }

    @DeleteMapping("/personagens/{id}")
    public ResponseEntity<Void> deletarPersonagem(@PathVariable Long id) {
        var personagem = personagemRepositorio.findById(id).orElse(null);
        if (personagem == null)
            return ResponseEntity.notFound().build();

        personagemRepositorio.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
