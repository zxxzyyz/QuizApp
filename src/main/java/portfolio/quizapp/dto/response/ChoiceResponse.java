package portfolio.quizapp.dto.response;

import lombok.Getter;

@Getter
public class ChoiceResponse {

    private Long choiceId;

    private Long quizId;

    private boolean correct;

    private String content;
}
