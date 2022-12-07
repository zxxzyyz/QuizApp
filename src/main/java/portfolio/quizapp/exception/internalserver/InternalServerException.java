package portfolio.quizapp.exception.internalserver;

import portfolio.quizapp.exception.ErrorCode;
import portfolio.quizapp.exception.QuizAppException;

public class InternalServerException extends QuizAppException {

    public InternalServerException(final ErrorCode errorCode, final String message) {
        super(errorCode, message);
    }
}
