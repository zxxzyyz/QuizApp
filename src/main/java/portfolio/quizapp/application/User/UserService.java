package portfolio.quizapp.application.User;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portfolio.quizapp.application.auth.QuizAppEncoder;
import portfolio.quizapp.application.auth.token.UserPayload;
import portfolio.quizapp.domain.user.Role;
import portfolio.quizapp.domain.user.User;
import portfolio.quizapp.domain.user.UserRepository;
import portfolio.quizapp.dto.request.UserRequest;
import portfolio.quizapp.dto.response.UserResponse;
import portfolio.quizapp.exception.Forbidden.NotAdminException;
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
    public Long save(@Nullable final UserPayload userPayload, final UserRequest userRequest) {
        throwIfUserExist(userRequest.getUsername());

        if (userPayload == null || userPayload.getRole() == Role.USER) {
            throwIfRequestToAdmin(userRequest.getRole());
        }

        final String encodedPassword = passwordEncoder.encode(userRequest.getPassword());
        final User user = User.of(userRequest.getUsername(), encodedPassword, userRequest.getRole());

        return userRepository.save(user).getId();
    }

    public UserResponse find(final UserPayload userPayload, final Long id) {
        final User target = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        if (userPayload.getRole() == Role.USER) {
            throwIfIdNotSame(userPayload.getId(), target.getId());
        }

        return UserResponse.from(target);
    }

    @Transactional
    public void update(final UserPayload userPayload, final Long id, final UserRequest request) {
        final User target = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        final String encodedPassword = passwordEncoder.encode(request.getPassword());

        if (userPayload.getRole() == Role.USER) {
            throwIfIdNotSame(userPayload.getId(), target.getId());
            throwIfRequestToAdmin(request.getRole());
        }

        target.update(User.of(request.getUsername(), encodedPassword, request.getRole()));
    }

    @Transactional
    public void delete(final UserPayload userPayload, final Long id) {
        final User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        if (userPayload.getRole() == Role.USER) {
            throwIfIdNotSame(userPayload.getId(), id);
        }

        userRepository.delete(user);
    }

    private void throwIfUserExist(final String username) {
        userRepository.findByUsername(username).ifPresent(user -> {
            throw new DuplicateUserException(user.getUsername());
        });
    }

    private void throwIfIdNotSame(Long loginId, Long targetId) {
        if (!loginId.equals(targetId)) {
            throw new NotAdminException();
        }
    }

    private void throwIfRequestToAdmin(final Role role) {
        if (role == Role.ADMIN) {
            throw new NotAdminException();
        }
    }
}
