package game3.domain.openai.dto.naver;

import java.util.List;
// 네이버 지역 검색 전체 응답
public record LocalSearchResponse(
        String lastBuildDate,
        int total,
        int start,
        int display,
        List<LocalSearchItem> items
) {}
