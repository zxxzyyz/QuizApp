package portfolio.quizapp.exception;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ErrorCode {

    NO_PASSWORD("400-010"),

    INVALID_PASSWORD("400-020"),

    DUPLICATE_USER("400-030"),

    NO_ACCESS_TOKEN("401-010"),

    INVALID_FORMAT_ACCESS_TOKEN("401-020"),

    USER_NOT_FOUND("404-010"),

    INTERNAL_SERVER_ERROR("500-010");

    private final String value;

    ErrorCode(final String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
