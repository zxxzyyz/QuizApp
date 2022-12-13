package portfolio.quizapp.dto.request;

import lombok.Builder;
import lombok.Getter;
import portfolio.quizapp.domain.user.Role;

@Getter
@Builder
public class UserUpdateRequest {

    private final String username;

    private final String password;

    private final Role role;
}
