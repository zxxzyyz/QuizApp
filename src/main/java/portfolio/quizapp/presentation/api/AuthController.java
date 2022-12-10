package portfolio.quizapp.presentation.api;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import portfolio.quizapp.application.auth.AuthService;
import portfolio.quizapp.application.auth.token.RefreshTokenCookieProvider;
import portfolio.quizapp.dto.request.LoginRequest;
import portfolio.quizapp.dto.LoginResult;
import portfolio.quizapp.dto.response.LoginResponse;
import portfolio.quizapp.exception.unauthorized.NoRefreshTokenException;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    private final AuthService authService;

    private final RefreshTokenCookieProvider refreshTokenCookieProvider;

    public AuthController(final AuthService authService, final RefreshTokenCookieProvider refreshTokenCookieProvider) {
        this.authService = authService;
        this.refreshTokenCookieProvider = refreshTokenCookieProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(final LoginRequest loginRequest) {
        final LoginResult loginResult = authService.login(loginRequest);
        ResponseCookie cookie = refreshTokenCookieProvider.createCookie(loginResult.getRefreshToken());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(LoginResponse.of(loginResult));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @CookieValue(value = RefreshTokenCookieProvider.REFRESH_TOKEN, required = false) final String refreshToken) {
        if (refreshToken != null) throw new NoRefreshTokenException();
        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookieProvider.createExpiredCookie().toString())
                .build();
    }
}
