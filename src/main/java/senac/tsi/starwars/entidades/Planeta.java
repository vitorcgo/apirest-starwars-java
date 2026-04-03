package senac.tsi.starwars.entidades;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Objects;

@Entity
public class Planeta {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @NotBlank
    @Size(min = 1, max = 100)
    private String nome;

    @NotNull
    @NotBlank
    @Size(min = 1, max = 100)
    private String clima;

    @NotNull
    @NotBlank
    @Size(min = 1, max = 100)
    private String terreno;

    public Planeta() {
    }

    public Planeta(String nome, String clima, String terreno) {
        this.nome = nome;
        this.clima = clima;
        this.terreno = terreno;
    }

    public Planeta(Long id, String nome, String clima, String terreno) {
        this.id = id;
        this.nome = nome;
        this.clima = clima;
        this.terreno = terreno;
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

    public String getClima() {
        return clima;
    }

    public void setClima(String clima) {
        this.clima = clima;
    }

    public String getTerreno() {
        return terreno;
    }

    public void setTerreno(String terreno) {
        this.terreno = terreno;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Planeta planeta = (Planeta) o;
        return Objects.equals(id, planeta.id) &&
                Objects.equals(nome, planeta.nome) &&
                Objects.equals(clima, planeta.clima) &&
                Objects.equals(terreno, planeta.terreno);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, clima, terreno);
    }

    @Override
    public String toString() {
        return "Planeta{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", clima='" + clima + '\'' +
                ", terreno='" + terreno + '\'' +
                '}';
    }
}
