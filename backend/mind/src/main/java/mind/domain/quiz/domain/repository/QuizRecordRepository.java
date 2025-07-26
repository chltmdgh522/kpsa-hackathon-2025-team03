package game3.domain.quiz.domain.repository;

import game3.domain.quiz.domain.entity.Quiz;
import game3.domain.quiz.domain.entity.QuizRecord;
import game3.domain.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizRecordRepository extends JpaRepository<QuizRecord, Long> {

    List<QuizRecord> findByUser(User user);
}
