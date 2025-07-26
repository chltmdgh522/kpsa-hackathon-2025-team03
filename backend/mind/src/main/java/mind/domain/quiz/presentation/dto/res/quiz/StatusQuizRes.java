package game3.domain.quiz.presentation.dto.res.quiz;

public record StatusQuizRes(
        boolean isStatus,
        // 퀴즈 번호 생성 테이블 컬럼 pk
        Long quizId,

        // 이건 퀴즈 1~5개의 테이블 아이디 위와 다름
        Long quizRecordId,

        Long imageId,

        String imageUrl,

        String audioUrl,

        Long quizNumber
) {
    public static StatusQuizRes of(boolean isStatus, Long quizId, Long quizRecordId, Long imageId, String imageUrl, String audioUrl, Long quizNumber) {
        return new StatusQuizRes(isStatus, quizId, quizRecordId, imageId, imageUrl, audioUrl, quizNumber);
    }
}