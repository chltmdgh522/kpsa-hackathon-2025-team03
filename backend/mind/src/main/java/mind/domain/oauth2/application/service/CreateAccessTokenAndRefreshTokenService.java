package game3.domain.oauth2.application.service;

import game3.domain.user.domain.entity.Role;

import java.util.Map;

public interface CreateAccessTokenAndRefreshTokenService {
    Map<String, String> createAccessTokenAndRefreshToken(String userId, Role role, String email);
}
