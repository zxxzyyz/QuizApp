package portfolio.quizapp.dto.response;

import lombok.Getter;
import portfolio.quizapp.exception.ErrorCode;
import portfolio.quizapp.exception.QuizAppException;

@Getter
public class ExceptionResponse {

    private ErrorCode errorCode;

    private String message;

    public ExceptionResponse(final ErrorCode errorCode, final String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public static ExceptionResponse from(final QuizAppException ex) {
        return new ExceptionResponse(ex.getErrorCode(), ex.getMessage());
    }
}
