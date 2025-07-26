package game3.domain.quiz.presentation.dto.res.record;

import java.time.Duration;

public record EndingRes(
        Duration allTime,

        Long quiz1,

        Long quiz2,

        Long quiz3,

        Long quiz4,

        Long quiz5
) {
    public static EndingRes of(Duration allTime, Long quiz1, Long quiz2, Long quiz3, Long quiz4, Long quiz5) {
        return new EndingRes(allTime, quiz1, quiz2, quiz3, quiz4, quiz5);
    }
}
