package portfolio.quizapp.exception.badrequest;

import portfolio.quizapp.exception.ErrorCode;

public class DuplicateUserException extends InvalidValueException {

    public DuplicateUserException(final String username) {
        super(ErrorCode.DUPLICATE_USER, "A user of username: " + username + " already exists.");
    }
}
