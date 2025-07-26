package game3.domain.quiz.domain.repository;

import game3.domain.quiz.domain.entity.Quiz;
import game3.domain.quiz.domain.entity.QuizRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    Optional<Quiz> findByQuizRecordAndQuizNumber(QuizRecord quizRecord, Long quizNumber);

    List<Quiz> findByQuizRecord(QuizRecord quizRecord);
}
