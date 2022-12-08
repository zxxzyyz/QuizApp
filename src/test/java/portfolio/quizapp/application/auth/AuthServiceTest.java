package portfolio.quizapp.application.auth;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import portfolio.quizapp.application.auth.token.JwtProvider;
import portfolio.quizapp.application.auth.token.RefreshTokenProvider;
import portfolio.quizapp.domain.token.RefreshToken;
import portfolio.quizapp.domain.token.RefreshTokenRepository;
import portfolio.quizapp.domain.user.User;
import portfolio.quizapp.domain.user.UserRepository;
import portfolio.quizapp.dto.LoginResult;
import portfolio.quizapp.dto.request.LoginRequest;
import portfolio.quizapp.exception.badrequest.InvalidPasswordException;
import portfolio.quizapp.exception.notfound.UserNotFoundException;
import portfolio.quizapp.predefinition.fixture.UserFixture;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    AuthService authService;

    @Mock
    UserRepository userRepository;

    @Mock
    JwtProvider jwtProvider;

    @Mock
    RefreshTokenProvider refreshTokenProvider;

    @Mock
    QuizAppEncoder quizAppEncoder;

    @Mock
    RefreshTokenRepository refreshTokenRepository;

    @Test
    void ログイン_成功() throws Exception {
        //given
        User user = UserFixture.KOO.生成(1L);
        String expectedAccessToken = "accessToken";
        String refreshTokenValue = "refreshTokenValue";
        LocalDateTime expirationDate = LocalDateTime.now().plusMinutes(1);
        LoginRequest loginRequest = new LoginRequest(user.getUsername(), user.getPassword());

        given(userRepository.findByUsername(user.getUsername())).willReturn(Optional.of(user));
        given(quizAppEncoder.matches(user.getPassword(), quizAppEncoder.encode(user.getPassword()))).willReturn(true);
        given(jwtProvider.createAccessToken(user.getId(), user.getRole()))
                .willReturn(expectedAccessToken);
        given(refreshTokenProvider.createRefreshToken(user.getId()))
                .willReturn(new RefreshToken(refreshTokenValue, user.getId(), expirationDate));

        //when
        LoginResult loginResult = authService.login(loginRequest);

        //then
        assertAll(
                () -> assertThat(loginResult.getAccessToken()).isEqualTo(expectedAccessToken),
                () -> assertThat(loginResult.getUser()).usingRecursiveComparison().isEqualTo(user),
                () -> assertThat(loginResult.getRefreshToken()).isEqualTo(refreshTokenValue),
                () -> verify(jwtProvider).createAccessToken(user.getId(), user.getRole()),
                () -> verify(refreshTokenProvider).createRefreshToken(user.getId())
        );
    }

    @Test
    void ログイン_失敗_UserNotFound() throws Exception {
        //given
        User user = UserFixture.KOO.生成(1L);
        LoginRequest loginRequest = new LoginRequest(user.getUsername(), user.getPassword());

        //when, then
        assertThatThrownBy(() -> authService.login(loginRequest))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void ログイン_失敗_InvalidPassword() throws Exception {
        //given
        User user = UserFixture.KOO.生成(1L);
        LoginRequest loginRequest = new LoginRequest(user.getUsername(), user.getPassword());
        given(userRepository.findByUsername(user.getUsername())).willReturn(Optional.of(user)); // not encoded

        //when, then
        assertThatThrownBy(() -> authService.login(loginRequest))
                .isInstanceOf(InvalidPasswordException.class);
    }
}