package mind.domain.quiz.application.service;

import mind.domain.quiz.presentation.dto.req.AnswerQuizReq;
import mind.domain.quiz.presentation.dto.res.quiz.GetQuizRes;
import mind.domain.quiz.presentation.dto.res.quiz.QuizAnswerRes;
import mind.domain.quiz.presentation.dto.res.quiz.StatusQuizRes;

public interface QuizService {

    GetQuizRes getQuiz(Long quizNumber, Long quizRecordId, String userId);




    void doAnalyzeAsync(String userId, Long quizId);
}
