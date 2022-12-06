package portfolio.quizapp.domain.user;

import lombok.Getter;
import portfolio.quizapp.domain.BaseTimeEntity;

import javax.persistence.*;

@Entity
@Getter
public class User extends BaseTimeEntity {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Embedded
    @Column(nullable = false)
    private Password password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
}
