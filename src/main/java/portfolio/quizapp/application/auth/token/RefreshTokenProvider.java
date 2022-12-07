package portfolio.quizapp.application.auth.token;

import org.apache.logging.log4j.core.util.UuidUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import portfolio.quizapp.domain.token.RefreshToken;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;

@Component
public class RefreshTokenProvider {

    private final long tokenLifeTime;

    public RefreshTokenProvider(@Value("${security.refresh-token.life-time}") final long tokenLifeTime) {
        this.tokenLifeTime = tokenLifeTime;
    }

    public RefreshToken createRefreshToken(final long id) {
        return RefreshToken.builder()
                .tokenValue(UuidUtil.getTimeBasedUuid().toString())
                .userId(id)
                .expirationTime(LocalDateTime.now().plus(tokenLifeTime, ChronoField.MILLI_OF_DAY.getBaseUnit()))
                .build();
    }
}
