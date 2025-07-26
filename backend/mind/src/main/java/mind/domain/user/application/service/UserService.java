package mind.domain.user.application.service;


import mind.domain.user.presentation.dto.req.NicknameReq;
import mind.domain.user.presentation.dto.res.GetMyPageRes;
import mind.domain.user.presentation.dto.res.GetNicknameRes;

public interface UserService {

    void updateNickname(String userId, NicknameReq req);

    GetNicknameRes getUser(String userId);


    GetMyPageRes getMyPage(String userId);


}