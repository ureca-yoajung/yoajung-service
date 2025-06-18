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

    // user/signup
    USER_FIND_SUCCESS("USER_FIND_SUCCESS_200", HttpStatus.OK, " 회원 정보 조회했습니다"),
    USER_UPDATED_SUCCESS("USER_FIND_SUCCESS_200", HttpStatus.OK, " 회원 정보 수정했습니다"),
    USER_SIGNUP_SUCCESS("USER_SIGNUP_SUCCESS_201", HttpStatus.CREATED, "회원가입 됐습니다"),
    USER_NOT_FOUND("USER_NOT_FOUND_404", HttpStatus.NOT_FOUND, "유저가 없습니다"),
    USER_DUPLICATED_EMAIL("USER_DUPLICATED_EMAIL_409", HttpStatus.CONFLICT, "이미 가입된 이메일입니다."),
    USER_DUPLICATED_PHONE_NUMBER("USER_DUPLICATED_PHONE_NUMBER_409", HttpStatus.CONFLICT, "이미 가입된 전화번호입니다"),

    // login
    USER_LOGIN_SUCCESS("USER_LOGIN_SUCCESS_200", HttpStatus.OK, "로그인에 성공했습니다."),
    USER_LOGIN_FAIL("USER_LOGIN_FAIL_401", HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 올바르지 않습니다."),
    USER_LOGOUT_SUCCESS("USER_LOGOUT_SUCCESS_200", HttpStatus.OK, "로그아웃에 성공했습니다."),
    USER_SESSION_EXPIRED("USER_SESSION_EXPIRED_401", HttpStatus.UNAUTHORIZED, "세션이 만료됐습니다. 다시 로그인해주세요."),
    USER_ADDITIONAL_INFO_REQUIRED("USER_ADDITIONAL_INFO_REQUIRED_200", HttpStatus.OK, "로그인을 위한 추가 정보기입이 필요합니다"),

    // tendency
    TENDENCY_ALREADY_EXISTS("TENDENCY_ALREADY_EXISTS_409", HttpStatus.CONFLICT, "이미 등록된 성향입니다"),
    TENDENCY_CREATED_SUCCESS("TENDENCY_CREATED_SUCCESS_201", HttpStatus.CREATED, "성향을 등록했습니다"),
    TENDENCY_UPDATED_SUCCESS("TENDENCY_UPDATED_SUCCESS_200", HttpStatus.OK, "성향을 수정했습니다"),
    TENDENCY_FIND_SUCCESS("TENDENCY_SUCCESS_200", HttpStatus.OK, "성향정보 조회했습니다"),
    TENDENCY_NOT_FOUND("TENDENCY_NOT_FOUND_404", HttpStatus.NOT_FOUND, "성향이 없습니다"),

    // pw
    PASSWORD_RESET_LINK_SENT("PASSWORD_RESET_LINK_SENT_200", HttpStatus.OK, "비밀번호 재설정 링크가 전송됐습니다"),
    PASSWORD_RESET_SUCCESS("PASSWORD_RESET_SUCCESS_200", HttpStatus.OK, "비밀번호 재설정됐습니다"),
    PASSWORD_RESET_TOKEN_INVALID("PASSWORD_RESET_TOKEN_INVALID_400", HttpStatus.BAD_REQUEST, "재설정 토큰이 유효하지 않습니다."),

    // auth
    EMAIL_CODE_SENT("EMAIL_CODE_SENT_200", HttpStatus.OK, "인증 코드 발송에 성공했습니다."),
    EMAIL_CODE_EXPIRED("EMAIL_CODE_EXPIRED_400", HttpStatus.BAD_REQUEST, "인증 코드가 만료되었습니다."),
    EMAIL_CODE_MISMATCH("EMAIL_CODE_MISMATCH_400", HttpStatus.BAD_REQUEST, "인증 코드가 일치하지 않습니다."),
    EMAIL_ALREADY_VERIFIED("EMAIL_ALREADY_VERIFIED_409", HttpStatus.CONFLICT, "이미 인증된 이메일입니다."),
    EMAIL_NOT_VERIFIED("EMAIL_NOT_VERIFIED_401", HttpStatus.UNAUTHORIZED, "이메일 인증이 필요합니다"),
    EMAIL_SEND_FAILED("EAMIL_SEND_FAILED_500", HttpStatus.INTERNAL_SERVER_ERROR, "이메일 발송에 실패했습니다"),
    EMAIL_VERIFICATION_SUCCESS("EMAIL_VERIFICATION_SUCCESS_200", HttpStatus.OK, "이메일 인증에 성공했습니다."),

    // Plan
    PLAN_DETAIL_SUCCESS("FIND_PLAN_DETAIL_200", HttpStatus.OK, "요금제 상세 조회에 성공했습니다."),
    PLAN_BENEFIT_SUCCESS("FIND_PLAN_BENEFIT_200", HttpStatus.OK, "요금제 혜택 조회에 성공했습니다."),
    PLAN_PRODUCT_SUCCESS("FIND_PLAN_PRODUCT_200", HttpStatus.OK, "요금제 상품 조회에 성공했습니다."),
    PLAN_LIST_SUCCESS("READ_PLAN_LIST_200", HttpStatus.OK, "요금제 목록 조회에 성공했습니다."),
    PLAN_NOT_FOUND("NOT_FOUND_PLAN_404", HttpStatus.NOT_FOUND, "해당 요금제를 찾을 수 없습니다."),
    INVALID_PLAN_CATEGORY("INVALID_PLAN_CATEGORY_400", HttpStatus.BAD_REQUEST, "유효하지 않은 요금제 카테고리입니다."),
    INVALID_PLAN_SORT_TYPE("INVALID_PLAN_SORT_TYPE_400", HttpStatus.BAD_REQUEST, "유효하지 않은 정렬 타입입니다."),

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
    CHAT_BAD_WORD_DETECTED("BAD_WORD_CHAT_400", HttpStatus.BAD_REQUEST, "상담원에게 폭언이나 욕설을 하시면 안 됩니다."),
    KEYWORD_EXTRACTION_FAILED("KEYWORD_EXTRACTION_FAILED_400", HttpStatus.BAD_REQUEST, "죄송합니다. 요금제 관련 질문을 이해하지 못했습니다. 번거로우시겠지만, 요금제에 대해 다시 한 번 질문해 주시면 감사하겠습니다."),

    // Product
    PRODUCT_LIST_SUCCESS("PRODUCT_LIST_SUCCESS_200", HttpStatus.OK, "상품 목록 조회에 성공했습니다."),


    REDIS_UNAVAILABLE("REDIS_UNAVAILABLE_503", HttpStatus.SERVICE_UNAVAILABLE, "세션 저장소 레디스 이용 불가 ");
    private final String code;
    private final HttpStatus status;
    private final String message;
}
