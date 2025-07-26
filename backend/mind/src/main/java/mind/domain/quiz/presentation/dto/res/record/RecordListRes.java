package game3.domain.quiz.presentation.dto.res.record;

import java.time.LocalDateTime;

public record RecordListRes(

        LocalDateTime createdAt,

        Double average,

        Long quizRecordId

) {

    public static RecordListRes of(LocalDateTime createdAt, Double average, Long quizRecordId){
        return new RecordListRes(createdAt, average, quizRecordId);
    }
}
