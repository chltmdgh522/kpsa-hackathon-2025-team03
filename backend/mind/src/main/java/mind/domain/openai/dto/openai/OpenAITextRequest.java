package mind.domain.openai.dto.openai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class OpenAITextRequest {
    private String model;
    private List<Message> messages;
    private double temperature;

    @JsonProperty("max_tokens")
    private Integer maxTokens;

    @Data
    @Builder
    public static class Message {
        private String role;
        private String content;
    }
}