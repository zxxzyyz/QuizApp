package portfolio.quizapp.application.auth.token;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseCookie;

import java.time.Duration;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class RefreshTokenCookieProviderTest {

    private RefreshTokenCookieProvider provider;

    @Test
    void Cookie_発行() throws Exception {
        //given
        long expirationTime = 1000L;
        provider = new RefreshTokenCookieProvider(expirationTime);
        String refreshTokenValue = "RefreshTokenValue";

        //when
        ResponseCookie cookie = provider.createCookie(refreshTokenValue);

        //then
        assertAll(
                () -> assertThat(cookie.getValue()).isEqualTo(refreshTokenValue),
                () -> assertThat(cookie.getMaxAge()).isEqualTo(Duration.ofMillis(expirationTime)));
    }

    @Test
    void Cookie_廃棄() throws Exception {
        //given
        provider = new RefreshTokenCookieProvider(1000L);

        //when
        ResponseCookie cookie = provider.createExpiredCookie();

        //then
        assertThat(cookie.getMaxAge()).isZero();
    }
}