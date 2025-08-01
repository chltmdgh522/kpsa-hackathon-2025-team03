package game3.domain.oauth2.application.service;

import game3.domain.oauth2.presentation.dto.response.oatuh.OAuth2TokenResponse;

public interface KakaoAccessTokenAndRefreshTokenService {
    OAuth2TokenResponse getAccessTokenAndRefreshToken(String code);
    OAuth2TokenResponse refreshAccessToken(String refreshToken);
}
