package game3.domain.user.presentation.controller;

import game3.domain.user.application.service.UserService;
import game3.domain.user.presentation.dto.req.NicknameReq;
import game3.domain.user.presentation.dto.res.GetMyPageRes;
import game3.domain.user.presentation.dto.res.GetNicknameRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User", description = "닉네임 수정 API")
public class UserController {

    private final UserService userService;


    @Operation(summary = "닉넴 조회", description = "사용자 닉네임을 조회합니다.")
    @GetMapping
    public ResponseEntity<GetNicknameRes> getNickname(@AuthenticationPrincipal String userId) {
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @Operation(summary = "닉넴 수정", description = "사용자 닉네임을 수정합니다.")
    @PatchMapping
    public void updateNickname(@RequestBody @Valid NicknameReq req, @AuthenticationPrincipal String userId) {
        userService.updateNickname(userId, req);
    }

    @Operation(summary = "마이페이지", description = "사용자 마이페지 데이터를 줍니다.")
    @GetMapping("/mypage")
    public ResponseEntity<GetMyPageRes> getMyPage(@AuthenticationPrincipal String userId) {
        return ResponseEntity.ok(userService.getMyPage(userId));
    }

}