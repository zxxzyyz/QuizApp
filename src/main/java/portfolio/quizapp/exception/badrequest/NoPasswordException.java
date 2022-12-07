package portfolio.quizapp.exception.badrequest;

import portfolio.quizapp.exception.ErrorCode;

public class NoPasswordException extends InvalidValueException {

    public NoPasswordException() {
        super(ErrorCode.NO_PASSWORD, "Password can not be a null.");
    }
}
