package senac.tsi.starwars.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import senac.tsi.starwars.entidades.Personagem;

@Repository
public interface PersonagemRepositorio extends JpaRepository<Personagem, Long> {
}
