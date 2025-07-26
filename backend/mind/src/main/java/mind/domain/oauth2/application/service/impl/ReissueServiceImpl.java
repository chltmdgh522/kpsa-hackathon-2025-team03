package game3.domain.oauth2.application.service.impl;

import game3.domain.oauth2.application.service.ReissueService;
import game3.domain.user.domain.entity.Role;
import game3.global.infra.exception.error.MindException;
import game3.global.infra.exception.error.ErrorCode;
import game3.global.jwt.domain.entity.JsonWebToken;
import game3.global.jwt.domain.repository.JsonWebTokenRepository;
import game3.global.jwt.util.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ReissueServiceImpl implements ReissueService {

    private final JWTUtil jwtUtil;
    private final JsonWebTokenRepository jsonWebTokenRepository;

    @Override
    public void reissue(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = jwtUtil.getRefreshTokenFromCookies(request);

        if(!jwtUtil.jwtVerify(refreshToken, "refresh")) {
            log.info("Refresh token not valid");
            throw new MindException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        JsonWebToken jsonWebToken = jsonWebTokenRepository.findById(refreshToken).orElse(null);

        if(jsonWebToken == null) {
            throw new MindException(ErrorCode.REFRESH_TOKEN_NOT_EXIST);
        }

        String userId = jsonWebToken.getProviderId();
        Role role = jsonWebToken.getRole();
        String email = jsonWebToken.getEmail();

        String newAccessToken = jwtUtil.createAccessToken(userId, role, email);
        String newRefreshToken = jwtUtil.createRefreshToken(userId, role, email);

        JsonWebToken newJsonWebToken = JsonWebToken.builder()
                .refreshToken(newRefreshToken)
                .email(email)
                .role(role)
                .build();

        jsonWebTokenRepository.delete(jsonWebToken);
        jsonWebTokenRepository.save(newJsonWebToken);

        response.addHeader("Authorization", "Bearer " + newAccessToken);
        response.addHeader(HttpHeaders.COOKIE, jwtUtil.createRefreshTokenCookie(newRefreshToken).toString());
    }
}
