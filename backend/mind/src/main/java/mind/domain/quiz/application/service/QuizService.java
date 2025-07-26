package game3.domain.quiz.application.service;

import game3.domain.quiz.presentation.dto.req.AnswerQuizReq;
import game3.domain.quiz.presentation.dto.res.quiz.GetQuizRes;
import game3.domain.quiz.presentation.dto.res.quiz.QuizAnswerRes;
import game3.domain.quiz.presentation.dto.res.quiz.StatusQuizRes;

public interface QuizService {

    GetQuizRes getQuiz(Long quizNumber, Long quizRecordId, String userId);


    GetQuizRes returnQuiz(Long quizId, Long imageId, String userId);


    QuizAnswerRes answerQuiz(AnswerQuizReq req, String userId);

    StatusQuizRes statusQuiz(String userId);

    void doAnalyzeAsync(String userId, Long quizId);
}
