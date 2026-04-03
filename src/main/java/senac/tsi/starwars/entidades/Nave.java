package senac.tsi.starwars.entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Objects;

@Entity
public class Nave {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @NotBlank
    @Size(min = 1, max = 150)
    private String nome;

    @NotNull
    @NotBlank
    @Size(min = 1, max = 150)
    private String modelo;

    @NotNull
    @NotBlank
    @Size(min = 1, max = 150)
    private String fabricante;

    // Relacionamento 3: Nave tem um Personagem como piloto principal
    @ManyToOne
    @JoinColumn(name = "piloto_id")
    private Personagem piloto;

    public Nave() {
    }

    public Nave(String nome, String modelo, String fabricante, Personagem piloto) {
        this.nome = nome;
        this.modelo = modelo;
        this.fabricante = fabricante;
        this.piloto = piloto;
    }

    public Nave(Long id, String nome, String modelo, String fabricante, Personagem piloto) {
        this.id = id;
        this.nome = nome;
        this.modelo = modelo;
        this.fabricante = fabricante;
        this.piloto = piloto;
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

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    public Personagem getPiloto() {
        return piloto;
    }

    public void setPiloto(Personagem piloto) {
        this.piloto = piloto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Nave nave = (Nave) o;
        return Objects.equals(id, nave.id) &&
                Objects.equals(nome, nave.nome) &&
                Objects.equals(modelo, nave.modelo) &&
                Objects.equals(fabricante, nave.fabricante);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, modelo, fabricante);
    }

    @Override
    public String toString() {
        return "Nave{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", modelo='" + modelo + '\'' +
                ", fabricante='" + fabricante + '\'' +
                '}';
    }
}
