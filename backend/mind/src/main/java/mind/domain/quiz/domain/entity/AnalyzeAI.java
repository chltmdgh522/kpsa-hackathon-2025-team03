package game3.domain.quiz.domain.entity;

import game3.domain.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DynamicUpdate
public class AnalyzeAI {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_record_id")
    private QuizRecord quizRecord;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "analyze_ai_contents", joinColumns = @JoinColumn(name = "analyze_ai_id"))
    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private List<String> contents = new ArrayList<>();


    @Builder
    public AnalyzeAI(QuizRecord quizRecord, List<String> contents) {
        this.quizRecord = quizRecord;
        this.contents = contents;
    }

}
