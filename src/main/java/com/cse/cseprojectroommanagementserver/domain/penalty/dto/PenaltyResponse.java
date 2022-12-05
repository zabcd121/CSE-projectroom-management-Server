package com.cse.cseprojectroommanagementserver.domain.penalty.dto;

import com.cse.cseprojectroommanagementserver.domain.penalty.domain.model.Penalty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

import static com.cse.cseprojectroommanagementserver.global.util.DateFormatProvider.LOCAL_DATE_FORMAT;
import static com.cse.cseprojectroommanagementserver.global.util.DateFormatProvider.LOCAL_DATE_TIME_FORMAT;

public class PenaltyResponse {

    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor
    @Getter
    public static class PenaltyLogResponse {
        @DateTimeFormat(pattern = LOCAL_DATE_FORMAT)
        private LocalDate startDate;

        @DateTimeFormat(pattern = LOCAL_DATE_FORMAT)
        private LocalDate endDate;

        private String description;

        public PenaltyLogResponse of(Penalty penalty) {
            this.startDate = penalty.getStartDate();
            this.endDate = penalty.getEndDate();
            this.description = penalty.getDescription();

            return this;
        }
    }
}
