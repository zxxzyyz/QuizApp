package portfolio.quizapp.presentation.api;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;
import portfolio.quizapp.dto.request.UserCreateRequest;
import portfolio.quizapp.predefinition.fixture.UserFixture;
import portfolio.quizapp.presentation.PresentationTest;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class UserControllerTest extends PresentationTest {

    @Test
    void 会員登録() throws Exception {
        //given
        Long userId = 1L;
        UserCreateRequest userCreateRequest = UserFixture.KOO.toCreateRequest();
        given(userService.save(any(UserCreateRequest.class))).willReturn(userId);

        //when
        ResultActions resultActions = postJson("/api/v1/users", userCreateRequest);

        //then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", equalTo("/api/v1/users/" + userId)))
                .andDo(print());
    }
}