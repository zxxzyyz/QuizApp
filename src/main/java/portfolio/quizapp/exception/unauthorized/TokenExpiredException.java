package portfolio.quizapp.exception.unauthorized;

import portfolio.quizapp.exception.ErrorCode;

public class TokenExpiredException extends UnauthorizedException {

    public TokenExpiredException() {
        super(ErrorCode.NO_ACCESS_TOKEN, "Your token has been expired.");
    }
}
