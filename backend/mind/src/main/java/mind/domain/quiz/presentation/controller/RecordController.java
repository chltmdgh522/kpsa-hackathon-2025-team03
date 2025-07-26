package game3.domain.quiz.presentation.controller;

import game3.domain.quiz.application.service.RecordService;
import game3.domain.quiz.presentation.dto.res.record.AIRecordRes;
import game3.domain.quiz.presentation.dto.res.record.EndingRes;
import game3.domain.quiz.presentation.dto.res.record.RecordListRes;
import game3.domain.quiz.presentation.dto.res.record.personal.RecordPersonalRes;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/record")
@RequiredArgsConstructor
@Slf4j
public class RecordController {
    private final RecordService recordService;

    @Operation(description = "퀴즈 기록 리스트 API")
    @GetMapping
    public ResponseEntity<List<RecordListRes>> getRecordList(@AuthenticationPrincipal String userId) {

        return ResponseEntity.ok(recordService.recordList(userId));
    }

    @Operation(description = "퀴즈 개별 기록 API")
    @GetMapping("/{quizRecordId}")
    public ResponseEntity<RecordPersonalRes> getRecordList(
            @PathVariable Long quizRecordId,
            @AuthenticationPrincipal String userId) {
        return ResponseEntity.ok(recordService.recordPersonal(userId, quizRecordId));
    }

    @Operation(description = "AI 기록 API")
    @GetMapping("/ai/{quizRecordId}")
    public ResponseEntity<List<AIRecordRes>> getRecordAI(
            @PathVariable Long quizRecordId,
            @AuthenticationPrincipal String userId) {
        return ResponseEntity.ok(recordService.aiRecordPersonal(userId, quizRecordId));
    }


    @Operation(description = "엔딩 후 불러오기 API")
    @GetMapping("/ending/{quizRecordId}")
    public ResponseEntity<EndingRes> getRecordEnding(
            @PathVariable Long quizRecordId,
            @AuthenticationPrincipal String userId) {
        return ResponseEntity.ok(recordService.recordEnding(userId, quizRecordId));
    }
}
