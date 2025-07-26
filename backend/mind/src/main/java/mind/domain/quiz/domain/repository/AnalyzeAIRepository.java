package game3.domain.quiz.domain.repository;

import game3.domain.quiz.domain.entity.AnalyzeAI;
import game3.domain.quiz.domain.entity.QuizRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnalyzeAIRepository extends JpaRepository<AnalyzeAI, Long> {
    Optional<AnalyzeAI> findByQuizRecord(QuizRecord quizRecord);

}
