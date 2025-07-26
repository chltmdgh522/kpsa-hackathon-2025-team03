package game3.domain.quiz.presentation.dto.res.record.personal;

import java.time.Duration;

public record TimeRes(
        Duration allTime,

        Duration quiz1Time,

        Duration quiz2Time,

        Duration quiz3Time,

        Duration quiz4Time,

        Duration quiz5Time

) {
    public static TimeRes of(Duration allTime, Duration quiz1Time, Duration quiz2Time, Duration quiz3Time,
                              Duration quiz4Time, Duration quiz5Time) {
        return new TimeRes(allTime, quiz1Time, quiz2Time, quiz3Time, quiz4Time, quiz5Time);
    }

}
