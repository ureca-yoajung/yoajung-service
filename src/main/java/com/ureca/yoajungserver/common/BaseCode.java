package com.ureca.yoajungserver.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BaseCode {
    // common
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR_500", HttpStatus.INTERNAL_SERVER_ERROR, "예기치 못한 오류가 발생했습니다"),
    INVALID_REQUEST("INVALID_REQUEST_400", HttpStatus.BAD_REQUEST, "요청값이 잘못됐습니다"),
    STATUS_OK("STATUS_OK_200", HttpStatus.OK, "서버가 정상적으로 동작 중입니다."),
    STATUS_CREATED("STATUS_CREATED_201", HttpStatus.CREATED, "정삭적으로 생성됐습니다"),
    STATUS_UNAUTHORIZED("STATUS_UNAUTHORIZED_401", HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),
    STATUS_FORBIDDEN("STATUS_FORBIDDEN_403", HttpStatus.FORBIDDEN, "권한이 없니다."),

    // user/singup
    USER_SIGNUP_SUCCESS("USER_SIGNUP_SUCCESS_201", HttpStatus.CREATED, "회원가입 됐습니다"),
    USER_NOT_FOUND("NOT_FOUND_USER_404", HttpStatus.NOT_FOUND, "유저가 없습니다"),
    USER_DUPLICATED_EMAIL("USER_DUPLICATED_EMAIL_409", HttpStatus.CONFLICT, "이미 가입된 이메일입니다."),
    USER_DUPLICATED_PHONE_NUMBER("USER_DUPLICATED_PHONE_NUMBER_409", HttpStatus.CONFLICT, "이미 가입된 전화번호입니다"),

    // auth
    EMAIL_CODE_SENT("EMAIL_CODE_SENT_200", HttpStatus.OK, "인증 코드 발송에 성공했습니다."),
    EMAIL_CODE_EXPIRED("EMAIL_CODE_EXPIRED_400", HttpStatus.BAD_REQUEST, "인증 코드가 만료되었습니다."),
    EMAIL_CODE_MISMATCH("EMAIL_CODE_MISMATCH_400", HttpStatus.BAD_REQUEST, "인증 코드가 일치하지 않습니다."),
    EMAIL_ALREADY_VERIFIED("EMAIL_ALREADY_VERIFIED_409", HttpStatus.CONFLICT, "이미 인증된 이메일입니다."),

    EMAIL_VERIFICATION_SUCCESS("EMAIL_VERIFICATION_SUCCESS_200", HttpStatus.OK, "이메일 인증에 성공했습니다."),

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
    REVIEW_BAD_WORD_DETECTED("BAD_WORD_REVIEW_400", HttpStatus.BAD_REQUEST, "욕설/모욕이 포함된 리뷰를 게시할 수 없습니다."),


    // Chat
    CHAT_SAVE_SUCCESS("SAVE_CHAT_201", HttpStatus.CREATED, "채팅 저장에 성공했습니다."),
    KEYWORD_MAPPING_SUCCESS("MAPPING_KEYWORD_200", HttpStatus.OK, "키워드 맵핑에 성공했습니다."),
    KEYWORD_MAPPING_FAIL("MAPPING_KEYWORD_400", HttpStatus.BAD_REQUEST, "키워드 맵핑에 실패했습니다."),
    CHAT_BAD_WORD_DETECTED("BAD_WORD_CHAT_400", HttpStatus.BAD_REQUEST, "상담원에게 폭언이나 욕설을 하시면 안 됩니다.");

    private final String code;
    private final HttpStatus status;
    private final String message;
}
