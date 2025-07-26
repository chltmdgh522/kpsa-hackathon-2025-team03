package mind.domain.quiz.application.service.impl;

import mind.domain.quiz.application.service.QuizService;
import mind.domain.quiz.domain.entity.Quiz;
import mind.domain.quiz.domain.entity.QuizRecord;
import mind.domain.quiz.domain.repository.QuizRecordRepository;
import mind.domain.quiz.domain.repository.QuizRepository;
import mind.domain.quiz.presentation.dto.req.AnswerQuizReq;
import mind.domain.quiz.presentation.dto.res.quiz.GetQuizRes;
import mind.domain.quiz.presentation.dto.res.quiz.QuizAnswerRes;
import mind.domain.quiz.presentation.dto.res.quiz.StatusQuizRes;
import mind.domain.s3.domain.entity.S3Image;
import mind.domain.s3.domain.repository.S3AudioRepository;
import mind.domain.s3.domain.repository.S3ImageRepository;
import mind.domain.user.domain.entity.User;
import mind.domain.user.domain.repository.UserRepository;
import mind.global.infra.exception.error.MindException;
import mind.global.infra.exception.error.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class QuizServiceImpl implements QuizService {
    private final UserRepository userRepository;
    private final QuizRepository quizRepository;
    private final QuizRecordRepository quizRecordRepository;
    private final S3ImageRepository s3ImageRepository;
    private final S3AudioRepository s3AudioRepository;

    private final AnalyzeServiceImpl analyzeService;

    @Override
    public GetQuizRes getQuiz(Long quizNumber, Long quizRecordId, String userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) throw new MindException(ErrorCode.USER_NOT_EXIST);

        // 퀴즈 번호가 1번일때 초기로 싹다 생성 2~5 그리고 퀴즈 전체 기록도 같이
        Quiz quiz = quizCreate(quizNumber, quizRecordId, user);

        // 고정 문제는 3번 5번, 동적 문제는 1,2,4 번 즉 얘네는 랜덤으로 해주면 될듯
        if (quizNumber == 3 || quizNumber == 5) {
            List<S3Image> byS3Image = s3ImageRepository.findByQuizNumber(quizNumber);
            S3Image s3Image = byS3Image.get(0);
            return GetQuizRes.of(quiz.getId(), quiz.getQuizRecord().getId(), s3Image.getId(), s3Image.getImageUrl(), s3Image.getS3Audio().getAudioUrl());
        }

        List<S3Image> byS3Image = s3ImageRepository.findByQuizNumber(quizNumber);
        Random random = new Random();
        int randomIndex = random.nextInt(byS3Image.size());

        S3Image s3Image = byS3Image.get(randomIndex);
        return GetQuizRes.of(quiz.getId(), quiz.getQuizRecord().getId(), s3Image.getId(), s3Image.getImageUrl(), s3Image.getS3Audio().getAudioUrl());
    }





    private Quiz quizCreate(Long quizNumber, Long quizRecordId, User user) {

        if (quizNumber == 1) {
            Quiz quizR = null;
            QuizRecord quizRecord = QuizRecord
                    .builder()
                    .user(user)
                    .isComplete(false)
                    .build();

            quizRecordRepository.save(quizRecord);

            for (int i = 1; i <= 5; i++) {
                Quiz quiz = Quiz.builder()
                        .user(user)
                        .quizRecord(quizRecord)
                        .quizNumber((long) i) // quizNumber는 1 ~ 5
                        .isComplete(false)
                        .wrongCnt(0L)
                        .build();
                quizRepository.save(quiz);

                if (quizNumber == i) quizR = quiz;
            }

            return quizR;
        }

        QuizRecord quizRecord = quizRecordRepository.findById(quizRecordId).orElse(null);
        if (quizRecord == null) throw new MindException(ErrorCode.QUIZ_RECOMMEND_NOT_EXIST);

        Quiz quiz = quizRepository.findByQuizRecordAndQuizNumber(quizRecord, quizNumber).orElse(null);
        if (quiz == null) throw new MindException(ErrorCode.QUIZ_NOT_EXIST);


        return quiz;

    }

    @Override
    public void doAnalyzeAsync(String userId, Long quizId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) throw new MindException(ErrorCode.USER_NOT_EXIST);


        Quiz quiz = quizRepository.findById(quizId).orElse(null);
        if (quiz == null) throw new MindException(ErrorCode.QUIZ_NOT_EXIST);


        analyzeService.analyzeAIAsync(user, quiz.getQuizRecord());

    }
}
