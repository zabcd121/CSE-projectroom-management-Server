package com.cse.cseprojectroommanagementserver.domain.reservation.exception;

public class ImpossibleCheckInTimeBeforeStartTimeException extends RuntimeException{
    public ImpossibleCheckInTimeBeforeStartTimeException(String message) {
        super(message);
    }
}
