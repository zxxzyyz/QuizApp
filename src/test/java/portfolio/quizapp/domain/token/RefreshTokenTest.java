package portfolio.quizapp.domain.token;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class RefreshTokenTest {

    @Test
    void RefreshToken_有効無効判別() throws Exception {
        //given
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime secondBeforeNow = now.minusSeconds(1);
        LocalDateTime secondAfterNow = now.plusSeconds(1);

        RefreshToken invalidToken = new RefreshToken("refreshToken", 1L, secondBeforeNow);
        RefreshToken validToken = new RefreshToken("refreshToken", 1L, secondAfterNow);

        //when, then
        assertThat(invalidToken.isValid()).isFalse();
        assertThat(validToken.isValid()).isTrue();
    }
}