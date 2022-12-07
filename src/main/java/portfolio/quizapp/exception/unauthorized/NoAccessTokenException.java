package portfolio.quizapp.exception.unauthorized;

import portfolio.quizapp.exception.ErrorCode;

public class NoAccessTokenException extends UnauthorizedException {

    public NoAccessTokenException() {
        super(ErrorCode.NO_ACCESS_TOKEN, "Access token does not exist.");
    }
}
