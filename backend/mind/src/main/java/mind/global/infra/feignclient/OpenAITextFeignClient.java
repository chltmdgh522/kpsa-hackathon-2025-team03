package game3.global.infra.feignclient;

import game3.domain.openai.dto.openai.OpenAITextRequest;
import game3.domain.openai.dto.openai.OpenAITextResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "OpenAITextAPI",
        url = "${openai.api.url}"
)
public interface OpenAITextFeignClient {
    @PostMapping(value = "/v1/chat/completions")
    OpenAITextResponse generateText(
            @RequestHeader("Authorization") String apiKey,
            @RequestBody OpenAITextRequest request
    );
}