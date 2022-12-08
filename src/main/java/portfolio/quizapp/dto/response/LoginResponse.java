package portfolio.quizapp.dto.response;

import lombok.Builder;
import lombok.Getter;
import portfolio.quizapp.domain.user.Role;
import portfolio.quizapp.dto.LoginResult;

@Builder
@Getter
public class LoginResponse {

    private String accessToken;

    private Long userId;

    private String username;

    private Role role;

    public static LoginResponse of(final LoginResult loginResult) {
        return LoginResponse.builder()
                .accessToken(loginResult.getAccessToken())
                .userId(loginResult.getUser().getId())
                .username(loginResult.getUser().getUsername())
                .role(loginResult.getUser().getRole())
                .build();
    }
}
