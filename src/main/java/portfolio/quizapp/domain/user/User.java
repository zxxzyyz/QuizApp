package portfolio.quizapp.domain.user;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import portfolio.quizapp.domain.BaseTimeEntity;

import javax.persistence.*;

@Builder
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    public User(final Long id, final String username, final String password, final Role role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public static User of(
            final String username,
            final String encryptedPassword,
            final Role role) {
        return User.builder()
                .username(username)
                .password(encryptedPassword)
                .role(role)
                .build();
    }

    public void update(final User user) {
        updateUsername(user.getUsername());
        updatePassword(user.getPassword());
        updateRole(user.getRole());
    }

    private void updateUsername(final String username) {
        if (username != null) {
            this.username = username;
        }
    }

    private void updatePassword(final String password) {
        if (password != null) {
            this.password = password;
        }
    }

    private void updateRole(final Role role) {
        if (username != null) {
            this.username = username;
        }
    }
}
