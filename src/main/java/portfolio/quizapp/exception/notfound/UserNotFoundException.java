package portfolio.quizapp.exception.notfound;

import portfolio.quizapp.exception.ErrorCode;

public class UserNotFoundException extends NotFoundException {

    public UserNotFoundException(final String username) {
        super(ErrorCode.USER_NOT_FOUND, "Could not find a user of username " + username);
    }
}
