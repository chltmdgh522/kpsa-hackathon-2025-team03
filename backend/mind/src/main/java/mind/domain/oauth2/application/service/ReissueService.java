package game3.domain.oauth2.application.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface ReissueService {
    void reissue(HttpServletRequest request, HttpServletResponse response);
}
