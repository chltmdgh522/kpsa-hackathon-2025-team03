package mind.domain.openai.application.service.impl;

import mind.domain.openai.application.service.OpenAITextService;
import mind.domain.openai.dto.openai.OpenAITextRequest;
import mind.domain.openai.dto.openai.OpenAITextResponse;
import mind.global.infra.exception.error.MindException;
import mind.global.infra.exception.error.ErrorCode;
import mind.global.infra.feignclient.OpenAITextFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OpenAITextServiceImpl implements OpenAITextService {

    private final OpenAITextFeignClient openAITextFeignClient;

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.text.model}")
    private String textModel;

    @Override
    public String quizAnalyze(String prompt) {
        try {
            List<OpenAITextRequest.Message> messages = new ArrayList<>();
            messages.add(OpenAITextRequest.Message.builder()
                    .role("system")
                    .content("당신은 발달장애 아동의 정서 및 사회성 발달을 평가하고 지도하는 전문 상담가입니다. " +
                            "데이터를 분석하여 아동의 강점과 어려움을 임상 심리학 및 발달심리학 이론에 근거해 설명하고, " +
                            "보호자와 치료자에게 실질적이고 따뜻한 조언을 제공하세요.")
                    .build());
            messages.add(OpenAITextRequest.Message.builder()
                    .role("user")
                    .content(prompt)
                    .build());

            OpenAITextRequest request = OpenAITextRequest.builder()
                    .model(textModel)
                    .messages(messages)
                    .temperature(0.7)
                    .maxTokens(1500)
                    .build();

            OpenAITextResponse response = openAITextFeignClient.generateText(
                    "Bearer " + apiKey,
                    request
            );

            if (response.getChoices() != null && !response.getChoices().isEmpty()) {
                return response.getChoices().get(0).getMessage().getContent();

            } else {
                throw new MindException(ErrorCode.OPENAI_NOT_EXIST);
            }
        } catch (Exception e) {
            log.error("에러 내용: {}", e.getMessage(), e);
            throw new MindException(ErrorCode.OPENAI_NOT_EXIST);
        }
    }







}