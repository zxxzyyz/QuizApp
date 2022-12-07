package portfolio.quizapp.exception;

public class QuizAppException extends RuntimeException {

    private final ErrorCode errorCode;

    public QuizAppException(final ErrorCode errorCode, final String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
