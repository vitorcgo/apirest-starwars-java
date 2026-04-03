package senac.tsi.starwars.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import senac.tsi.starwars.entidades.Especie;

@Repository
public interface EspecieRepositorio extends JpaRepository<Especie, Long> {
}
