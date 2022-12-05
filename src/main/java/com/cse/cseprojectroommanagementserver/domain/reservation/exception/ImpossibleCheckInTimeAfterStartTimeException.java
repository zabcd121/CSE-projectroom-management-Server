package com.cse.cseprojectroommanagementserver.domain.reservation.exception;

public class ImpossibleCheckInTimeAfterStartTimeException extends RuntimeException{
    public ImpossibleCheckInTimeAfterStartTimeException(String message) {
        super(message);
    }
}
