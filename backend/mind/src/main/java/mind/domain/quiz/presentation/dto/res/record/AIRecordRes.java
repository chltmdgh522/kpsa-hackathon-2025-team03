package game3.domain.quiz.presentation.dto.res.record;

public record AIRecordRes(
        String content
) {
    public static AIRecordRes of(String content) {
        return new AIRecordRes(content);
    }
}
