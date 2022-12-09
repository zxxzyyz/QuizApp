package portfolio.quizapp.exception.Forbidden;

import portfolio.quizapp.exception.ErrorCode;


public class NotAdminException extends ForbiddenException {

    public NotAdminException() {
        super(ErrorCode.PERMISSION_DENIED, "Admin authority is required for your request.");
    }
}
