package com.cse.cseprojectroommanagementserver.domain.reservation.domain.model;

import com.cse.cseprojectroommanagementserver.domain.member.domain.model.Member;
import com.cse.cseprojectroommanagementserver.domain.projecttable.domain.model.ProjectTable;
import com.cse.cseprojectroommanagementserver.domain.reservation.exception.CheckInPreviousReservationInUseException;
import com.cse.cseprojectroommanagementserver.domain.reservation.exception.ImpossibleCheckInTimeAfterStartTimeException;
import com.cse.cseprojectroommanagementserver.domain.reservation.exception.ImpossibleCheckInTimeBeforeStartTimeException;
import com.cse.cseprojectroommanagementserver.domain.tablereturn.domain.model.TableReturn;
import com.cse.cseprojectroommanagementserver.global.common.BaseTimeEntity;
import com.cse.cseprojectroommanagementserver.global.common.QRImage;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

import static com.cse.cseprojectroommanagementserver.domain.reservation.domain.model.Means.*;
import static com.cse.cseprojectroommanagementserver.domain.reservation.domain.model.ReservationStatus.*;
import static com.cse.cseprojectroommanagementserver.global.util.DateFormatProvider.*;
import static com.cse.cseprojectroommanagementserver.global.util.ReservationFixedPolicy.*;

@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Reservation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_table_id")
    private ProjectTable projectTable;

    @OneToOne(mappedBy = "targetReservation", fetch = FetchType.LAZY)
    private TableReturn tableReturn;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "reservation_qr_id")
    private ReservationQR reservationQR;

    @Enumerated(value = EnumType.STRING)
    private ReservationStatus reservationStatus;

    @DateTimeFormat(pattern = LOCAL_DATE_TIME_FORMAT)
    private LocalDateTime startDateTime;

    @DateTimeFormat(pattern = LOCAL_DATE_TIME_FORMAT)
    private LocalDateTime endDateTime;

    @DateTimeFormat(pattern = LOCAL_DATE_TIME_FORMAT)
    private LocalDateTime checkInTime;

    @Enumerated(EnumType.STRING)
    private Means means;

    public void changeReservationStatus(ReservationStatus reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    public void changeReservationQR(ReservationQR reservationQR) {
        if (this.reservationQR != null) {
            this.reservationQR.changeReservation(null);
        }
        this.reservationQR = reservationQR;
        this.reservationQR.changeReservation(this);
    }

    public static Reservation createReservation(Member member, ProjectTable projectTable, QRImage qrImage, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        Reservation reservation = Reservation.builder()
                .member(member)
                .projectTable(projectTable)
                .reservationStatus(RESERVATION_COMPLETED)
                .startDateTime(startDateTime)
                .endDateTime(endDateTime)
                .means(WEB)
                .build();

        reservation.changeReservationQR(ReservationQR.builder().qrImage(qrImage).build());

        return reservation;
    }

    public static Reservation createOnSiteReservation(Member member, ProjectTable projectTable, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return Reservation.builder()
                .member(member)
                .projectTable(projectTable)
                .reservationStatus(IN_USE)
                .startDateTime(startDateTime)
                .endDateTime(endDateTime)
                .checkInTime(LocalDateTime.now())
                .means(KIOSK)
                .build();
    }

    public void cancel() {
        this.reservationStatus = CANCELED;
    }

    public void changeTableReturnToReturned(TableReturn tableReturn) {
        this.tableReturn = tableReturn;
        this.reservationStatus = RETURNED;
    }

    public void changeTableReturnToNotReturned(TableReturn tableReturn) {
        this.tableReturn = tableReturn;
        this.reservationStatus = NOT_RETURNED;
    }

//    public void changeStatusToReturnWaiting() {
//        this.reservationStatus = RETURN_WAITING;
//    }

    public void checkIn(boolean isPreviousReservationInUse) {
        if (isPreviousReservationInUse) throw new CheckInPreviousReservationInUseException();
        else if (LocalDateTime.now().isBefore(this.startDateTime.minusMinutes(POSSIBLE_CHECKIN_TIME_BEFORE.getValue()))) throw new ImpossibleCheckInTimeBeforeStartTimeException();//???????????? 10????????? ????????? ????????? ??????
        else if (LocalDateTime.now().isAfter(this.startDateTime.plusMinutes(POSSIBLE_CHECKIN_TIME_AFTER.getValue()))) throw new ImpossibleCheckInTimeAfterStartTimeException();//???????????? 20?????? ?????? ????????? ????????? ??????

        this.checkInTime = LocalDateTime.now();
        this.reservationStatus = IN_USE;
    }

}
