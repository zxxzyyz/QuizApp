package portfolio.quizapp.predefinition.fixture;

import portfolio.quizapp.domain.user.Role;
import portfolio.quizapp.domain.user.User;
import portfolio.quizapp.dto.request.UserCreateRequest;

import static portfolio.quizapp.domain.user.Role.*;

public enum UserFixture {

    KOO("Jungmin", "1234", ADMIN),

    ANDO("Yurika", "5678", USER);

    private final String username;

    private final  String password;

    private final  Role role;

    UserFixture(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public User 生成() {
        return 生成(null);
    }

    public User 生成(final Long id) {
        return buildUser(id).build();
    }

    public UserCreateRequest toCreateRequest() {
        return UserCreateRequest.builder()
                .username(username)
                .password(password)
                .build();
    }

    private User.UserBuilder buildUser(final Long id) {
        return User.builder()
                .id(id)
                .username(this.username)
                .password(this.password)
                .role(this.role);
    }
}
