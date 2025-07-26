package game3.domain.s3.domain.repository;

import game3.domain.s3.domain.entity.S3Audio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface S3AudioRepository extends JpaRepository<S3Audio, Long> {

    List<S3Audio> findByName(String name);
}
