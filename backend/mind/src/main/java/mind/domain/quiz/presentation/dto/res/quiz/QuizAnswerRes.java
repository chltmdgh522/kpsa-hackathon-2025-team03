package game3.domain.quiz.presentation.dto.res.quiz;

public record QuizAnswerRes(
        boolean answer,

        Long quizRecordId,

        Long quizId,

        String audioUrl // 2번 때문에 넣음 2번이 오답횟수 2회 미만이면 없고 1 3 4 5 는 아예 없음
) {
    public static QuizAnswerRes of(boolean answer, Long quizRecordId, Long quizId, String audioUrl) {
        return new QuizAnswerRes(answer, quizRecordId, quizId, audioUrl);
    }
}
