package game3.global.infra.exception.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /**
     * 에러코드 규약
     * HTTP Status Code는 에러에 가장 유사한 코드를 부여한다.
     * 사용자정의 에러코드는 음수를 사용한다.
     * 사용자정의 에러코드는 중복되지 않게 배정한다.
     * 사용자정의 에러코드는 각 카테고리 별로 100단위씩 끊어서 배정한다. 단, Common 카테고리는 -100 단위를 고정으로 가져간다.
     */

    /**
     * 401 : 미승인
     * 403 : 권한의 문제가 있을때
     * 406 : 객체가 조회되지 않을 때
     * 409 : 현재 데이터와 값이 충돌날 때(ex. 아이디 중복)
     * 412 : 파라미터 값이 뭔가 누락됐거나 잘못 왔을 때
     * 422 : 파라미터 문법 오류
     * 424 : 뭔가 단계가 꼬였을때, 1번안하고 2번하고 그런경우
     */

    // Common
    SERVER_UNTRACKED_ERROR(-100, "미등록 서버 에러입니다. 서버 팀에 연락주세요.", 500),
    OBJECT_NOT_FOUND(-101, "조회된 객체가 없습니다.", 406),
    INVALID_PARAMETER(-102, "잘못된 파라미터입니다.", 422),
    PARAMETER_VALIDATION_ERROR(-103, "파라미터 검증 에러입니다.", 422),
    PARAMETER_GRAMMAR_ERROR(-104, "파라미터 문법 에러입니다.", 422),

    //Auth
    UNAUTHORIZED(-200, "인증 자격이 없습니다.", 401),
    FORBIDDEN(-201, "권한이 없습니다.", 403),
    JWT_ERROR_TOKEN(-202, "잘못된 토큰입니다.", 401),
    JWT_EXPIRE_TOKEN(-203, "만료된 토큰입니다.", 401),
    AUTHORIZED_ERROR(-204, "인증 과정 중 에러가 발생했습니다.", 500),
    INVALID_ACCESS_TOKEN(-205, "Access Token이 유효하지 않습니다.", 401),
    JWT_UNMATCHED_CLAIMS(-206, "토큰 인증 정보가 일치하지 않습니다", 401),
    INVALID_REFRESH_TOKEN(-207, "Refresh Token이 유효하지 않습니다.", 401),
    REFRESH_TOKEN_NOT_EXIST(-208, "Refresh Token이 DB에 존재하지 않습니다.", 401),
    DUPLICATE_LOGIN_NOT_EXIST(-209, "중복 로그인은 허용되지 않습니다.", 401),

    // user
    INVALID_ROLE(-210, "해당 역할이 존재하지 않습니다.", 400),
    USER_NOT_EXIST(-211, "존재하지 않는 유저입니다.", 404),
    ALLERGY_NOT_EXIST(-212, "존재하지 않는 필드입니다.", 400),

    // group
    QUIZ_NOT_EXIST(-300, "존재하지 않는 퀴즈입니다.", 404),

    // food
    QUIZ_RECOMMEND_NOT_EXIST(-400, "존재하지 않는 퀴즈전체 기록입니다.", 404),
    AI_NOT_EXIST(-410, "존재하지 않는 AI 기록입니다.", 404),
    RESTAURANT_NOT_EXIST(-401, "존재하지 않는 음식점입니다.", 404),
    JSON_MAPPING_FAIL(-402, "JSON 매핑 실패입니다.", 422),
    IMAGE_MAPPING_FAIL(-403, "이미지를 갖고 오지 못했습니다.", 422),
    FOOD_NAME_DISTINCT(-404, "음식 이름이 중복됩니다.", 409),
    INVALID_CATEGORY(-405, "존재하지 않는 카테고리입니다.", 400),

    // openAI
    OPENAI_NOT_EXIST(-500, "내용을 생성할 수 없습니다.", 500),

    //file
    FILE_NOT_EXIST(-600, "존재하지 않는 이미지입니다.", 404);

    private final int code;
    private final String message;
    private final int httpCode;
}