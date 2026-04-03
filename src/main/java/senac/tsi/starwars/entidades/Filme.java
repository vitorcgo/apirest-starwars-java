package senac.tsi.starwars.entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Filme {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @NotBlank
    @Size(min = 1, max = 255)
    private String titulo;

    @NotNull
    private Integer episodio;

    @NotNull
    @NotBlank
    @Size(min = 1, max = 150)
    private String diretor;

    @NotNull
    private Integer anoLancamento;

    // Relacionamento 4: Filme tem muitos Personagens (e um Personagem aparece em muitos Filmes)
    @ManyToMany
    @JoinTable(
            name = "filme_personagem",
            joinColumns = @JoinColumn(name = "filme_id"),
            inverseJoinColumns = @JoinColumn(name = "personagem_id")
    )
    private List<Personagem> personagens = new ArrayList<>();

    // Relacionamento 5: Filme tem muitas Naves (e uma Nave aparece em muitos Filmes)
    @ManyToMany
    @JoinTable(
            name = "filme_nave",
            joinColumns = @JoinColumn(name = "filme_id"),
            inverseJoinColumns = @JoinColumn(name = "nave_id")
    )
    private List<Nave> naves = new ArrayList<>();

    public Filme() {
    }

    public Filme(String titulo, Integer episodio, String diretor, Integer anoLancamento) {
        this.titulo = titulo;
        this.episodio = episodio;
        this.diretor = diretor;
        this.anoLancamento = anoLancamento;
    }

    public Filme(Long id, String titulo, Integer episodio, String diretor, Integer anoLancamento) {
        this.id = id;
        this.titulo = titulo;
        this.episodio = episodio;
        this.diretor = diretor;
        this.anoLancamento = anoLancamento;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getEpisodio() {
        return episodio;
    }

    public void setEpisodio(Integer episodio) {
        this.episodio = episodio;
    }

    public String getDiretor() {
        return diretor;
    }

    public void setDiretor(String diretor) {
        this.diretor = diretor;
    }

    public Integer getAnoLancamento() {
        return anoLancamento;
    }

    public void setAnoLancamento(Integer anoLancamento) {
        this.anoLancamento = anoLancamento;
    }

    public List<Personagem> getPersonagens() {
        return personagens;
    }

    public void setPersonagens(List<Personagem> personagens) {
        this.personagens = personagens;
    }

    public List<Nave> getNaves() {
        return naves;
    }

    public void setNaves(List<Nave> naves) {
        this.naves = naves;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Filme filme = (Filme) o;
        return Objects.equals(id, filme.id) &&
                Objects.equals(titulo, filme.titulo) &&
                Objects.equals(episodio, filme.episodio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, titulo, episodio);
    }

    @Override
    public String toString() {
        return "Filme{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", episodio=" + episodio +
                ", diretor='" + diretor + '\'' +
                ", anoLancamento=" + anoLancamento +
                '}';
    }
}
