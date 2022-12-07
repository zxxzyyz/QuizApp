package portfolio.quizapp.exception.notfound;

import portfolio.quizapp.exception.ErrorCode;
import portfolio.quizapp.exception.QuizAppException;

public class NotFoundException extends QuizAppException {

    public NotFoundException(final ErrorCode errorCode, final String message) {
        super(errorCode, message);
    }
}
