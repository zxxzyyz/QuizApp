package portfolio.quizapp.presentation.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import portfolio.quizapp.application.User.UserService;
import portfolio.quizapp.application.auth.token.UserPayload;
import portfolio.quizapp.dto.request.UserCreateRequest;
import portfolio.quizapp.dto.request.UserUpdateRequest;
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
        final Long userId = userService.save(userCreateRequest);
        return ResponseEntity.created(URI.create("/api/v1/users/" + userId)).build();
    }

    @Login
    @GetMapping("/me")
    public ResponseEntity<UserResponse> findMe(@Verified final UserPayload userPayload) {
        UserResponse userResponse = userService.find(userPayload.getId());
        return ResponseEntity.ok(userResponse);
    }

    @Login(adminRequired = true)
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findOne(@PathVariable final Long id) {
        UserResponse response = userService.find(id);
        return ResponseEntity.ok(response);
    }

    @Login(adminRequired = true)
    @PatchMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable final Long id, final UserUpdateRequest userUpdateRequest) {
        userService.update(id, userUpdateRequest);
        return ResponseEntity.noContent().build();
    }

    @Login
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMe(@Verified final UserPayload userPayload) {
        userService.delete(userPayload.getId());
        return ResponseEntity.noContent().build();
    }

    @Login(adminRequired = true)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
