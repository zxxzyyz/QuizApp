package portfolio.quizapp.presentation;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import portfolio.quizapp.application.auth.token.JwtProvider;
import portfolio.quizapp.application.auth.token.UserPayload;
import portfolio.quizapp.domain.user.Role;
import portfolio.quizapp.exception.Forbidden.NotAdminException;
import portfolio.quizapp.exception.unauthorized.NoAccessTokenException;
import portfolio.quizapp.exception.unauthorized.TokenExpiredException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtProvider jwtProvider;

    public AuthInterceptor(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        HandlerMethod hm = (HandlerMethod) handler;
        Login auth = hm.getMethodAnnotation(Login.class);

        if (!(handler instanceof HandlerMethod) || auth == null) {
            return true;
        }
        String authHeader = request.getHeader(AUTHORIZATION);

        if (request.getHeader(AUTHORIZATION) != null) {
            if (!jwtProvider.isValidAccessToken(authHeader)) {
                throw new TokenExpiredException();
            }

            if (auth.adminRequired()) {
                UserPayload payload = jwtProvider.getPayload(authHeader);
                if (payload.getRole() != Role.ADMIN) {
                    throw new NotAdminException();
                }
            }
            return true;
        }

        if (auth.userRequired()) {
            throw new NoAccessTokenException();
        }

        return true;
    }
}
