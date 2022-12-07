package portfolio.quizapp.exception.unauthorized;

import portfolio.quizapp.exception.ErrorCode;

public class InvalidFormatAccessTokenException extends UnauthorizedException {

    public InvalidFormatAccessTokenException(final String message) {
        super(ErrorCode.INVALID_FORMAT_ACCESS_TOKEN, "Format of your access token is not correct={" + message + "}");
    }
}
