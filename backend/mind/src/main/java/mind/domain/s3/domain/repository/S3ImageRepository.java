package game3.domain.s3.domain.repository;

import game3.domain.s3.domain.entity.S3Audio;
import game3.domain.s3.domain.entity.S3Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface S3ImageRepository extends JpaRepository<S3Image, Long> {
    List<S3Image> findByQuizNumber(Long quizNumber);
}
