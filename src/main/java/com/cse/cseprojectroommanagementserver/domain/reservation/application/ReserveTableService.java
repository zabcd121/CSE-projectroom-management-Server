package com.cse.cseprojectroommanagementserver.domain.reservation.application;

import com.cse.cseprojectroommanagementserver.domain.member.domain.model.Member;
import com.cse.cseprojectroommanagementserver.domain.member.domain.repository.MemberRepository;
import com.cse.cseprojectroommanagementserver.domain.member.domain.repository.MemberSearchableRepository;
import com.cse.cseprojectroommanagementserver.domain.penalty.domain.repository.PenaltySearchableRepository;
import com.cse.cseprojectroommanagementserver.domain.projecttable.domain.repository.ProjectTableRepository;
import com.cse.cseprojectroommanagementserver.domain.reservation.domain.model.Reservation;
import com.cse.cseprojectroommanagementserver.domain.reservation.domain.repository.ReservationRepository;
import com.cse.cseprojectroommanagementserver.domain.reservation.domain.repository.ReservationVerifiableRepository;
import com.cse.cseprojectroommanagementserver.domain.reservation.exception.DuplicatedReservationException;
import com.cse.cseprojectroommanagementserver.domain.reservation.exception.PenaltyMemberReserveFailException;
import com.cse.cseprojectroommanagementserver.domain.reservation.exception.ReservationQRNotCreatedException;
import com.cse.cseprojectroommanagementserver.domain.reservationpolicy.domain.model.ReservationPolicy;
import com.cse.cseprojectroommanagementserver.domain.reservationpolicy.domain.repository.ReservationPolicyRepository;
import com.cse.cseprojectroommanagementserver.global.common.AppliedStatus;
import com.cse.cseprojectroommanagementserver.global.common.QRImage;
import com.cse.cseprojectroommanagementserver.global.util.QRGenerator;
import com.cse.cseprojectroommanagementserver.global.util.QRNotCreatedException;
import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;

import static com.cse.cseprojectroommanagementserver.domain.reservation.dto.ReservationRequestDto.*;
import static com.cse.cseprojectroommanagementserver.domain.reservation.dto.ReservationRequestDto.ReserveRequest;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReserveTableService {

    private final ReservationVerifiableRepository reservationVerifiableRepository;
    private final ReservationRepository reservationRepository;
    private final PenaltySearchableRepository penaltySearchRepository;
    private final ReservationPolicyRepository reservationPolicyRepository;
    private final MemberRepository memberRepository;
    private final ProjectTableRepository projectTableRepository;
    private final MemberSearchableRepository memberSearchableRepository;
    private final QRGenerator qrGenerator;

    private final PasswordEncoder passwordEncoder;


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void reserve(ReserveRequest reserveRequest) {
        validateReservation(reserveRequest.getMemberId(), reserveRequest.getProjectTableId(), reserveRequest.getStartDateTime(), reserveRequest.getEndDateTime());

        QRImage reservationQrImage = null;

        try {
            reservationQrImage = qrGenerator.createReservationQRCodeImage();
        } catch (WriterException | IOException | QRNotCreatedException e) {
            throw new ReservationQRNotCreatedException();
        }

        reservationRepository.save(
                Reservation.createReservation(
                        memberRepository.getReferenceById(reserveRequest.getMemberId()), //?????? ????????? ?????? proxy ????????? ??????
                        projectTableRepository.getReferenceById(reserveRequest.getProjectTableId()),
                        reservationQrImage,
                        reserveRequest.getStartDateTime(),
                        reserveRequest.getEndDateTime()
                ));

    }

    /**
     * ????????????: QR ???????????? ????????? ???????????? ????????? ?????????
     * @param
     */
    @Transactional
    public void reserveOnsiteByAccountQR(Member member, OnsiteReservationRequestByQR reservationRequest) {
        reserveOnsite(member, reservationRequest.getProjectTableId(), reservationRequest.getStartDateTime(), reservationRequest.getEndDateTime());
    }

    /**
     * ????????????: FORM ???????????? ????????? ???????????? ????????? ?????????
     * @param
     */
    @Transactional
    public void reserveOnsiteByFormLogin(Member member, OnsiteReservationRequestByLoginForm reservationRequest) {
        reserveOnsite(member, reservationRequest.getProjectTableId(), reservationRequest.getStartDateTime(), reservationRequest.getEndDateTime() );
    }


    private void reserveOnsite(Member member, Long projectTableId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        validateReservation(member.getMemberId(), projectTableId, startDateTime, endDateTime);

        reservationRepository.save(
                Reservation.createOnSiteReservation(
                        memberRepository.getReferenceById(member.getMemberId()),
                        projectTableRepository.getReferenceById(projectTableId),
                        startDateTime,
                        endDateTime
                ));
    }

    private void validateReservation(Long memberId, Long projectTableId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        isPenaltyMember(memberId);
        isDuplicatedReservation(projectTableId, startDateTime, endDateTime);

        ReservationPolicy reservationPolicy = findReservationPolicy();
        //?????? ??? ????????? ????????? ????????? ????????? ?????????
        Long countTodayMemberCreatedReservation = getCountTodayMemberCreatedReservation(memberId);
        reservationPolicy.verifyReservation(startDateTime, endDateTime, countTodayMemberCreatedReservation);
    }


    private boolean isPenaltyMember(Long memberId) {
        if (penaltySearchRepository.existsByMemberId(memberId)) {
            throw new PenaltyMemberReserveFailException();
        }
        return false;
    }


    private boolean isDuplicatedReservation(Long tableId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (reservationVerifiableRepository.existsBy(tableId, startDateTime, endDateTime)) {
            throw new DuplicatedReservationException();
        }
        log.info("?????? ?????? ??????");
        return false;
    }

    private ReservationPolicy findReservationPolicy() {
        return reservationPolicyRepository.findByAppliedStatus(AppliedStatus.CURRENT);
    }

    private Long getCountTodayMemberCreatedReservation(Long memberId) {
        return reservationVerifiableRepository.countCreatedReservationForTodayByMemberId(memberId);
    }


}