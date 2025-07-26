package game3.domain.quiz.presentation.controller;

import game3.domain.quiz.application.service.QuizService;
import game3.domain.quiz.presentation.dto.req.AnswerQuizReq;
import game3.domain.quiz.presentation.dto.res.quiz.GetQuizRes;
import game3.domain.quiz.presentation.dto.res.quiz.QuizAnswerRes;
import game3.domain.quiz.presentation.dto.res.quiz.StatusQuizRes;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quiz")
@RequiredArgsConstructor
@Slf4j
public class QuizController {

    private final QuizService quizService;


    @Operation(description = "퀴즈 진행중 API 즉 시작할때 진행중인지 아닌지 판별 API")
    @GetMapping("/status")
    public ResponseEntity<StatusQuizRes> getStartInfo(@AuthenticationPrincipal String userId) {

        return ResponseEntity.ok(quizService.statusQuiz(userId));
    }

    @Operation(description = "퀴즈 정보 불러오기 API")
    @GetMapping("/{quizRecordId}")
    public ResponseEntity<GetQuizRes> getQuiz(
            @PathVariable Long quizRecordId, // 퀴즈번호가 1번일떄는 0으로 해도됨
            @RequestParam Long quizNumber,
            @AuthenticationPrincipal String userId) {

        log.info("sajdfkasjdlkfajsdlfkjasldkfjalksdjflkasdjflkasjdklfasd");
        log.info(String.valueOf(quizRecordId));
        return ResponseEntity.ok(quizService.getQuiz(quizNumber, quizRecordId, userId));
    }

    @Operation(description = "퀴즈 다시하기 API")
    @GetMapping("/return/{quizId}")
    public ResponseEntity<GetQuizRes> returnQuiz(@PathVariable Long quizId,
                                                 @RequestParam Long imageId,
                                                 @AuthenticationPrincipal String userId) {
        log.info("dasjdlfjasldjflaskdjflasjdlfasdklf");
        log.info(String.valueOf(quizId));
        log.info(String.valueOf(imageId));
        return ResponseEntity.ok(quizService.returnQuiz(quizId, imageId,userId));
    }


    @Operation(description = "퀴즈 정답 API")
    @PostMapping
    public ResponseEntity<QuizAnswerRes> answerQuiz(@RequestBody AnswerQuizReq req,
                                                    @AuthenticationPrincipal String userId) {
        log.info("정답은 나야");
        log.info(req.answer1());
        log.info(String.valueOf(req.quizId()));
        QuizAnswerRes quizAnswerRes = quizService.answerQuiz(req, userId);

        if(quizAnswerRes.audioUrl().equals("5")){
            log.info("크하하하하하");
            // 5번까지 완료했으면 AI 분석은 백그라운드에서 수행 (논블로킹)
            quizService.doAnalyzeAsync(userId, req.quizId());
        }

        return ResponseEntity.ok(quizAnswerRes);
    }
}
