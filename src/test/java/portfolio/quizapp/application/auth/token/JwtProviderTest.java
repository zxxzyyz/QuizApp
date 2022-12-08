package portfolio.quizapp.application.auth.token;

import org.junit.jupiter.api.Test;
import portfolio.quizapp.domain.user.Role;
import portfolio.quizapp.exception.unauthorized.InvalidFormatAccessTokenException;
import portfolio.quizapp.exception.unauthorized.NoAccessTokenException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class JwtProviderTest {

    JwtProvider jwtProvider = new JwtProvider(
            "abcdefghijklmnopqrstuvwxyz0123456789",
            86400000);

    @Test
    void Token_発行() throws Exception {
        //given
        String accessToken = jwtProvider.createAccessToken(1L, Role.USER);

        //when, then
        assertThat(accessToken).isNotNull();
    }

    @Test
    public void Token_有効() throws Exception {
        //given
        String accessToken = jwtProvider.createAccessToken(1L, Role.USER);
        String authHeader = "Bearer " + accessToken;

        //when, then
        assertThat(jwtProvider.isValidAccessToken(authHeader)).isTrue();
    }

    @Test
    void Token_無効() throws Exception {
        //given
        JwtProvider expiredProvider = new JwtProvider("test-0123456789012345678901234567890123456789", 0);
        String accessToken = expiredProvider.createAccessToken(1L, Role.USER);
        String authHeader = "Bearer " + accessToken;

        //when, then
        assertThat(expiredProvider.isValidAccessToken(authHeader)).isFalse();
    }

    @Test
    void Token_内容異常() throws Exception {
        //given
        String authHeader = "Bearer WrongToken";

        //when, then
        assertThat(jwtProvider.isValidAccessToken(authHeader)).isFalse();
    }

    @Test
    void Token_形式異常() throws Exception {
        //given
        String case1 = null;
        String case2 = "BearerToken";
        String case3 = "Bearer Wrong Token";
        String case4 = "Test Token";

        //when, then
        assertAll(
                () -> assertThatThrownBy(() -> jwtProvider.isValidAccessToken(case1))
                        .isInstanceOf(NoAccessTokenException.class),
                () -> assertThatThrownBy(() -> jwtProvider.isValidAccessToken(case2))
                        .isInstanceOf(InvalidFormatAccessTokenException.class),
                () -> assertThatThrownBy(() -> jwtProvider.isValidAccessToken(case3))
                        .isInstanceOf(InvalidFormatAccessTokenException.class),
                () -> assertThatThrownBy(() -> jwtProvider.isValidAccessToken(case4))
                        .isInstanceOf(InvalidFormatAccessTokenException.class)
        );
    }
    
    @Test
    void Token_PayLoad_デコード() throws Exception {
        //given
        String token = jwtProvider.createAccessToken(1L, Role.USER);
        String authorizationHeader = "Bearer " + token;

        //when
        UserPayload payload = jwtProvider.getPayload(authorizationHeader);

        //then
        assertThat(payload).isEqualTo(new UserPayload(1L, Role.USER));
    }
}