package portfolio.quizapp.exception.badrequest;

import portfolio.quizapp.exception.ErrorCode;
import portfolio.quizapp.exception.QuizAppException;

public class InvalidValueException extends QuizAppException {

    public InvalidValueException(final ErrorCode errorCode, final String message) {
        super(errorCode, message);
    }
}
