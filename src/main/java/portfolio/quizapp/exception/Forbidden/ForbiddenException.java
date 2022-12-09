package portfolio.quizapp.exception.Forbidden;

import portfolio.quizapp.exception.ErrorCode;
import portfolio.quizapp.exception.QuizAppException;

public class ForbiddenException extends QuizAppException {


    public ForbiddenException(final ErrorCode errorCode, final String message) {
        super(errorCode, message);
    }
}
