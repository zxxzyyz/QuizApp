package portfolio.quizapp.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import portfolio.quizapp.dto.response.ExceptionResponse;
import portfolio.quizapp.exception.badrequest.InvalidValueException;
import portfolio.quizapp.exception.notfound.NotFoundException;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(InvalidValueException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidValueException(final InvalidValueException ex) {
        return ResponseEntity.badRequest().body(ExceptionResponse.from(ex));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFoundException(final NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ExceptionResponse.from(ex));
    }
}
