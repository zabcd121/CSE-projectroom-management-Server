package com.cse.cseprojectroommanagementserver.domain.reservation.exception.advice;

import com.cse.cseprojectroommanagementserver.domain.member.exception.InvalidAccountQRException;
import com.cse.cseprojectroommanagementserver.domain.member.exception.InvalidPasswordException;
import com.cse.cseprojectroommanagementserver.domain.member.exception.NotExistsMemberException;
import com.cse.cseprojectroommanagementserver.domain.reservation.api.ReservationApiController;
import com.cse.cseprojectroommanagementserver.domain.reservation.exception.*;
import com.cse.cseprojectroommanagementserver.domain.reservationpolicy.exception.ExceedMaxPeriodEnableReservationException;
import com.cse.cseprojectroommanagementserver.domain.reservationpolicy.exception.ExceedMaxTimeEnableReservationException;
import com.cse.cseprojectroommanagementserver.domain.reservationpolicy.exception.ExceedTodaysMaxCountEnableReservationException;
import com.cse.cseprojectroommanagementserver.global.common.dto.ResponseError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.cse.cseprojectroommanagementserver.global.common.ResponseConditionCode.*;

@RestControllerAdvice(assignableTypes = {ReservationApiController.class})
@Slf4j
public class ReservationPolicyControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ResponseError emptyArgument(MethodArgumentNotValidException ex) {
        ResponseError responseError = makeErrorResponse(ex.getBindingResult());
        return responseError;
    }

    private ResponseError makeErrorResponse(BindingResult bindingResult) {
        ResponseError responseError = null;

        if (bindingResult.hasErrors()) {
            String bindResultField = bindingResult.getFieldError().getField();
            log.error("bindResultField: {}", bindResultField);

            switch (bindResultField) {
                case "startDateTime":
                    responseError = new ResponseError(RESERVATION_START_TIME_EMPTY);
                    break;
                case "endDateTime":
                    responseError = new ResponseError(RESERVATION_END_TIME_EMPTY);
                    break;
            }
        }

        return responseError;
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler
    public ResponseError penaltyUserExHandler(PenaltyMemberReserveFailException ex) {
        log.error("[exceptionHandler] PenaltyMemberReserveFailException", ex);
        return new ResponseError(RESERVATION_FAIL_PENALTY_USER);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler
    public ResponseError duplicatedReservationExHandler(DuplicatedReservationException ex) {
        log.error("[exceptionHandler] DuplicatedReservationException", ex);
        return new ResponseError(RESERVATION_FAIL_RESERVATION_DUPLICATED);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler
    public ResponseError exceedTodaysMaxCountExHandler(ExceedTodaysMaxCountEnableReservationException ex) {
        log.error("[exceptionHandler] ExceedTodaysMaxCountEnableReservationException", ex);
        return new ResponseError(RESERVATION_FAIL_EXCEED_MAX_COUNT);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler
    public ResponseError exceedMaxTimeExHandler(ExceedMaxTimeEnableReservationException ex) {
        log.error("[exceptionHandler] ExceedMaxTimeEnableReservationException", ex);
        return new ResponseError(RESERVATION_FAIL_EXCEED_MAX_TIME);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler
    public ResponseError exceedTodaysMaxCountExHandler(ExceedMaxPeriodEnableReservationException ex) {
        log.error("[exceptionHandler] ExceedMaxPeriodEnableReservationException", ex);
        return new ResponseError(RESERVATION_FAIL_EXCEED_MAX_PERIOD);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ResponseError notCreatedAccountQRExHandler(ReservationQRNotCreatedException ex) {
        log.error("[exceptionHandler] ReservationQRNotCreatedException", ex);
        return new ResponseError(RESERVATION_QR_CREATE_FAIL);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ResponseError notExistsReservationExHandler(NotExistsReservationException ex) {
        log.error("[exceptionHandler] NotExistsReservationException", ex);
        return new ResponseError(RESERVATION_SEARCH_FAIL);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ResponseError notExistsReservationExHandler(InvalidAccountQRException ex) {
        log.error("[exceptionHandler] InvalidAccountQRException", ex);
        return new ResponseError(RESERVATION_SEARCH_FAIL);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ResponseError notExistsMemberExHandler(NotExistsMemberException ex) {
        log.error("[exceptionHandler] NotExistsMemberException", ex);
        return new ResponseError(LOGIN_ID_NOT_EXIST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ResponseError InvalidPasswordExHandler(InvalidPasswordException ex) {
        log.error("[exceptionHandler] InvalidPasswordExHandler", ex);
        return new ResponseError(PASSWORD_INVALID);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ResponseError IsNotInUSeTableExHandler(IsNotInUseTableException ex) {
        log.error("[exceptionHandler] IsNotInUseTableException", ex);
        return new ResponseError(NOT_IN_USE_TABLE);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ResponseError impossibleQRCheckInExHandler(InvalidReservationQRException ex) {
        log.error("[exceptionHandler] ImpossibleCheckInTimeBeforeStartTimeException", ex);
        return new ResponseError(RESERVATION_QR_CHECK_IN_FAIL);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ResponseError impossibleCheckInTimeBeforeStartTimeExHandler(ImpossibleCheckInTimeBeforeStartTimeException ex) {
        log.error("[exceptionHandler] ImpossibleCheckInTimeBeforeStartTimeException", ex);
        return new ResponseError(RESERVATION_CHECK_IN_FAIL_BEFORE_START_TIME, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ResponseError impossibleCheckInTimeAfterStartTimeExHandler(ImpossibleCheckInTimeAfterStartTimeException ex) {
        log.error("[exceptionHandler] ImpossibleCheckInTImeException", ex);
        return new ResponseError(RESERVATION_CHECK_IN_FAIL_AFTER_START_TIME, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ResponseError checkInPreviousReservationInUseExHandler(CheckInPreviousReservationInUseException ex) {
        log.error("[exceptionHandler] CheckInPreviousReservationInUseException", ex);
        return new ResponseError(RESERVATION_CHECK_IN_FAIL_PREVIOUS_IN_USE);
    }

}

