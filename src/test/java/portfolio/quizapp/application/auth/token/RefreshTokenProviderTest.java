package portfolio.quizapp.application.auth.token;

import org.junit.jupiter.api.Test;
import portfolio.quizapp.domain.token.RefreshToken;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class RefreshTokenProviderTest {

    @Test
    void RefreshToken_発行() throws Exception {
        // given
        RefreshTokenProvider provider = new RefreshTokenProvider(10000);

        // when
        RefreshToken refreshToken = provider.createRefreshToken(1L);

        //then
        assertAll(
                () -> assertThat(refreshToken.isValid()).isTrue(),
                () -> assertThat(refreshToken.getTokenValue()).isNotNull()
        );
    }
}