package portfolio.quizapp.application.User;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portfolio.quizapp.application.auth.QuizAppEncoder;
import portfolio.quizapp.domain.user.Role;
import portfolio.quizapp.domain.user.User;
import portfolio.quizapp.domain.user.UserRepository;
import portfolio.quizapp.dto.request.UserCreateRequest;
import portfolio.quizapp.exception.badrequest.DuplicateUserException;

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

    private void badRequestIfUserExist(String username) {
        userRepository.findByUsername(username).ifPresent(user -> {
            throw new DuplicateUserException(user.getUsername());
        });
    }
}
