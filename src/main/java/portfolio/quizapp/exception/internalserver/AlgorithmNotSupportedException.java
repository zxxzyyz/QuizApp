package portfolio.quizapp.exception.internalserver;

import portfolio.quizapp.exception.ErrorCode;

public class AlgorithmNotSupportedException extends InternalServerException {

    public AlgorithmNotSupportedException(final String message) {
        super(ErrorCode.INTERNAL_SERVER_ERROR, message);
    }
}
