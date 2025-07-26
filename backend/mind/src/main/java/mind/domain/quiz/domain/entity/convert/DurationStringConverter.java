package game3.domain.quiz.domain.entity.convert;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.time.Duration;

@Converter(autoApply = false)
public class DurationStringConverter implements AttributeConverter<Duration, String> {

    @Override
    public String convertToDatabaseColumn(Duration attribute) {
        return attribute == null ? null : attribute.toString();  // PT1M30S 저장
    }

    @Override
    public Duration convertToEntityAttribute(String dbData) {
        try {
            return (dbData == null || dbData.isBlank()) ? Duration.ZERO : Duration.parse(dbData);
        } catch (Exception e) {
            return Duration.ZERO;
        }
    }
}

