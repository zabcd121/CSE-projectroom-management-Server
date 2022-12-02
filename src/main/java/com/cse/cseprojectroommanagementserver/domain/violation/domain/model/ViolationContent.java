package com.cse.cseprojectroommanagementserver.domain.violation.domain.model;


public enum ViolationContent {
    UN_USED("예약 후 미사용"),
    NOT_RETURNED("사용후 미반납");

    private final String content;

    private ViolationContent(String content) {
        this.content = content;
    }

    public String getContent() { return content; }

}
