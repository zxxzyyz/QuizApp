package portfolio.quizapp.application.User;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portfolio.quizapp.application.auth.QuizAppEncoder;
import portfolio.quizapp.domain.user.Role;
import portfolio.quizapp.domain.user.User;
import portfolio.quizapp.domain.user.UserRepository;
import portfolio.quizapp.dto.request.UserCreateRequest;
import portfolio.quizapp.dto.response.UserResponse;
import portfolio.quizapp.exception.badrequest.DuplicateUserException;
import portfolio.quizapp.exception.notfound.UserNotFoundException;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    private final QuizAppEncoder passwordEncoder;

    public UserService(UserRepository userRepository, QuizAppEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Long saveUser(final UserCreateRequest userCreateRequest) {
        badRequestIfUserExist(userCreateRequest.getUsername());
        final String encodedPassword = passwordEncoder.encode(userCreateRequest.getPassword());
        final User user = User.from(userCreateRequest, encodedPassword, Role.USER);
        return userRepository.save(user).getId();
    }

    public Long saveAdmin(final UserCreateRequest userCreateRequest) {
        final String encodedPassword = passwordEncoder.encode(userCreateRequest.getPassword());
        final User user = User.from(userCreateRequest, encodedPassword, Role.ADMIN);
        return userRepository.save(user).getId();
    }

    private void badRequestIfUserExist(final String username) {
        userRepository.findByUsername(username).ifPresent(user -> {
            throw new DuplicateUserException(user.getUsername());
        });
    }

    public UserResponse find(final Long userId) {
        final User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        return UserResponse.from(user);
    }
}
