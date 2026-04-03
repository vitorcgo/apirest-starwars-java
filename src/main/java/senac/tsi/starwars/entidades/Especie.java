package senac.tsi.starwars.entidades;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Objects;

@Entity
public class Especie {

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
    private String classificacao;

    @NotNull
    @NotBlank
    @Size(min = 1, max = 100)
    private String linguagem;

    public Especie() {
    }

    public Especie(String nome, String classificacao, String linguagem) {
        this.nome = nome;
        this.classificacao = classificacao;
        this.linguagem = linguagem;
    }

    public Especie(Long id, String nome, String classificacao, String linguagem) {
        this.id = id;
        this.nome = nome;
        this.classificacao = classificacao;
        this.linguagem = linguagem;
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

    public String getClassificacao() {
        return classificacao;
    }

    public void setClassificacao(String classificacao) {
        this.classificacao = classificacao;
    }

    public String getLinguagem() {
        return linguagem;
    }

    public void setLinguagem(String linguagem) {
        this.linguagem = linguagem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Especie especie = (Especie) o;
        return Objects.equals(id, especie.id) &&
                Objects.equals(nome, especie.nome) &&
                Objects.equals(classificacao, especie.classificacao) &&
                Objects.equals(linguagem, especie.linguagem);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, classificacao, linguagem);
    }

    @Override
    public String toString() {
        return "Especie{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", classificacao='" + classificacao + '\'' +
                ", linguagem='" + linguagem + '\'' +
                '}';
    }
}
