package game3.domain.oauth2.application.service;

public interface KakaoTokenService {
    String getValidAccessToken(String userId);
}
