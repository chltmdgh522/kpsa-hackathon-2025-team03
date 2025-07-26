package mind.global.jwt.domain.repository;


import mind.global.jwt.domain.entity.KakaoJsonWebToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KakaoJsonWebTokenRepository extends CrudRepository<KakaoJsonWebToken, String> {
}