package game3.domain.quiz.presentation.dto.res.record.personal;

import game3.domain.quiz.domain.entity.QuizRecord;
import jakarta.persistence.Column;

public record PointRes (
        Long emotionPoint,
        Long interestPoint,
        Long contextPoint,
        Long sympathyPoint,
        Long emotionCnt,
        Long interestCnt,
        Long contextCnt,
        Long sympathyCnt
) {
    public static PointRes of(QuizRecord quizRecord) {
        return new PointRes(
                quizRecord.getEmotionPoint(),
                quizRecord.getInterestPoint(),
                quizRecord.getContextPoint(),
                quizRecord.getSympathyPoint(),
                quizRecord.getEmotionCnt(),
                quizRecord.getInterestCnt(),
                quizRecord.getContextCnt(),
                quizRecord.getSympathyCnt()
        );
    }
}
