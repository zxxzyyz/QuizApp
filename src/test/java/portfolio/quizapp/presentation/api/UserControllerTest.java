package portfolio.quizapp.presentation.api;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.ResultActions;
import portfolio.quizapp.application.auth.token.UserPayload;
import portfolio.quizapp.domain.user.Role;
import portfolio.quizapp.dto.request.UserRequest;
import portfolio.quizapp.dto.response.UserResponse;
import portfolio.quizapp.exception.notfound.UserNotFoundException;
import portfolio.quizapp.predefinition.fixture.UserFixture;
import portfolio.quizapp.presentation.PresentationTest;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class UserControllerTest extends PresentationTest {

    @Test
    void 会員登録() throws Exception {
        //given
        Long userId = 3L;
        UserRequest userRequest = UserFixture.KOO.toCreateRequest();
        given(userService.save(isNull(), any(UserRequest.class))).willReturn(userId);

        //when
        ResultActions resultActions = postJson("/api/v1/users", userRequest);

        //then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", equalTo("/api/v1/users/" + userId)))
                .andDo(print());
    }

    @Test
    void 特定IDユーザー_取得() throws Exception {
        //given
        String authHeader = "Bearer Token";
        given(jwtProvider.isValidAccessToken(authHeader)).willReturn(true);

        Long loginId = 1L;
        UserPayload userPayload = new UserPayload(loginId, Role.ADMIN);
        given(jwtProvider.getPayload(authHeader)).willReturn(userPayload);

        Long targetId = 2L;
        given(userService.find(userPayload, targetId)).willReturn(UserResponse.from(UserFixture.ANDO.生成(targetId)));

        //when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/users/" + targetId)
                .header(HttpHeaders.AUTHORIZATION, authHeader)).andDo(print());

        //then
        resultActions.andExpect(status().isOk());

        assertAll(
                () -> verify(jwtProvider).isValidAccessToken(authHeader),
                () -> verify(jwtProvider).getPayload(authHeader),
                () -> verify(userService).find(userPayload, targetId)
        );
    }

    @Test
    void 特定IDユーザー_取得_UserNotFound() throws Exception {
        //given
        String authHeader = "Bearer Token";
        given(jwtProvider.isValidAccessToken(authHeader)).willReturn(true);

        Long loginId = 1L;
        UserPayload userPayload = new UserPayload(loginId, Role.ADMIN);
        given(jwtProvider.getPayload(authHeader)).willReturn(userPayload);

        Long targetId = 2L;
        given(userService.find(userPayload, targetId)).willThrow(new UserNotFoundException(targetId));

        //when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/users/" + targetId)
                .header(HttpHeaders.AUTHORIZATION, authHeader)).andDo(print());

        //then
        resultActions.andExpect(status().isNotFound());

        assertAll(
                () -> verify(jwtProvider).isValidAccessToken(authHeader),
                () -> verify(jwtProvider).getPayload(authHeader),
                () -> verify(userService).find(userPayload, targetId)
        );
    }
}