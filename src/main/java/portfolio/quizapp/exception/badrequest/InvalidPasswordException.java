package portfolio.quizapp.exception.badrequest;

import portfolio.quizapp.exception.ErrorCode;

public class InvalidPasswordException extends InvalidValueException {

    public InvalidPasswordException() {
        super(ErrorCode.INVALID_PASSWORD, "Wrong password.");
    }
}
