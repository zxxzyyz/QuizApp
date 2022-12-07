package portfolio.quizapp.application.auth.token;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import portfolio.quizapp.domain.user.Role;
import portfolio.quizapp.exception.unauthorized.InvalidFormatAccessTokenException;
import portfolio.quizapp.exception.unauthorized.NoAccessTokenException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {

    private static final String TOKEN_TYPE = "Bearer";

    private static final String TOKEN_SUBJECT = "AccessToken";

    private static final String TOKEN_DELIMITER = " ";

    private static final String CLAIM_ID = "id";

    private static final String CLAIM_ROLE = "role";

    private final Key secretKey;

    private final long tokenLifeTime;

    public JwtProvider(
            @Value("${security.access-token.secret-key}") final String secretKey,
            @Value("${security.access-token.life-time}") final long tokenLifeTime) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.tokenLifeTime = tokenLifeTime;
    }

    public String createAccessToken(final Long id, final Role role) {
        final Date tokenGenerationTime = new Date();
        final Date tokenExpirationTime = new Date(tokenGenerationTime.getTime() + tokenLifeTime);
        return Jwts.builder()
                .setSubject(TOKEN_SUBJECT)
                .setIssuedAt(tokenGenerationTime)
                .setExpiration(tokenExpirationTime)
                .claim(CLAIM_ID, id)
                .claim(CLAIM_ROLE, role)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isValidAccessToken(final String authorizationHeader) {
        final String accessToken = extractAccessToken(authorizationHeader, TOKEN_TYPE);
        try {
            final Claims claimBody = getClaimBody(accessToken);
            final boolean isAccessToken = claimBody.getSubject().equals(TOKEN_SUBJECT);
            final boolean isValid = claimBody.getExpiration().after(new Date());

            return isAccessToken && isValid;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    public UserPayload getPayload(final String authorizationHeader) {
        final String accessToken = extractAccessToken(authorizationHeader, TOKEN_TYPE);
        final Claims claimBody = getClaimBody(accessToken);

        try {
            final Long id = claimBody.get(CLAIM_ID, Long.class);
            final Role role = Role.valueOf(claimBody.get(CLAIM_ROLE, String.class));

            return new UserPayload(id, role);
        } catch (IllegalArgumentException | NullPointerException | RequiredTypeException ex) {
            try (PrintWriter pw = new PrintWriter(new StringWriter())) {
                ex.printStackTrace(pw);
                throw new InvalidFormatAccessTokenException(pw.toString());
            }
        }
    }

    private Claims getClaimBody(final String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private String extractAccessToken(String authorizationHeader, String tokenType) {
        if (authorizationHeader == null) {
            throw new NoAccessTokenException();
        }

        final String[] tokenTypeAndValue = authorizationHeader.split(TOKEN_DELIMITER);
        if (tokenTypeAndValue.length != 2) {
            throw new InvalidFormatAccessTokenException("Invalid token length");
        }
        if (!tokenTypeAndValue[0].equalsIgnoreCase(tokenType)) {
            throw new InvalidFormatAccessTokenException("Invalid token type");
        }

        return tokenTypeAndValue[1];
    }
}
