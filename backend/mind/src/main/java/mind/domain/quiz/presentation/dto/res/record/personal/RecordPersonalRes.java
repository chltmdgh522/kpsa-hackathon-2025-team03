package game3.domain.quiz.presentation.dto.res.record.personal;

import java.time.Duration;
import java.time.LocalDateTime;

public record RecordPersonalRes(

        String name,

        LocalDateTime createdAt,

        TimeRes timeRes,

        PointRes pointRes

) {

    public static RecordPersonalRes of(String name, LocalDateTime createdAt, TimeRes timeRes, PointRes pointRes) {
        return new RecordPersonalRes(name, createdAt, timeRes, pointRes);
    }
}
