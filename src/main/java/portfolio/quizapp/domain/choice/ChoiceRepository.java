package portfolio.quizapp.domain.choice;

import org.springframework.data.jpa.repository.JpaRepository;
import portfolio.quizapp.domain.user.User;

public interface ChoiceRepository extends JpaRepository<User, Long> {

}