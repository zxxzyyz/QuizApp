package portfolio.quizapp.presentation.api;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseCookie;
import org.springframework.test.web.servlet.ResultActions;
import portfolio.quizapp.domain.user.User;
import portfolio.quizapp.dto.LoginResult;
import portfolio.quizapp.dto.request.LoginRequest;
import portfolio.quizapp.exception.badrequest.InvalidPasswordException;
import portfolio.quizapp.predefinition.fixture.UserFixture;
import portfolio.quizapp.presentation.PresentationTest;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class AuthControllerTest extends PresentationTest {

    @Test
    void Login_成功() throws Exception {
        //given
        User koo = UserFixture.KOO.生成(1L);
        LoginRequest loginRequest = new LoginRequest(koo.getUsername(), koo.getPassword());

        String accessToken = "accessToken";
        String refreshToken = "refreshToken";
        LoginResult loginResult = LoginResult.from(accessToken, refreshToken, koo);

        given(authService.login(any(LoginRequest.class))).willReturn(loginResult);
        given(refreshTokenCookieProvider.createCookie(loginResult.getRefreshToken()))
                .willReturn(ResponseCookie.from("refreshToken", refreshToken).build());

        //when
        ResultActions resultActions = postJson("/api/v1/login", loginRequest);

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(header().string("Set-Cookie", containsString("refreshToken=" + refreshToken)))
                .andDo(print());
    }

    @Test
    void Login_失敗() throws Exception {
        //given
        User koo = UserFixture.KOO.生成(1L);
        LoginRequest loginRequest = new LoginRequest(koo.getUsername(), koo.getPassword());

        given(authService.login(any(LoginRequest.class))).willThrow(new InvalidPasswordException());

        String message;
        try {
            throw new InvalidPasswordException();
        } catch (Exception exception) {
            message = exception.getMessage();
        }

        //when
        ResultActions resultActions = postJson("/api/v1/login", loginRequest);

        //then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value(message))
                .andDo(print());

    }
}