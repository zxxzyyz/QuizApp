package portfolio.quizapp.dto;

import lombok.Builder;
import lombok.Getter;
import portfolio.quizapp.domain.user.User;

@Builder
@Getter
public class LoginResult {

    private String accessToken;

    private String refreshToken;

    private User user;

    public static LoginResult from(final String accessToken, final String refreshToken, final User user) {
        return LoginResult.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(user)
                .build();
    }
}
