package senac.tsi.starwars.excecoes;

public class RecursoNaoEncontradoException extends RuntimeException {

    public RecursoNaoEncontradoException(String recurso, Long id) {
        super("Não foi possível encontrar " + recurso + " com id " + id);
    }
}
