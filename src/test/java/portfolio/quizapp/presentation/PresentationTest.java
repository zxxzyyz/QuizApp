package portfolio.quizapp.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import portfolio.quizapp.application.User.UserService;
import portfolio.quizapp.application.auth.AuthService;
import portfolio.quizapp.application.auth.token.JwtProvider;
import portfolio.quizapp.application.auth.token.RefreshTokenCookieProvider;
import portfolio.quizapp.presentation.api.AuthController;
import portfolio.quizapp.presentation.api.UserController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@WebMvcTest(controllers = {AuthController.class, UserController.class})
public class PresentationTest {

    @MockBean
    protected AuthService authService;

    @MockBean
    protected UserService userService;

    @MockBean
    protected JwtProvider jwtProvider;

    @MockBean
    protected RefreshTokenCookieProvider refreshTokenCookieProvider;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    protected ResultActions postJson(String uri, Object body) throws Exception {
        return mockMvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .contentType(objectMapper.writeValueAsString(body)))
                .andDo(print());
    }

    protected ResultActions postJson(String uri) throws Exception {
        return mockMvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }
}
