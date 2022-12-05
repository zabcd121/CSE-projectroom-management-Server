package com.cse.cseprojectroommanagementserver.domain.reservation.application;

import com.cse.cseprojectroommanagementserver.domain.reservation.domain.model.Reservation;
import com.cse.cseprojectroommanagementserver.domain.reservation.domain.repository.ReservationVerifiableRepository;
import com.cse.cseprojectroommanagementserver.domain.reservation.exception.InvalidReservationQRException;
import com.cse.cseprojectroommanagementserver.domain.reservation.repository.ReservationSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.cse.cseprojectroommanagementserver.domain.reservation.dto.ReservationRequestDto.*;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReservationAuthService {

    private final ReservationSearchRepository reservationSearchableRepository;
    private final ReservationVerifiableRepository reservationVerifiableRepository;

    @Transactional
    public void authReservationQR(QRAuthRequest qrAuthRequest) {
        Reservation findReservation = reservationSearchableRepository.findByQRContents(qrAuthRequest.getQrContent())
                .orElseThrow(() -> new InvalidReservationQRException());

        findReservation.checkIn(reservationVerifiableRepository.existsInUsePreviousReservation(findReservation.getProjectTable().getTableId(), findReservation.getStartDateTime()));
    }

}
