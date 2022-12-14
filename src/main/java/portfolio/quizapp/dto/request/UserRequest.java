package portfolio.quizapp.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import portfolio.quizapp.domain.user.Role;

@Getter
@Builder
@AllArgsConstructor
public class UserRequest {

    private String username;

    private String password;

    private Role role;
}
