package game3.domain.user.application.service;


import game3.domain.user.presentation.dto.req.NicknameReq;
import game3.domain.user.presentation.dto.res.GetMyPageRes;
import game3.domain.user.presentation.dto.res.GetNicknameRes;

public interface UserService {

    void updateNickname(String userId, NicknameReq req);

    GetNicknameRes getUser(String userId);


    GetMyPageRes getMyPage(String userId);


}