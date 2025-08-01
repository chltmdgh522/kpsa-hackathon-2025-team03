package game3.domain.oauth2.application.service;

import game3.domain.oauth2.presentation.dto.response.oatuh.OAuth2TokenResponse;
import game3.domain.oauth2.presentation.dto.response.oatuh.OAuth2UserResponse;

import java.util.Map;

public interface KakaoUserCreateService {
    Map<String, String> createKakaoUser(OAuth2TokenResponse oAuth2TokenResponse, OAuth2UserResponse oAuth2UserResponse);
}
