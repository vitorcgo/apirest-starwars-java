package senac.tsi.starwars.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import senac.tsi.starwars.entidades.Filme;

@Repository
public interface FilmeRepositorio extends JpaRepository<Filme, Long> {
}
