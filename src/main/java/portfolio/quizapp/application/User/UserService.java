package portfolio.quizapp.application.User;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portfolio.quizapp.application.auth.QuizAppEncoder;
import portfolio.quizapp.domain.user.Role;
import portfolio.quizapp.domain.user.User;
import portfolio.quizapp.domain.user.UserRepository;
import portfolio.quizapp.dto.request.UserCreateRequest;
import portfolio.quizapp.dto.request.UserUpdateRequest;
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
    public Long save(final UserCreateRequest userCreateRequest) {
        badRequestIfUserExist(userCreateRequest.getUsername());
        final String encodedPassword = passwordEncoder.encode(userCreateRequest.getPassword());
        final User user = User.of(userCreateRequest.getUsername(), encodedPassword, Role.USER);
        return userRepository.save(user).getId();
    }

    public UserResponse find(final Long id) {
        final User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return UserResponse.from(user);
    }

    @Transactional
    public void update(final Long id, final UserUpdateRequest request) {
        final User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        final String encodedPassword = passwordEncoder.encode(request.getPassword());
        user.update(User.of(request.getUsername(), encodedPassword, request.getRole()));
    }

    @Transactional
    public void delete(final Long id) {
        final User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        userRepository.delete(user);
    }

    private void badRequestIfUserExist(final String username) {
        userRepository.findByUsername(username).ifPresent(user -> {
            throw new DuplicateUserException(user.getUsername());
        });
    }
}
