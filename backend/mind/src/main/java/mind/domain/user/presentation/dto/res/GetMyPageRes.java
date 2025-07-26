package game3.domain.user.presentation.dto.res;

import java.time.Duration;

public record GetMyPageRes(
        String name,

        String profile,

        Duration allTime,

        Long allCnt
) {
    public static GetMyPageRes of(String name, String profile, Duration allTime, Long allCnt) {
        return new GetMyPageRes(name, profile, allTime, allCnt);
    }
}
