package portfolio.quizapp.exception.unauthorized;

import portfolio.quizapp.exception.ErrorCode;

public class NoRefreshTokenException extends UnauthorizedException {

    public NoRefreshTokenException() {
        super(ErrorCode.NO_ACCESS_TOKEN, "Refresh token does not exist.");
    }
}
