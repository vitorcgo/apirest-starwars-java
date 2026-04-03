package senac.tsi.starwars.excecoes;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RecursoNaoEncontradoAdvice {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String recursoNaoEncontradoHandler(RecursoNaoEncontradoException ex) {
        return ex.getMessage();
    }
}
