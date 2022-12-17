package portfolio.quizapp.dto.request;

import lombok.Getter;

@Getter
public class ChoiceRequest {

    private Long quizId;

    private boolean correct;

    private String content;
}
