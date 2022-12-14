package portfolio.quizapp.application.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import portfolio.quizapp.application.auth.token.UserPayload;
import portfolio.quizapp.domain.user.Role;
import portfolio.quizapp.domain.user.User;
import portfolio.quizapp.dto.request.UserRequest;
import portfolio.quizapp.dto.response.UserResponse;
import portfolio.quizapp.exception.Forbidden.NotAdminException;
import portfolio.quizapp.exception.badrequest.DuplicateUserException;
import portfolio.quizapp.exception.notfound.UserNotFoundException;
import portfolio.quizapp.predefinition.fixture.UserFixture;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    UserService userService;

    UserRequest request1;
    Long id1;
    UserPayload payload1;

    UserRequest request2;
    Long id2;
    UserPayload payload2;

    String newUsername;
    String newPassword;

    @BeforeEach
    void 前提_データ() {
        UserPayload supportPayload = new UserPayload(0L, Role.ADMIN);

        request1 = UserFixture.KOO.toCreateRequest();
        id1 = userService.save(supportPayload, request1);
        payload1 = new UserPayload(id1, request1.getRole());

        request2 = UserFixture.ANDO.toCreateRequest();
        id2 = userService.save(supportPayload, request2);
        payload2 = new UserPayload(id2, request2.getRole());

        newUsername = "newUsername";
        newPassword = "newPassword";
        assertThat(id1).isNotEqualTo(0L);
        assertThat(id2).isNotEqualTo(0L);
    }

    @Test
    void 会員登録() throws Exception {
        //given
        UserRequest userRequest = UserFixture.SMITH.toCreateRequest();

        //when
        Long id = userService.save(null, userRequest);

        //when, then
        assertThat(id).isNotNull();
    }

    @Test
    void 重複会員_会員登録() throws Exception {
        //when, then
        assertThatThrownBy(() -> userService.save(null, request1)).isInstanceOf(DuplicateUserException.class);
    }

    @Test
    void 会員照会_会員存在しない_UserNotFound() throws Exception {
        //when, then
        assertThatThrownBy(() -> userService.find(payload1, id1+id2)).isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void 会員照会_本人() throws Exception {
        //given
        UserResponse expectedResponse = new UserResponse(id1, request1.getUsername(), request1.getRole());

        //when
        UserResponse userResponse = userService.find(payload1, id1);

        //then
        assertThat(userResponse).usingRecursiveComparison().isEqualTo(expectedResponse);
    }

    @Test
    void 会員照会_他人_NotAdmin() throws Exception {
        //when, then
        assertThatThrownBy(() -> userService.find(payload2, id1)).isInstanceOf(NotAdminException.class);
    }

    @Test
    void 会員照会_他人_管理者() throws Exception {
        //given
        UserResponse expectedResponse = new UserResponse(id2, request2.getUsername(), request2.getRole());

        //when
        UserResponse userResponse = userService.find(payload1, id2);

        //then
        assertThat(userResponse).usingRecursiveComparison().isEqualTo(expectedResponse);
    }

    @Test
    void 会員更新_会員存在しない_UserNotFound() throws Exception {
        //given
        UserRequest userRequest = new UserRequest(newUsername, newPassword, request1.getRole());

        //when, then
        assertThatThrownBy(() -> userService.update(payload1, id1+id2, userRequest)).isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void 会員更新_本人() throws Exception {
        //given
        UserRequest updateRequest = new UserRequest(newUsername, newPassword, Role.ADMIN);

        //when
        userService.update(payload1, id1, updateRequest);
        UserResponse updated = userService.find(payload1, id1);

        //then
        assertAll(
                () -> assertThat(updated.getUsername()).isEqualTo(newUsername),
                () -> assertThat(updated.getRole()).isEqualTo(updateRequest.getRole())
        );
    }

    @Test
    void 会員更新_本人_ユーザーが管理者に変更() throws Exception {
        //given
        UserRequest updateRequest = new UserRequest(newUsername, newPassword, Role.ADMIN);

        //when, then
        assertThatThrownBy(() -> userService.update(payload2, id2, updateRequest)).isInstanceOf(NotAdminException.class);
    }

    @Test
    void 会員更新_他人_NotAdmin() throws Exception {
        //given
        UserRequest updateRequest = new UserRequest(newUsername, newPassword, Role.ADMIN);

        //when, then
        assertThatThrownBy(() -> userService.update(payload2, id1, updateRequest)).isInstanceOf(NotAdminException.class);
    }

    @Test
    void 会員更新_他人_管理者() throws Exception {
        //given
        UserRequest updateRequest = new UserRequest(newUsername, newPassword, Role.ADMIN);

        //when
        userService.update(payload1, id2, updateRequest);
        UserResponse updated = userService.find(payload1, id2);

        //then
        assertAll(
                () -> assertThat(updated.getUsername()).isEqualTo(newUsername),
                () -> assertThat(updated.getRole()).isEqualTo(Role.ADMIN)
        );
    }

    @Test
    void 会員削除_会員存在しない_UserNotFound() throws Exception {
        //when, then
        assertThatThrownBy(() -> userService.delete(payload1, id1+id2)).isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void 会員削除_本人() throws Exception {
        //when
        userService.delete(payload1, id1);

        //then
        assertThatThrownBy(() -> userService.find(payload1, id1)).isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void 会員削除_他人_NotAdmin() throws Exception {
        //when, then
        assertThatThrownBy(() -> userService.delete(payload2, id1)).isInstanceOf(NotAdminException.class);
    }

    @Test
    void 会員削除_他人_管理者() throws Exception {
        //when
        userService.delete(payload1, id2);

        //then
        assertThatThrownBy(() -> userService.find(payload1, id2)).isInstanceOf(UserNotFoundException.class);
    }
}