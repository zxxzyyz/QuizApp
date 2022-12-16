package portfolio.quizapp.presentation.api;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import portfolio.quizapp.application.user.UserService;
import portfolio.quizapp.application.auth.token.RefreshTokenCookieProvider;
import portfolio.quizapp.application.auth.token.UserPayload;
import portfolio.quizapp.dto.request.UserRequest;
import portfolio.quizapp.dto.response.UserResponse;
import portfolio.quizapp.presentation.Login;
import portfolio.quizapp.presentation.Verified;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    private final RefreshTokenCookieProvider refreshTokenCookieProvider;

    public UserController(UserService userService, RefreshTokenCookieProvider refreshTokenCookieProvider) {
        this.userService = userService;
        this.refreshTokenCookieProvider = refreshTokenCookieProvider;
    }

    @PostMapping
    public ResponseEntity<Void> create(@Verified @Nullable final UserPayload userPayload, final UserRequest userRequest) {
        final Long userId = userService.save(userPayload, userRequest);
        return ResponseEntity.created(URI.create("/api/v1/users/" + userId)).build();
    }

    @Login
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findOne(@Verified final UserPayload userPayload, @PathVariable final Long id) {
        UserResponse response = userService.find(userPayload, id);
        return ResponseEntity.ok(response);
    }

    @Login
    @PatchMapping("/{id}")
    public ResponseEntity<Void> update(
            @Verified final UserPayload userPayload, @PathVariable final Long id, final UserRequest userRequest) {
        userService.update(userPayload, id, userRequest);
        return ResponseEntity.noContent().build();
    }

    @Login
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Verified final UserPayload userPayload, @PathVariable final Long id) {
        userService.delete(userPayload, id);

        if (userPayload.getId().equals(id)) {
            return ResponseEntity.noContent()
                    .header(HttpHeaders.SET_COOKIE, refreshTokenCookieProvider.createExpiredCookie().toString())
                    .build();
        }

        return ResponseEntity.noContent().build();
    }
}
