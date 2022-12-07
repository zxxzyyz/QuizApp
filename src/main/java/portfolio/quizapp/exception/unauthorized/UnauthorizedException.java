package portfolio.quizapp.exception.unauthorized;

import portfolio.quizapp.exception.ErrorCode;
import portfolio.quizapp.exception.QuizAppException;

public class UnauthorizedException extends QuizAppException {

    public UnauthorizedException(final ErrorCode errorCode, final String message) {
        super(errorCode, message);
    }
}
