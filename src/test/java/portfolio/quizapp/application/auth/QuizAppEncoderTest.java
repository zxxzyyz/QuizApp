package portfolio.quizapp.application.auth;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class QuizAppEncoderTest {

    private final QuizAppEncoder passwordEncoder = new QuizAppEncoder();

    @Test
    void 暗号化() throws Exception {
        //given
        String rawPassword = "1a2b3c4d";

        //when
        String encodedPassword1 = passwordEncoder.encode(rawPassword);
        String encodedPassword2 = passwordEncoder.encode(rawPassword);

        //then
        Assertions.assertThat(rawPassword).isNotEqualTo(encodedPassword2);
        Assertions.assertThat(encodedPassword1).isEqualTo(encodedPassword2);
    }
}