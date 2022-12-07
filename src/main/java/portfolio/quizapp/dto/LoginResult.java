package portfolio.quizapp.dto;

import lombok.Builder;
import lombok.Getter;
import portfolio.quizapp.domain.token.RefreshToken;
import portfolio.quizapp.domain.user.Role;
import portfolio.quizapp.domain.user.User;

@Builder
@Getter
public class LoginResult {

    private String refreshToken;

    private String accessToken;

    private Long userId;

    private String username;

    private Role role;

    public static LoginResult from(final RefreshToken refreshToken, final String accessToken, final User user) {
        return LoginResult.builder()
                .refreshToken(refreshToken.getTokenValue())
                .accessToken(accessToken)
                .userId(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }
}
