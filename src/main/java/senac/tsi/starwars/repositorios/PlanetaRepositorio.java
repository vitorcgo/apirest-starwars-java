package senac.tsi.starwars.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import senac.tsi.starwars.entidades.Planeta;

@Repository
public interface PlanetaRepositorio extends JpaRepository<Planeta, Long> {
}
