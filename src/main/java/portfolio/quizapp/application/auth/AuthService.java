package portfolio.quizapp.application.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portfolio.quizapp.application.auth.token.JwtProvider;
import portfolio.quizapp.application.auth.token.RefreshTokenProvider;
import portfolio.quizapp.domain.token.RefreshToken;
import portfolio.quizapp.domain.token.RefreshTokenRepository;
import portfolio.quizapp.domain.user.User;
import portfolio.quizapp.domain.user.UserRepository;
import portfolio.quizapp.dto.request.LoginRequest;
import portfolio.quizapp.dto.LoginResult;
import portfolio.quizapp.exception.badrequest.InvalidPasswordException;
import portfolio.quizapp.exception.notfound.UserNotFoundException;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtProvider jwtProvider;

    private final RefreshTokenRepository refreshTokenRepository;

    private final RefreshTokenProvider refreshTokenProvider;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider, RefreshTokenRepository refreshTokenRepository, RefreshTokenProvider refreshTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.refreshTokenRepository = refreshTokenRepository;
        this.refreshTokenProvider = refreshTokenProvider;
    }

    public LoginResult login(final LoginRequest loginRequest) {
        final String username = loginRequest.getUsername();
        final User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));

        if (!passwordEncoder.matches(user.getPassword(), passwordEncoder.encode(loginRequest.getPassword()))) {
            throw new InvalidPasswordException();
        }

        final String accessToken = jwtProvider.createAccessToken(user.getId(), user.getRole());
        final RefreshToken refreshToken = refreshTokenProvider.createRefreshToken(user.getId());
        refreshTokenRepository.save(refreshToken);

        return LoginResult.from(refreshToken, accessToken, user);
    }
}
