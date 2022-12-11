package portfolio.quizapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import portfolio.quizapp.domain.user.Role;
import portfolio.quizapp.domain.user.User;

@Getter
@AllArgsConstructor
public class UserResponse {

    private Long userId;

    private String username;

    private Role role;

    public static UserResponse from(final User user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getRole());
    }
}
