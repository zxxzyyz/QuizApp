package portfolio.quizapp.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import portfolio.quizapp.application.User.QuizAppEncoder;

@Configuration
public class EncoderConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new QuizAppEncoder();
    }
}
