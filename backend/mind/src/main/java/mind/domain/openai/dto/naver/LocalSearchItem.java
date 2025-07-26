package game3.domain.openai.dto.naver;

// 네이버 지역 검색 1건
public record LocalSearchItem(
        String title,
        String link,
        String category,
        String description,
        String address,
        String roadAddress,
        String mapx,  // 경도
        String mapy   // 위도
) {
}