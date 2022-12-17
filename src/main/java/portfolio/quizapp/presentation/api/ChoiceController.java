package portfolio.quizapp.presentation.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import portfolio.quizapp.application.auth.token.UserPayload;
import portfolio.quizapp.application.choice.ChoiceService;
import portfolio.quizapp.dto.request.ChoiceRequest;
import portfolio.quizapp.dto.response.ChoiceResponse;
import portfolio.quizapp.presentation.Login;
import portfolio.quizapp.presentation.Verified;

@RestController
@RequestMapping("/api/v1/choices")
public class ChoiceController {

    private final ChoiceService choiceService;

    public ChoiceController(ChoiceService choiceService) {
        this.choiceService = choiceService;
    }

    @Login
    @PostMapping
    public ChoiceResponse create(@Verified UserPayload userPayload, @RequestBody ChoiceRequest choiceRequest) {

    }
}
