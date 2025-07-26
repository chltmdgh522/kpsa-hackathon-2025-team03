package game3.domain.quiz.domain.entity;

import game3.domain.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DynamicUpdate
public class QuizRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "quizRecord", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Quiz> quizzs = new ArrayList<>();

    @OneToOne(mappedBy = "quizRecord", cascade = CascadeType.ALL, orphanRemoval = true)
    private AnalyzeAI analyzeAI;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Long emotionPoint = 0L; // 감정인식 점수

    @Column(nullable = false)
    private Long interestPoint = 0L; // 타인 관심

    @Column(nullable = false)
    private Long contextPoint = 0L; // 맥락 이해

    @Column(nullable = false)
    private Long sympathyPoint = 3L; // 공감 능력 기본 점수 3

    @Column(nullable = false)
    private Long emotionCnt = 0L; // 감정인식 오답

    @Column(nullable = false)
    private Long interestCnt = 0L;

    @Column(nullable = false)
    private Long contextCnt = 0L;

    @Column(nullable = false)
    private Long sympathyCnt = 0L;

    @Column(nullable = false)
    private boolean isComplete; // 퀴즈들 전체 완료


    @Builder
    public QuizRecord(User user,
                      boolean isComplete) {
        this.user = user;
        this.isComplete = isComplete;
    }


}
