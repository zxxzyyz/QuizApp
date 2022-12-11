package portfolio.quizapp.presentation.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import portfolio.quizapp.application.User.UserService;
import portfolio.quizapp.application.auth.token.UserPayload;
import portfolio.quizapp.dto.request.UserCreateRequest;
import portfolio.quizapp.dto.response.UserResponse;
import portfolio.quizapp.presentation.Login;
import portfolio.quizapp.presentation.Verified;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Void> create(final UserCreateRequest userCreateRequest) {
        final Long userId = userService.saveUser(userCreateRequest);
        return ResponseEntity.created(URI.create("/api/v1/users/" + userId)).build();
    }

    @Login
    @GetMapping("/me")
    public ResponseEntity<UserResponse> findOne(@Verified final UserPayload userPayload) {
        UserResponse userResponse = userService.find(userPayload.getId());
        return ResponseEntity.ok(userResponse);
    }

    @Login(adminRequired = true)
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findOne(@PathVariable final Long id) {
        UserResponse response = userService.find(id);
        return ResponseEntity.ok(response);
    }
}
