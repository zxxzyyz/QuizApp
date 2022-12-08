package portfolio.quizapp.application.User;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import portfolio.quizapp.dto.request.UserCreateRequest;
import portfolio.quizapp.exception.badrequest.DuplicateUserException;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    UserService userService;

    @Test
    void 会員登録() throws Exception {
        //given
        UserCreateRequest userCreateRequest = new UserCreateRequest("UserForTest", "PasswordForTest");

        //when
        Long id = userService.saveUser(userCreateRequest);

        //then
        assertThat(id).isNotNull();
    }

    @Test
    void 重複会員_会員登録() throws Exception {
        //given
        UserCreateRequest userCreateRequest = new UserCreateRequest("UserForTest", "PasswordForTest");

        //when
        Long id = userService.saveUser(userCreateRequest);
        assertThat(id).isNotNull();

        //then
        assertThatThrownBy(() -> userService.saveUser(userCreateRequest)).isInstanceOf(DuplicateUserException.class);
    }
}