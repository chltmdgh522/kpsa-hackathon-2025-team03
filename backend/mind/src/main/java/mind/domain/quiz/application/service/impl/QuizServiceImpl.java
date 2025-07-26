package game3.domain.quiz.application.service.impl;

import game3.domain.quiz.application.service.QuizService;
import game3.domain.quiz.domain.entity.Quiz;
import game3.domain.quiz.domain.entity.QuizRecord;
import game3.domain.quiz.domain.repository.QuizRecordRepository;
import game3.domain.quiz.domain.repository.QuizRepository;
import game3.domain.quiz.presentation.dto.req.AnswerQuizReq;
import game3.domain.quiz.presentation.dto.res.quiz.GetQuizRes;
import game3.domain.quiz.presentation.dto.res.quiz.QuizAnswerRes;
import game3.domain.quiz.presentation.dto.res.quiz.StatusQuizRes;
import game3.domain.s3.domain.entity.S3Image;
import game3.domain.s3.domain.repository.S3AudioRepository;
import game3.domain.s3.domain.repository.S3ImageRepository;
import game3.domain.user.domain.entity.User;
import game3.domain.user.domain.repository.UserRepository;
import game3.global.infra.exception.error.MindException;
import game3.global.infra.exception.error.ErrorCode;
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

    @Override
    public GetQuizRes returnQuiz(Long quizId, Long imageId, String userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) throw new MindException(ErrorCode.USER_NOT_EXIST);

        Quiz quiz = quizRepository.findById(quizId).orElse(null);
        if (quiz == null) throw new MindException(ErrorCode.QUIZ_NOT_EXIST);


        Long quizNumber = quiz.getQuizNumber();

        // 고정 문제는 3번 5번, 동적 문제는 1,2,4 번 즉 얘네는 랜덤으로 해주면 될듯
        if (quizNumber == 3 || quizNumber == 5) {
            List<S3Image> byS3Image = s3ImageRepository.findByQuizNumber(quizNumber);
            S3Image s3Image = byS3Image.get(0);
            return GetQuizRes.of(quiz.getId(), quiz.getQuizRecord().getId(), s3Image.getId(), s3Image.getImageUrl(), s3Image.getS3Audio().getAudioUrl());
        }


        List<S3Image> byS3Image = s3ImageRepository.findByQuizNumber(quizNumber);
        List<S3Image> randomS3Image = new ArrayList<>();
        // 이전 이미지가 나오면 안됨
        for (S3Image s3Image : byS3Image) {
            if (!Objects.equals(s3Image.getId(), imageId)) {
                randomS3Image.add(s3Image);
            }
        }
        Random random = new Random();
        int randomIndex = random.nextInt(randomS3Image.size());

        log.info(String.valueOf(quiz.getId()));
        S3Image s3Image = randomS3Image.get(randomIndex);
        return GetQuizRes.of(quiz.getId(), quiz.getQuizRecord().getId(), s3Image.getId(), s3Image.getImageUrl(), s3Image.getS3Audio().getAudioUrl());

    }

    @Override
    public QuizAnswerRes answerQuiz(AnswerQuizReq req, String userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) throw new MindException(ErrorCode.USER_NOT_EXIST);

        S3Image s3Image = s3ImageRepository.findById(req.imageId()).orElse(null);
        if (s3Image == null) throw new MindException(ErrorCode.FILE_NOT_EXIST);

        Quiz quiz = quizRepository.findById(req.quizId()).orElse(null);
        if (quiz == null) throw new MindException(ErrorCode.QUIZ_NOT_EXIST);

        String quizAnswer = s3Image.getQuizAnswer();

        String[] fiveAnswer = quizAnswer.split(",");
        boolean isCorrect = false;

        // 5번 문제: 두 개의 답 비교
        if (quiz.getQuizNumber() == 5) {
            isCorrect = fiveAnswer[0].equals(req.answer1()) && fiveAnswer[1].equals(req.answer2());
        } else {
            // 1~4번 문제: 단일 답 비교
            isCorrect = s3Image.getQuizAnswer().equals(req.answer1());
        }

        log.info("퀴즈의 아이디는 무엇이냐     ㅇㅁㅇㄹㅁ니얼민어림넝리ㅏㅓ리");
        log.info(String.valueOf(quiz.getQuizRecord().getId()));

        if (isCorrect) {
            quiz.setComplete(true);
            quiz.setQuizTime(req.time());
            sucessPoint(quiz);

            if (quiz.getQuizNumber() == 5) {
                quiz.getQuizRecord().setComplete(true);
                return QuizAnswerRes.of(true, quiz.getQuizRecord().getId(), quiz.getId(), "5");
            }
            return QuizAnswerRes.of(true, quiz.getQuizRecord().getId(), quiz.getId(), "필요없음");
        } else {
            // 실패
            failPoint(quiz, fiveAnswer, req);
        }


        // 2번 두번일떄 그거 하삼 까먹지 마삼
        if (quiz.getQuizNumber() == 2 && quiz.getWrongCnt() >= 2) {
            return QuizAnswerRes.of(false, quiz.getQuizRecord().getId(), quiz.getId(), Objects.requireNonNull(s3AudioRepository.findById(20L).orElse(null)).getAudioUrl());
        }
        return QuizAnswerRes.of(false, quiz.getQuizRecord().getId(), quiz.getId(), "필요없음");

    }

    @Override
    public StatusQuizRes statusQuiz(String userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) throw new MindException(ErrorCode.USER_NOT_EXIST);

        List<QuizRecord> byQuizRecords = quizRecordRepository.findByUser(user);

        for (QuizRecord quizRecord : byQuizRecords) {
            if (!quizRecord.isComplete()) {
                for (int quizNumber = 1; quizNumber <= 5; quizNumber++) {
                    Quiz quiz = quizRepository.findByQuizRecordAndQuizNumber(quizRecord, (long) quizNumber).orElse(null);
                    if (quiz == null) throw new MindException(ErrorCode.QUIZ_NOT_EXIST); // 흠 근데 이거 null 이 나올 수가 없음

                    if (!quiz.isComplete()) {

                        // 걍 맨앞에꺼 불러오자 귀찮다 ....
                        List<S3Image> byS3Image = s3ImageRepository.findByQuizNumber(quiz.getQuizNumber());
                        S3Image s3Image = byS3Image.get(0);
                        return StatusQuizRes.of
                                (false, quiz.getId(), quizRecord.getId(), s3Image.getId(), s3Image.getImageUrl(), s3Image.getS3Audio().getAudioUrl(), quiz.getQuizNumber());
                    }
                }
            }
        }

        // 튜토리얼 시작하세요!! 게임 시작해도 된다는 의미
        return StatusQuizRes.of(true, null, null, null, null, null, null);
    }

    private static void sucessPoint(Quiz quiz) {
        QuizRecord quizRecord = quiz.getQuizRecord();
        switch (quiz.getQuizNumber().intValue()) {
            case 1:
                // 1번 퀴즈 로직
                quizRecord.setEmotionPoint(quizRecord.getEmotionPoint() + 3);

                break;
            case 2:
                // 2번 퀴즈 로직
                quizRecord.setInterestPoint(quizRecord.getInterestPoint() + 3);
                break;
            case 3:
                // 3번 퀴즈 로직
                quizRecord.setContextPoint(quizRecord.getContextPoint() + 3);
                break;
            case 4:
                // 4번 퀴즈 로직
                quizRecord.setInterestPoint(quizRecord.getInterestPoint() + 5);
                quizRecord.setEmotionPoint(quizRecord.getEmotionPoint() + 3);
                break;
            case 5:
                // 5번 퀴즈 로직
                quizRecord.setEmotionPoint(quizRecord.getEmotionPoint() + 2); // 감정
                quizRecord.setContextPoint(quizRecord.getContextPoint() + 5); // 맥락
                quizRecord.setSympathyPoint(quizRecord.getSympathyPoint() + 4); // 공감
                break;
        }
    }

    private static void failPoint(Quiz quiz, String[] fiveAnswer, AnswerQuizReq req) {

        QuizRecord quizRecord = quiz.getQuizRecord();
        switch (quiz.getQuizNumber().intValue()) {
            case 1:
                // 1번 퀴즈 로직
                // 점수
                quizRecord.setEmotionPoint(quizRecord.getEmotionPoint() - 1); // 감정 인식

                // 기록 오류 횟수
                quizRecord.setEmotionCnt(quizRecord.getEmotionCnt() + 1);

                // 오류횟수 카운팅
                quiz.setWrongCnt(quiz.getWrongCnt() + 1);
                break;
            case 2:
                // 2번 퀴즈 로직
                // 점수
                quizRecord.setInterestPoint(quizRecord.getInterestPoint() - 1); // 타인 관심

                // 기록 오류 횟수
                quizRecord.setInterestCnt(quizRecord.getInterestCnt() + 1);

                // 오류횟수 카운팅
                quiz.setWrongCnt(quiz.getWrongCnt() + 1);

                break;
            case 3:
                // 3번 퀴즈 로직
                // 점수
                quizRecord.setContextPoint(quizRecord.getContextPoint() - 1); // 맥락 이해

                // 기록 오류 횟수
                quizRecord.setContextCnt(quizRecord.getContextCnt() + 1);

                // 오류횟수 카운팅
                quiz.setWrongCnt(quiz.getWrongCnt() + 1);
                break;
            case 4:
                // 4번 퀴즈 로직
                // 점수
                quizRecord.setInterestPoint(quizRecord.getInterestPoint() - 2);
                quizRecord.setEmotionPoint(quizRecord.getEmotionPoint() - 1);

                // 기록 오류 횟수
                quizRecord.setInterestCnt(quizRecord.getInterestCnt() + 1);
                quizRecord.setEmotionCnt(quizRecord.getEmotionCnt() + 1);

                // 오류횟수 카운팅
                quiz.setWrongCnt(quiz.getWrongCnt() + 1);
                break;
            case 5:
                // 첫번째 선택지 목줄

                if (!fiveAnswer[0].equals(req.answer1())) {
                    // 점수
                    quizRecord.setContextPoint(quizRecord.getContextPoint() - 2);

                    // 기록 오류 횟수
                    quizRecord.setContextCnt(quizRecord.getContextCnt() + 1);
                }
                // 두번째 선택지
                if (req.answer2().equals("무서울 수 있지. 근데 그런 건 네가 혼자서 이겨내야 해") || req.answer2().equals("겨우 이런게 무서운거야?")) {
                    // 점수
                    quizRecord.setEmotionPoint(quizRecord.getEmotionPoint() + 2);
                    quizRecord.setSympathyPoint(quizRecord.getSympathyPoint() - 2); // 공감

                    // 기록 오류 횟수
                    quizRecord.setSympathyCnt(quizRecord.getSympathyCnt() + 1);

                } else if (req.answer2().equals("그만 떨고 조용히 좀 해")) {
                    // 점수
                    quizRecord.setEmotionPoint(quizRecord.getEmotionPoint() - 1);
                    quizRecord.setSympathyPoint(quizRecord.getSympathyPoint() - 2); // 공감

                    // 기록 오류 횟수
                    quizRecord.setEmotionCnt(quizRecord.getEmotionCnt() + 1);
                    quizRecord.setSympathyCnt(quizRecord.getSympathyCnt() + 1);

                } else if (req.answer2().equals("이 개 너무 귀엽지 않아?")) {
                    // 점수
                    quizRecord.setEmotionPoint(quizRecord.getEmotionPoint() - 1);
                    quizRecord.setSympathyPoint(quizRecord.getSympathyPoint() - 2); // 공감
                    quizRecord.setContextPoint(quizRecord.getContextPoint() - 3); // 맥락 이해

                    // 기록 오류 횟수
                    quizRecord.setEmotionCnt(quizRecord.getEmotionCnt() + 1);
                    quizRecord.setSympathyCnt(quizRecord.getSympathyCnt() + 1);
                    quizRecord.setContextCnt(quizRecord.getContextCnt() + 1);
                }

                // 오류횟수 카운팅
                quiz.setWrongCnt(quiz.getWrongCnt() + 1);
                break;
        }
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
