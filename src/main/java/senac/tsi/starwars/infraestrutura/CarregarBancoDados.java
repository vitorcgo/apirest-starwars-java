package senac.tsi.starwars.infraestrutura;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import senac.tsi.starwars.entidades.*;
import senac.tsi.starwars.repositorios.*;

@Configuration
public class CarregarBancoDados {

    private static final Logger log = LoggerFactory.getLogger(CarregarBancoDados.class);

    @Bean
    CommandLineRunner iniciarBancoDados(
            PlanetaRepositorio planetaRepositorio,
            EspecieRepositorio especieRepositorio,
            PersonagemRepositorio personagemRepositorio,
            NaveRepositorio naveRepositorio,
            FilmeRepositorio filmeRepositorio
    ) {
        return args -> {

            // --- Planetas ---
            Planeta tatooine = planetaRepositorio.save(new Planeta("Tatooine", "Árido", "Deserto"));
            log.info("Preloading " + tatooine);

            Planeta alderaan = planetaRepositorio.save(new Planeta("Alderaan", "Temperado", "Planícies"));
            log.info("Preloading " + alderaan);

            Planeta dagobah = planetaRepositorio.save(new Planeta("Dagobah", "Murky", "Pântano"));
            log.info("Preloading " + dagobah);

            Planeta coruscant = planetaRepositorio.save(new Planeta("Coruscant", "Temperado", "Metrópole urbana"));
            log.info("Preloading " + coruscant);

            Planeta naboo = planetaRepositorio.save(new Planeta("Naboo", "Temperado", "Colinas e campos"));
            log.info("Preloading " + naboo);

            // --- Espécies ---
            Especie humano = especieRepositorio.save(new Especie("Humano", "Mamífero sentiente", "Galáctico básico"));
            log.info("Preloading " + humano);

            Especie wookiee = especieRepositorio.save(new Especie("Wookiee", "Mamífero sentiente", "Shyriiwook"));
            log.info("Preloading " + wookiee);

            Especie yodasEspecie = especieRepositorio.save(new Especie("Espécie de Yoda", "Desconhecida", "Galáctico básico"));
            log.info("Preloading " + yodasEspecie);

            Especie twi_lek = especieRepositorio.save(new Especie("Twi'lek", "Humanóide", "Twi'leki"));
            log.info("Preloading " + twi_lek);

            Especie droid = especieRepositorio.save(new Especie("Droide", "Robô sentiente", "Binário"));
            log.info("Preloading " + droid);

            // --- Personagens ---
            Personagem luke = personagemRepositorio.save(new Personagem("Luke Skywalker", "Masculino", tatooine, humano));
            log.info("Preloading " + luke);

            Personagem leia = personagemRepositorio.save(new Personagem("Leia Organa", "Feminino", alderaan, humano));
            log.info("Preloading " + leia);

            Personagem han = personagemRepositorio.save(new Personagem("Han Solo", "Masculino", coruscant, humano));
            log.info("Preloading " + han);

            Personagem vader = personagemRepositorio.save(new Personagem("Darth Vader", "Masculino", tatooine, humano));
            log.info("Preloading " + vader);

            Personagem yoda = personagemRepositorio.save(new Personagem("Yoda", "Masculino", dagobah, yodasEspecie));
            log.info("Preloading " + yoda);

            Personagem chewie = personagemRepositorio.save(new Personagem("Chewbacca", "Masculino", naboo, wookiee));
            log.info("Preloading " + chewie);

            Personagem r2d2 = personagemRepositorio.save(new Personagem("R2-D2", "Nenhum", naboo, droid));
            log.info("Preloading " + r2d2);

            // --- Naves ---
            Nave millenniumFalcon = naveRepositorio.save(new Nave("Millennium Falcon", "YT-1300f", "Corellian Engineering", han));
            log.info("Preloading " + millenniumFalcon);

            Nave xWing = naveRepositorio.save(new Nave("X-Wing", "T-65B X-wing", "Incom Corporation", luke));
            log.info("Preloading " + xWing);

            Nave tieFighter = naveRepositorio.save(new Nave("TIE Fighter", "TIE/LN", "Sienar Fleet Systems", vader));
            log.info("Preloading " + tieFighter);

            Nave starDestroyer = naveRepositorio.save(new Nave("Star Destroyer", "Imperial I-class", "Kuat Drive Yards", vader));
            log.info("Preloading " + starDestroyer);

            Nave podracer = naveRepositorio.save(new Nave("Naboo Royal Starship", "J-type 327", "Theed Palace Space Vessel", leia));
            log.info("Preloading " + podracer);

            // --- Filmes ---
            Filme ep4 = new Filme("Uma Nova Esperança", 4, "George Lucas", 1977);
            ep4.getPersonagens().add(luke);
            ep4.getPersonagens().add(leia);
            ep4.getPersonagens().add(han);
            ep4.getPersonagens().add(vader);
            ep4.getPersonagens().add(chewie);
            ep4.getNaves().add(millenniumFalcon);
            ep4.getNaves().add(xWing);
            ep4.getNaves().add(tieFighter);
            filmeRepositorio.save(ep4);
            log.info("Preloading " + ep4);

            Filme ep5 = new Filme("O Império Contra-Ataca", 5, "Irvin Kershner", 1980);
            ep5.getPersonagens().add(luke);
            ep5.getPersonagens().add(leia);
            ep5.getPersonagens().add(han);
            ep5.getPersonagens().add(vader);
            ep5.getPersonagens().add(yoda);
            ep5.getPersonagens().add(chewie);
            ep5.getNaves().add(millenniumFalcon);
            ep5.getNaves().add(starDestroyer);
            filmeRepositorio.save(ep5);
            log.info("Preloading " + ep5);

            Filme ep6 = new Filme("O Retorno de Jedi", 6, "Richard Marquand", 1983);
            ep6.getPersonagens().add(luke);
            ep6.getPersonagens().add(leia);
            ep6.getPersonagens().add(han);
            ep6.getPersonagens().add(vader);
            ep6.getPersonagens().add(yoda);
            ep6.getPersonagens().add(r2d2);
            ep6.getNaves().add(millenniumFalcon);
            ep6.getNaves().add(xWing);
            filmeRepositorio.save(ep6);
            log.info("Preloading " + ep6);
        };
    }
}
