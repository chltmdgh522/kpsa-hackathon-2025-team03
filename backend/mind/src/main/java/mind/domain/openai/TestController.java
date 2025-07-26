package mind.domain.openai;

import mind.domain.quiz.application.service.impl.AnalyzeServiceImpl;
import mind.domain.quiz.domain.entity.QuizRecord;
import mind.domain.quiz.domain.repository.QuizRecordRepository;
import mind.domain.user.domain.entity.User;
import mind.domain.user.domain.repository.UserRepository;
import mind.global.infra.exception.error.ErrorCode;
import mind.global.infra.exception.error.MindException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/openai")
@RestController
@RequiredArgsConstructor
@Slf4j
public class TestController {

    private final UserRepository userRepository;
    private final QuizRecordRepository quizRecordRepository;
    private final AnalyzeServiceImpl analyzeService;
    @GetMapping
    public void openaiTest(@AuthenticationPrincipal String userId){
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) throw new MindException(ErrorCode.USER_NOT_EXIST);

        QuizRecord quizRecord = quizRecordRepository.findById(2L).orElse(null);
        if (quizRecord == null) throw new MindException(ErrorCode.QUIZ_RECOMMEND_NOT_EXIST);

        analyzeService.test(user,quizRecord);

    }
}
