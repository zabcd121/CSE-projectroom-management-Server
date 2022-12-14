package com.cse.cseprojectroommanagementserver.domain.reservation.repository;

import com.cse.cseprojectroommanagementserver.domain.projecttable.domain.model.QProjectTable;
import com.cse.cseprojectroommanagementserver.domain.reservation.domain.model.QReservationQR;
import com.cse.cseprojectroommanagementserver.domain.reservation.domain.model.Reservation;
import com.cse.cseprojectroommanagementserver.domain.reservation.domain.repository.ReservationSearchableRepository;
import com.cse.cseprojectroommanagementserver.global.util.ReservationFixedPolicy;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.cse.cseprojectroommanagementserver.domain.projectroom.domain.model.QProjectRoom.*;
import static com.cse.cseprojectroommanagementserver.domain.projecttable.domain.model.QProjectTable.*;
import static com.cse.cseprojectroommanagementserver.domain.reservation.domain.model.QReservation.*;
import static com.cse.cseprojectroommanagementserver.domain.reservation.domain.model.QReservationQR.*;
import static com.cse.cseprojectroommanagementserver.domain.reservation.domain.model.ReservationStatus.*;
import static com.cse.cseprojectroommanagementserver.domain.reservation.dto.ReservationResponseDto.*;
import static com.cse.cseprojectroommanagementserver.domain.tablereturn.domain.model.QTableReturn.*;
import static com.cse.cseprojectroommanagementserver.global.util.ReservationFixedPolicy.*;

@Repository
@RequiredArgsConstructor
public class ReservationSearchRepository implements ReservationSearchableRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<SearchReservationResponse> findAllByProjectRoomIdAndBetweenFirstDateTimeAndLastDateTime(Long projectRoomId, LocalDateTime firstDateTime, LocalDateTime lastDateTime) {
        return queryFactory
                .select(Projections.fields(SearchReservationResponse.class,
                        reservation.projectTable.tableId.as("projectTableId"), projectTable.tableName,
                        reservation.startDateTime, reservation.endDateTime, tableReturn.returnedDateTime))
                .from(reservation)
                .leftJoin(reservation.tableReturn, tableReturn)
                .join(reservation.projectTable, projectTable)
                .where(reservation.projectTable.projectRoom.projectRoomId.eq(projectRoomId)
                                .and(reservation.startDateTime.between(firstDateTime, lastDateTime)
                                .and(reservation.reservationStatus.notIn(CANCELED))))
                .fetch();
    }

    @Override
    public List<CurrentReservationByMemberResponse> findCurrentAllByMemberId(Long memberId) {
        return queryFactory
                .select(Projections.fields(CurrentReservationByMemberResponse.class,
                        reservation.reservationId, reservation.startDateTime, reservation.endDateTime, tableReturn.returnedDateTime,
                        reservation.reservationStatus, projectRoom.roomName, projectTable.tableName,
                        reservationQR.qrImage.fileLocalName.as("imageName"), reservationQR.qrImage.fileUrl.as("imageURL")))
                .from(reservation)
                .leftJoin(reservation.tableReturn, tableReturn)
                .join(reservation.projectTable, projectTable)
                .join(reservation.projectTable.projectRoom, projectRoom)
                .leftJoin(reservation.reservationQR, reservationQR)
                .where(reservation.member.memberId.eq(memberId)
                        .and(reservation.reservationStatus.in(RESERVATION_COMPLETED, IN_USE, RETURN_WAITING)))
                .orderBy(reservation.startDateTime.asc())
                .fetch();
    }

    @Override
    public List<PastReservationByMemberResponse> findPastAllByMemberId(Long memberId) {
        return queryFactory
                .select(Projections.fields(PastReservationByMemberResponse.class,
                        reservation.reservationId, reservation.startDateTime, reservation.endDateTime, tableReturn.returnedDateTime,
                        reservation.reservationStatus, projectRoom.roomName, projectTable.tableName))
                .from(reservation)
                .leftJoin(reservation.tableReturn, tableReturn)
                .join(reservation.projectTable, projectTable)
                .join(projectTable.projectRoom, projectRoom)
                .where(reservation.member.memberId.eq(memberId)
                        .and(reservation.reservationStatus.in(UN_USED, RETURN_WAITING, NOT_RETURNED, RETURNED, CANCELED)))
                .orderBy(reservation.startDateTime.desc())
                .fetch();
    }

    @Override
    public Optional<Reservation> findByReservationId(Long reservationId) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(reservation)
                        .where(reservation.reservationId.eq(reservationId))
                        .fetchOne()
        );
    }

    @Override
    public Optional<Reservation> findByQRContents(String contents) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(reservation)
                        .join(reservation.reservationQR, reservationQR).fetchJoin()
                        .join(reservation.projectTable, projectTable).fetchJoin()
                        .where(reservation.reservationStatus.eq(RESERVATION_COMPLETED)
                                .and(reservationQR.qrImage.content.eq(contents)))
                        .fetchOne()
        );
    }

    @Override
    public Optional<List<Reservation>> findReturnWaitingReservations() {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(reservation)
                        .where(reservation.reservationStatus.eq(RETURN_WAITING))
                        .fetch()
        );
    }

    @Override
    public Optional<List<Reservation>> findFinishedButInUseStatusReservations() {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(reservation)
                        .where(reservation.reservationStatus.eq(IN_USE))
                        .where(reservation.endDateTime.before(LocalDateTime.now()))
                        .fetch()
        );
    }

    @Override
    public boolean existsCurrentInUseReservationByTableName(String tableName) {
        return queryFactory
                .from(reservation)
                .where(reservation.projectTable.tableName.eq(tableName)
                        .and(reservation.reservationStatus.eq(IN_USE)))
                .select(reservation.reservationId)
                .fetchFirst() != null;
    }

    @Override
    public Long countPastReservations(Long memberId) {
        return queryFactory
                .select(reservation.count())
                .from(reservation)
                .where(reservation.reservationStatus.in(CANCELED, UN_USED, NOT_RETURNED, RETURNED)
                        .and(reservation.member.memberId.eq(memberId)))
                .fetchOne();
    }

    @Override
    public List<Reservation> findUnUsedReservations() {
        LocalDateTime current = LocalDateTime.now();
        return queryFactory
                .selectFrom(reservation)
                .where(reservation.reservationStatus.eq(RESERVATION_COMPLETED)
                        .and(reservation.startDateTime.eq(LocalDateTime.of(current.getYear(), current.getMonthValue(), current.getDayOfMonth(), current.getHour(), current.getMinute()- POSSIBLE_CHECKIN_TIME_AFTER.getValue()))))
                .fetch();
    }

}
