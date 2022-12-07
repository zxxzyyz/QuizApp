package portfolio.quizapp.application.auth.token;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.Cookie;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class RefreshTokenCookieProvider {

    private static final int ZERO = 0;

    public static final String REFRESH_TOKEN = "RefreshToken";

    private final Long lifeTime;

    public RefreshTokenCookieProvider(@Value("${security.refresh-token.life-time}") final Long lifeTime) {
        this.lifeTime = lifeTime;
    }

    public ResponseCookie createCookie(final String refreshToken) {
        return createCookieBuilder(refreshToken)
                .maxAge(Duration.ofMillis(lifeTime))
                .build();
    }

    public ResponseCookie createExpiredCookie() {
        return createCookieBuilder("")
                .maxAge(Duration.ofMillis(ZERO))
                .build();
    }

    private ResponseCookie.ResponseCookieBuilder createCookieBuilder(final String refreshToken) {
        return ResponseCookie.from(REFRESH_TOKEN, refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite(Cookie.SameSite.NONE.attributeValue());
    }
}
