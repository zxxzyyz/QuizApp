package portfolio.quizapp.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserCreateRequest {

    private final String username;

    private final String password;
}
