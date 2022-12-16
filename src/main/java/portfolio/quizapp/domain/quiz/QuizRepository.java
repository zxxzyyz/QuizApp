package portfolio.quizapp.domain.quiz;

import org.springframework.data.jpa.repository.JpaRepository;
import portfolio.quizapp.domain.user.User;

public interface QuizRepository extends JpaRepository<User, Long> {

}