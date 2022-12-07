package portfolio.quizapp.presentation.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import portfolio.quizapp.application.User.UserService;
import portfolio.quizapp.dto.request.UserCreateRequest;

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
}
