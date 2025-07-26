package game3.domain.quiz.presentation.dto.req;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Duration;

public record AnswerQuizReq(
        Long quizId,

        Long imageId,

        String answer1,

        String answer2, // 얘는 5번용 5번 아니면 빈칸으로 와두됨


        @JsonFormat(shape = JsonFormat.Shape.STRING)
        Duration time
){
}
