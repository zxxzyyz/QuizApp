package portfolio.quizapp.application.auth.token;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import portfolio.quizapp.domain.user.Role;

@Getter
@EqualsAndHashCode
public class UserPayload {

    private final Long id;

    private final Role role;

    public UserPayload(final Long id, final Role role) {
        this.id = id;
        this.role = role;
    }
}
