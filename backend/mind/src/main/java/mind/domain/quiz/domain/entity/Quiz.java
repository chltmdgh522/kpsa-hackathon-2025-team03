package game3.domain.quiz.domain.entity;

import game3.domain.quiz.domain.entity.convert.DurationStringConverter;
import game3.domain.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DynamicUpdate
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_record_id")
    private QuizRecord quizRecord;

    @Column(nullable = false)
    private boolean isComplete;

    @Column(nullable = false)
    private Long quizNumber;

    @Column(nullable = false)
    @Convert(converter = DurationStringConverter.class)
    private Duration quizTime = Duration.ZERO;

    // 오답 횟수
    @Column(nullable = false)
    private Long wrongCnt;

    @Builder
    public Quiz(User user,
                QuizRecord quizRecord,
                boolean isComplete,
                Long quizNumber,
                Long wrongCnt) {
        this.user = user;
        this.quizRecord = quizRecord;
        this.isComplete = isComplete;
        this.quizNumber = quizNumber;
        this.wrongCnt = wrongCnt;
    }
}
