package com.cse.cseprojectroommanagementserver.global.common;

import lombok.Getter;

@Getter
public enum ResponseConditionCode {
    /**
     * Signup: 01
     */
    SIGNUP_SUCCESS("0100", "회원가입 성공"),
    SIGNUP_FAIL("0101", "회원가입 실패"),
    LOGIN_ID_DUPLICATED("0102", "이미 사용중인 아이디(학번) 입니다.)"),
    LOGIN_ID_NOT_DUPLICATED("0103", "사용 가능한 아이디 입니다."),
    EMAIL_DUPLICATED("0104", "이미 사용중인 이메일 입니다."),
    EMAIL_USABLE("0105", "사용 가능한 이메일 입니다."),
    LOGIN_ID_WRONG_TYPE("0106", "잘못된 형식의 아이디(학번)이 입력되었습니다."),
    PASSWORD_WRONG_TYPE("0107", "잘못된 형식의 비밃번호가 입력되었습니다. 최소 한개 이상의 대소문자와 숫자, 특수문자를 포함한 8자 이상 16자 이하의 비밀번호를 입력해야 합니다."),
    EMAIL_WRONG_TYPE("0108", "잘못된 형식의 이메일이 입력되었습니다."),
    EMAIL_AUTH_FAIL("0109", "이메일 인증 실패"),
    EMAIL_NULL("0110", "이메일이 입력되지 않았습니다."),
    NAME_WRONG_TYPE("0111", "이름 형식이 잘못 입력되었습니다."),
    NAME_NULL("0112", "이름이 입력되지 않았습니다."),
    ACCOUNT_QR_CREATE_FAIL("0113", "QR 코드 생성에 실패했습니다."),



    /**
     * Auth: 02
     */
    LOGIN_SUCCESS("0200", "로그인 성공"),
    LOGIN_ID_NULL("0201", "아이디가 입력되지 않았습니다."),
    PASSWORD_NULL("0202", "패스워드가 입력되지 않았습니다."),
    LOGIN_ID_NOT_EXIST("0203", "존재하지 않는 아이디입니다."),
    PASSWORD_INVALID("0204", "잘못된 비밀번호를 입력하였습니다."),
    LOGOUT_SUCCESS("0205", "로그아웃 성공"),

    TOKEN_NOT_PASSED("0206", "인증 토큰이 전달되지 않았습니다."),
    TOKEN_NOT_BEARER("0207", "Bearer 토큰이 전달되지 않았습니다."),
    TOKEN_WRONG_TYPE("0208", "잘못된 타입의 인증 토큰이 전달되었습니다."),
    TOKEN_WRONG_SIGNATURE("0209", "잘못된 서명이 전달되었습니다."),
    TOKEN_EXPIRED("0210", "만료된 토큰이 전달되었습니다."),
    TOKEN_UNSUPPORTED("0211", "지원되지 않는 토큰이 전달되었습니다."),
    ACCESS_DENIED("0212", "접근 거부"),
    REFRESH_TOKEN_NOT_EXIST_IN_STORE("0213", "refresh 토큰이 만료되었습니다."),
    TOKEN_REISSUED("0214", "토큰이 재발급 되었습니다."),
    MEMBER_INFO_REISSUED("0215", "회원 정보 재발급"),
    MEMBER_MY_PAGE_INFO_SEARCH_SUCCESS("0215", "마이페이지 정보 조회 성공했습니다."),

    /**
     * Reservation: 03
     */
    RESERVATION_SUCCESS("0300", "예약 성공"),
    RESERVATION_FAIL_PENALTY_USER("0301", "계정 정지 상태이므로 예약이 불가능합니다."),
    RESERVATION_FAIL_RESERVATION_DUPLICATED("0302", "중복된 예약이 존재합니다."),
    RESERVATION_FAIL_EXCEED_MAX_TIME("0303", "예약가능 최대시간을 초과하였습니다."),
    RESERVATION_FAIL_EXCEED_MAX_COUNT("0304", "하루에 최대 예약 가능한 횟수를 초과하였습니다."),
    RESERVATION_FAIL_EXCEED_MAX_PERIOD("0305", "현재 예약 불가능한 날짜입니다."),
    RESERVATION_START_TIME_EMPTY("0306", "예약 시작시간을 입력해주세요."),
    RESERVATION_END_TIME_EMPTY("0307", "예약 종료시간을 입력해주세요."),
    RESERVATION_SEARCH_SUCCESS("0308", "예약 조회 성공"),
    RESERVATION_SEARCH_FAIL("0309", "예약 조회 실패"),
    RESERVATION_CANCEL_SUCCESS("0310", "예약 취소 성공"),
    RESERVATION_CANCEL_FAIL("0311", "예약 취소 실패"),
    RESERVATION_QR_CREATE_FAIL("0312", "예약 QR 생성 실패"),
    RESERVATION_QR_AUTH_SUCCESS("0313", "예약 QR 인증 성공"),
    IN_USE_TABLE("0314", "사용중인 테이블입니다."),
    NOT_IN_USE_TABLE("0315", "예약 확인 후 사용해주세요"),
    RESERVATION_QR_CHECK_IN_FAIL("0316", "유효하지 않은 QR 코드 입니다"),
    RESERVATION_CHECK_IN_FAIL_BEFORE_START_TIME("0317", "체크인 불가능한 시간입니다."),
    RESERVATION_CHECK_IN_FAIL_AFTER_START_TIME("0318", "체크인 불가능한 시간입니다."),
    RESERVATION_CHECK_IN_FAIL_PREVIOUS_IN_USE("0319", "이전 예약 사용중에는 체크인이 불가능합니다."),

    /**
     * Table Return: 04
     */
    RETURN_SUCCESS("0400", "반납 성공"),
    RETURN_FAIL("0400", "반납 실패"),

    /**
     * Project Room: 05
     */
    PROJECT_ROOM_SEARCH_SUCCESS("0500", "프로젝트실 조회 성공"),

    /**
     * Penalty: 06
     */
    PENALTY_LOGS_SEARCH_SUCCESS("0600", "제재 내역 조회 성공");

    private final String code;
    private final String message;

    ResponseConditionCode(final String code, final String message) {
        this.code = code;
        this.message = message;
    }
}
