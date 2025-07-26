package game3.domain.quiz.application.service.impl;

import game3.domain.quiz.application.service.RecordService;
import game3.domain.quiz.domain.entity.AnalyzeAI;
import game3.domain.quiz.domain.entity.Quiz;
import game3.domain.quiz.domain.entity.QuizRecord;
import game3.domain.quiz.domain.repository.AnalyzeAIRepository;
import game3.domain.quiz.domain.repository.QuizRecordRepository;
import game3.domain.quiz.domain.repository.QuizRepository;
import game3.domain.quiz.presentation.dto.res.record.AIRecordRes;
import game3.domain.quiz.presentation.dto.res.record.EndingRes;
import game3.domain.quiz.presentation.dto.res.record.RecordListRes;
import game3.domain.quiz.presentation.dto.res.record.personal.PointRes;
import game3.domain.quiz.presentation.dto.res.record.personal.RecordPersonalRes;
import game3.domain.quiz.presentation.dto.res.record.personal.TimeRes;
import game3.domain.user.domain.entity.User;
import game3.domain.user.domain.repository.UserRepository;
import game3.global.infra.exception.error.ErrorCode;
import game3.global.infra.exception.error.MindException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class RecordServiceImpl implements RecordService {
    private final UserRepository userRepository;
    private final QuizRepository quizRepository;
    private final QuizRecordRepository quizRecordRepository;
    private final AnalyzeAIRepository aiRepository;

    @Override
    public List<RecordListRes> recordList(String userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) throw new MindException(ErrorCode.USER_NOT_EXIST);

        List<QuizRecord> byQuizRecords = quizRecordRepository.findByUser(user);
        List<RecordListRes> result = new ArrayList<>();
        for (QuizRecord byQuizRecord : byQuizRecords) {
            // 퀴즈 전체 완료한것만 반환
            if (byQuizRecord.isComplete()) {
                long sum = byQuizRecord.getContextPoint()
                        + byQuizRecord.getEmotionPoint()
                        + byQuizRecord.getInterestPoint()
                        + byQuizRecord.getSympathyPoint();

                Double average = sum / 4.0;
                result.add(RecordListRes.of(byQuizRecord.getCreatedAt(), average, byQuizRecord.getId()));
            }
        }

        return result;
    }

    @Override
    public RecordPersonalRes recordPersonal(String userId, Long quizRecordId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) throw new MindException(ErrorCode.USER_NOT_EXIST);

        QuizRecord quizRecord = quizRecordRepository.findById(quizRecordId).orElse(null);
        if (quizRecord == null) throw new MindException(ErrorCode.QUIZ_RECOMMEND_NOT_EXIST);


        PointRes pointRes = PointRes.of(quizRecord);
        List<Duration> temp = new ArrayList<>();
        for (int quizNumber = 1; quizNumber <= 5; quizNumber++) {
            Quiz quiz = quizRepository.findByQuizRecordAndQuizNumber(quizRecord, (long) quizNumber).orElse(null);
            if (quiz == null) throw new MindException(ErrorCode.QUIZ_NOT_EXIST); // 없으면 개망... ㅋㅋㅋㅋㅋㅋㅋ

            temp.add(quiz.getQuizTime());
        }

        Duration allTime = Duration.ZERO;

        for (Duration duration : temp) {
            allTime = allTime.plus(duration);
        }
        TimeRes timeRes = TimeRes.of(allTime, temp.get(0), temp.get(1), temp.get(2), temp.get(3), temp.get(4));
        return RecordPersonalRes.of(user.getName(), quizRecord.getCreatedAt(), timeRes, pointRes);
    }

    @Override
    public List<AIRecordRes> aiRecordPersonal(String userId, Long quizRecordId) {

        QuizRecord quizRecord = quizRecordRepository.findById(quizRecordId).orElse(null);
        if (quizRecord == null) throw new MindException(ErrorCode.QUIZ_RECOMMEND_NOT_EXIST);


        AnalyzeAI analyzeAI = aiRepository.findByQuizRecord(quizRecord).orElse(null);
        if (analyzeAI == null) throw new MindException(ErrorCode.AI_NOT_EXIST);

        List<AIRecordRes> result = new ArrayList<>();

        List<String> contents = analyzeAI.getContents();

        for (String content : contents) {
            result.add(AIRecordRes.of(content));
        }
        return result;
    }

    @Override
    public EndingRes recordEnding(String userId, Long quizRecordId) {

        QuizRecord quizRecord = quizRecordRepository.findById(quizRecordId).orElse(null);
        if (quizRecord == null) throw new MindException(ErrorCode.QUIZ_RECOMMEND_NOT_EXIST);

        List<Quiz> byQuizzs = quizRepository.findByQuizRecord(quizRecord);


        Duration allTime = Duration.ZERO;
        List<Long> score = new ArrayList<>();
        for (Quiz byQuizz : byQuizzs) {
            score.add(byQuizz.getWrongCnt());
            allTime = allTime.plus(byQuizz.getQuizTime());
        }
        return EndingRes.of(allTime, score.get(0), score.get(1), score.get(2), score.get(3), score.get(4));
    }
}
