package senac.tsi.starwars.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import senac.tsi.starwars.entidades.Nave;

@Repository
public interface NaveRepositorio extends JpaRepository<Nave, Long> {
}
