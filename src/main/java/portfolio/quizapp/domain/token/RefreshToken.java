package portfolio.quizapp.domain.token;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Builder
@Getter
@Entity
@Table(name = "refresh_token")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    @Id
    @Column(name = "token_value")
    private String tokenValue;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "expiration_time", nullable = false)
    private LocalDateTime expirationTime;

    public RefreshToken(final String tokenValue, final Long userId, final LocalDateTime expirationTime) {
        this.tokenValue = tokenValue;
        this.userId = userId;
        this.expirationTime = expirationTime;
    }

    public boolean isValid() {
        return expirationTime.isAfter(LocalDateTime.now());
    }
}
