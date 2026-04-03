package senac.tsi.starwars.entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Objects;

@Entity
public class Personagem {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @NotBlank
    @Size(min = 1, max = 150)
    private String nome;

    @NotNull
    @NotBlank
    @Size(min = 1, max = 50)
    private String genero;

    // Relacionamento 1: Personagem tem um Planeta natal
    @ManyToOne
    @JoinColumn(name = "planeta_id")
    private Planeta planetaNatal;

    // Relacionamento 2: Personagem pertence a uma Especie
    @ManyToOne
    @JoinColumn(name = "especie_id")
    private Especie especie;

    public Personagem() {
    }

    public Personagem(String nome, String genero, Planeta planetaNatal, Especie especie) {
        this.nome = nome;
        this.genero = genero;
        this.planetaNatal = planetaNatal;
        this.especie = especie;
    }

    public Personagem(Long id, String nome, String genero, Planeta planetaNatal, Especie especie) {
        this.id = id;
        this.nome = nome;
        this.genero = genero;
        this.planetaNatal = planetaNatal;
        this.especie = especie;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public Planeta getPlanetaNatal() {
        return planetaNatal;
    }

    public void setPlanetaNatal(Planeta planetaNatal) {
        this.planetaNatal = planetaNatal;
    }

    public Especie getEspecie() {
        return especie;
    }

    public void setEspecie(Especie especie) {
        this.especie = especie;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Personagem that = (Personagem) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(nome, that.nome) &&
                Objects.equals(genero, that.genero);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, genero);
    }

    @Override
    public String toString() {
        return "Personagem{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", genero='" + genero + '\'' +
                '}';
    }
}
