package mind.domain.quiz.application.service.impl;

import mind.domain.openai.application.service.OpenAITextService;
import mind.domain.quiz.domain.entity.AnalyzeAI;
import mind.domain.quiz.domain.entity.QuizRecord;
import mind.domain.quiz.domain.repository.AnalyzeAIRepository;
import mind.domain.quiz.domain.repository.QuizRecordRepository;
import mind.domain.user.domain.entity.User;
import mind.global.infra.exception.error.ErrorCode;
import mind.global.infra.exception.error.MindException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AnalyzeServiceImpl {

    private final OpenAITextService openAITextService;

    private final QuizRecordRepository quizRecordRepository;

    private final AnalyzeAIRepository analyzeAIRepository;

    @Async
    public void analyzeAIAsync(User user, QuizRecord quizRecord) {

        String prompt = createPrompt(user, quizRecord);
        log.info(prompt);

        String aiContent = openAITextService.quizAnalyze(prompt);
        log.info(aiContent);

        // 변환
        Map<Integer, String> integerStringMap = splitGptResponse(aiContent);
        List<String> content = List.of(integerStringMap.get(1), integerStringMap.get(2), integerStringMap.get(3), integerStringMap.get(4), integerStringMap.get(5));

        // 저장 ai 도메인에
        AnalyzeAI analyzeAI = AnalyzeAI.builder().quizRecord(quizRecord).contents(content).build();
        analyzeAIRepository.save(analyzeAI);


    }

    public void test(User user, QuizRecord quizRecord) {
        String prompt = createPrompt(user, quizRecord);
        log.info(prompt);
        String aiContent = openAITextService.quizAnalyze(prompt);
        log.info(aiContent);


//        Map<Integer, String> integerStringMap = splitGptResponse(aiContent);
//        List<String> content = List.of(integerStringMap.get(1), integerStringMap.get(2), integerStringMap.get(3), integerStringMap.get(4), integerStringMap.get(5));
//
//        // 저장 ai 도메인에
//        AnalyzeAI analyzeAI = AnalyzeAI.builder().quizRecord(quizRecord).contents(content).build();
//        analyzeAIRepository.save(analyzeAI);

    }


    private static String createPrompt(User user, QuizRecord quizRecord) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("이름: ").append(user.getName());
        prompt.append(" 발달 장애 아동 환자가 참여한 퀴즈 결과입니다. " +
                "이 결과는 발달장애 아동의 특성을 파악하기 위한 참고 자료이며, 점수는 항목별 8점 만점 기준입니다.\n\n");

        prompt.append("▶️ [점수 요약]\n");
        prompt.append("감정 인식 점수: ").append(quizRecord.getEmotionPoint()).append("\n");
        prompt.append("타인 관심 점수: ").append(quizRecord.getInterestPoint()).append("\n");
        prompt.append("맥락 이해 점수: ").append(quizRecord.getContextPoint()).append("\n");
        prompt.append("공감 능력 점수: ").append(quizRecord.getSympathyPoint()).append("\n\n");

        prompt.append("❗ [오답 횟수]\n");
        prompt.append("감정 인식 오답: ").append(quizRecord.getEmotionCnt()).append("회\n");
        prompt.append("타인 관심 오답: ").append(quizRecord.getInterestCnt()).append("회\n");
        prompt.append("맥락 이해 오답: ").append(quizRecord.getContextCnt()).append("회\n");
        prompt.append("공감 능력 오답: ").append(quizRecord.getSympathyCnt()).append("회\n\n");

        prompt.append("📌 위의 데이터를 기반으로 다음 다섯 가지 항목을 작성해 주세요:\n");
        prompt.append("1. 전반적인 발달 특성 요약\n");
        prompt.append("2. 강점으로 보이는 영역과 그 이유\n");
        prompt.append("3. 주의가 필요한 영역과 그 이유\n");
        prompt.append("4. 부족한 영역을 개선하기 위한 훈련 제안\n");
        prompt.append("5. 보호자 및 치료자가 일상에서 참고할 수 있는 행동 가이드\n\n");

        prompt.append("※ 각 항목은 반드시 '1.', '2.', '3.'처럼 번호로 시작해 주세요. 번호 뒤에는 질문을 반복하지 말고, 바로 답변을 작성해 주세요.\n");
        prompt.append("※ 응답에는 불필요한 특수문자나 별표(****)는 사용하지 말아 주세요.\n");
        prompt.append("※ 사용한 심리학 이론은 반드시 해당 이론의 정의와 창시자 또는 연도와 함께 설명해 주세요.\n");
        prompt.append("예: '사회인지 이론(Bandura, 1986)은 ...' 처럼요.\n");
        prompt.append("※ 5번 항목 마지막에 보호자나 치료자가 참고할 수 있는 책, 논문, 가이드라인 등을 최대 2~3개 제시해 주세요.\n");
        prompt.append("※ 전체 응답은 약 1300자 내외로 작성해 주세요.\n");
        prompt.append("※ 각 항목의 내용은 가능한 한 발달심리 및 아동 임상 분야의 연구나 통계 데이터를 바탕으로 작성해 주세요.\n");
        prompt.append("※ 응답은 따뜻하고 실질적인 조언의 말투로 작성해 주세요.");

        return prompt.toString();
    }

    public static Map<Integer, String> splitGptResponse(String response) {
        Map<Integer, String> result = new LinkedHashMap<>();
        String[] parts = response.split("(?=\\d+\\. )"); // 1. 2. 3. 형식 기준으로 split

        for (String part : parts) {
            if (part.trim().isEmpty()) continue;

            int idx = part.indexOf(". ");
            if (idx > 0) {
                try {
                    int key = Integer.parseInt(part.substring(0, idx).trim());
                    String value = part.substring(idx + 2).trim(); // 내용만 추출
                    result.put(key, value);
                } catch (NumberFormatException e) {
                    // 무시: "머신러닝" 이런 제목이 잘못 split되었을 경우 방지
                }
            }
        }

        return result;
    }


}
