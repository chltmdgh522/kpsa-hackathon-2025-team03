package game3.domain.user.application.service.impl;


import game3.domain.quiz.domain.entity.Quiz;
import game3.domain.quiz.domain.entity.QuizRecord;
import game3.domain.quiz.domain.repository.QuizRecordRepository;
import game3.domain.quiz.domain.repository.QuizRepository;
import game3.domain.user.application.service.UserService;
import game3.domain.user.domain.entity.User;
import game3.domain.user.domain.repository.UserRepository;
import game3.domain.user.presentation.dto.req.NicknameReq;
import game3.domain.user.presentation.dto.res.GetMyPageRes;
import game3.domain.user.presentation.dto.res.GetNicknameRes;
import game3.global.infra.exception.error.MindException;
import game3.global.infra.exception.error.ErrorCode;
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
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final QuizRecordRepository quizRecordRepository;

    private final QuizRepository quizRepository;

    @Override
    public void updateNickname(String userId, NicknameReq req) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) throw new MindException(ErrorCode.USER_NOT_EXIST);

        user.updateName(req.nickname());
    }

    @Override
    public GetNicknameRes getUser(String userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) throw new MindException(ErrorCode.USER_NOT_EXIST);

        return GetNicknameRes.of(user);
    }

    @Override
    public GetMyPageRes getMyPage(String userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) throw new MindException(ErrorCode.USER_NOT_EXIST);

        List<QuizRecord> byUserQuizRecords = quizRecordRepository.findByUser(user);

        Long allCnt = (long) byUserQuizRecords.size(); // 총 퀴즈 전체 횟수
        Duration allTime = Duration.ZERO; // 퀴즈 전체 시간

        for (QuizRecord quizRecord : byUserQuizRecords) {
            List<Long> score = new ArrayList<>();
            List<Quiz> byQuizzs = quizRepository.findByQuizRecord(quizRecord);
            for (Quiz byQuizz : byQuizzs) {
                score.add(byQuizz.getWrongCnt());
                allTime = allTime.plus(byQuizz.getQuizTime());
            }

        }
        return GetMyPageRes.of(user.getName(), user.getProfile(), allTime, allCnt);
    }


}
