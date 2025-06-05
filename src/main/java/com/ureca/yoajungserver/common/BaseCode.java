package com.ureca.yoajungserver.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BaseCode {
    // common
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR_500",HttpStatus.INTERNAL_SERVER_ERROR,"예기치 못한 오류가 발생했습니다"),
    STATUS_OK("STATUS_OK_200", HttpStatus.OK, "서버가 정상적으로 동작 중입니다."),
    STATUS_AUTHENTICATED("STATUS_AUTHENTICATED_200", HttpStatus.OK, "로그인된 사용자입니다."),
    STATUS_UNAUTHORIZED("STATUS_UNAUTHORIZED_401", HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),

    // Plan
    PLAN_DETAIL_SUCCESS("FIND_PLAN_DETAIL_200", HttpStatus.OK, "요금제 상세 조회에 성공했습니다."),
    PLAN_LIST_SUCCESS("READ_PLAN_LIST_200", HttpStatus.OK, "요금제 목록 조회에 성공했습니다."),
    PLAN_NOT_FOUND("NOT_FOUND_PLAN_404", HttpStatus.NOT_FOUND, "해당 요금제를 찾을 수 없습니다."),

    // Review
    REVIEW_CREATE_SUCCESS("CREATE_REVIEW_201", HttpStatus.CREATED, "리뷰 생성에 성공했습니다."),
    REVIEW_UPDATE_SUCCESS("UPDATE_REVIEW_200", HttpStatus.OK, "리뷰 수정에 성공했습니다."),
    REVIEW_DELETE_SUCCESS("DELETE_REVIEW_200", HttpStatus.OK, "리뷰 삭제에 성공했습니다."),
    REVIEW_LIKE_SUCCESS("CREATE_REVIEW_LIKE_200", HttpStatus.OK, "리뷰 좋아요 등록에 성공했습니다."),
    REVIEW_LIKE_CANCELED("CANCELED_REVIEW_LIKE_200", HttpStatus.OK, "리뷰 좋아요 취소에 성공했습니다."),
    REVIEW_LIKE_DUPLICATED("DUPLICATED_REVIEW_LIKE_409", HttpStatus.CONFLICT, "중복된 리뷰 좋아요 요청입니다."),
    REVIEW_NOT_FOUND("NOT_FOUND_REVIEW_404", HttpStatus.NOT_FOUND, "해당 리뷰를 찾을 수 없습니다."),
    REVIEW_ALREADY_EXIST("ALREADY_EXIST_REVIEW_409", HttpStatus.CONFLICT, "이미 리뷰를 작성했습니다."),
    NOT_REVIEW_AUTHOR("NOT_REVIEW_AUTHOR_409", HttpStatus.CONFLICT, "리뷰 작성자만 접근 가능합니다."),
    REVIEW_NOT_ALLOWED("REVIEW_NOT_ALLOWED_409", HttpStatus.CONFLICT, "리뷰 작성 권한이 없습니다."),


    // User
    USER_SIGNUP_SUCCESS("SIGNUP_USER_201", HttpStatus.CREATED, "회원가입에 성공했습니다."),
    USER_NOT_FOUND("NOT_FOUND_USER_404", HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다."),

    // Chat
    CHAT_SAVE_SUCCESS("SAVE_CHAT_201", HttpStatus.CREATED, "채팅 저장에 성공했습니다."),
    KEYWORD_MAPPING_SUCCESS("MAPPING_KEYWORD_200", HttpStatus.OK, "키워드 맵핑에 성공했습니다."),
    KEYWORD_MAPPING_FAIL("MAPPING_KEYWORD_400", HttpStatus.BAD_REQUEST, "키워드 맵핑에 실패했습니다.");
    CHAT_BAD_WORD_DETECTED("BAD_WORD_CHAT_400", HttpStatus.BAD_REQUEST, "상담원에게 폭언이나 욕설을 하시면 안 됩니다.");

    private final String code;
    private final HttpStatus status;
    private final String message;
}
