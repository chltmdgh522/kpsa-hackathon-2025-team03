package mind.domain.user.application.service.impl;


import mind.domain.quiz.domain.entity.Quiz;
import mind.domain.quiz.domain.entity.QuizRecord;
import mind.domain.quiz.domain.repository.QuizRecordRepository;
import mind.domain.quiz.domain.repository.QuizRepository;
import mind.domain.user.application.service.UserService;
import mind.domain.user.domain.entity.User;
import mind.domain.user.domain.repository.UserRepository;
import mind.domain.user.presentation.dto.req.NicknameReq;
import mind.domain.user.presentation.dto.res.GetMyPageRes;
import mind.domain.user.presentation.dto.res.GetNicknameRes;
import mind.global.infra.exception.error.MindException;
import mind.global.infra.exception.error.ErrorCode;
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
