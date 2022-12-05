package com.cse.cseprojectroommanagementserver.domain.tablereturn.application;

import com.cse.cseprojectroommanagementserver.domain.reservation.domain.model.Reservation;
import com.cse.cseprojectroommanagementserver.domain.reservation.domain.repository.ReservationSearchableRepository;
import com.cse.cseprojectroommanagementserver.domain.reservation.exception.NotExistsReservationException;
import com.cse.cseprojectroommanagementserver.domain.tablereturn.domain.model.TableReturn;
import com.cse.cseprojectroommanagementserver.domain.tablereturn.domain.repository.TableReturnRepository;
import com.cse.cseprojectroommanagementserver.domain.tablereturn.dto.TableReturnRequestDto;
import com.cse.cseprojectroommanagementserver.global.common.Image;
import com.cse.cseprojectroommanagementserver.global.util.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.UUID;

import static com.cse.cseprojectroommanagementserver.domain.tablereturn.dto.TableReturnRequestDto.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TableReturnService {

    private final TableReturnRepository tableReturnRepository;
    private final ReservationSearchableRepository reservationSearchableRepository;
    private final FileUploadUtil fileUploadUtil;

    @Transactional
    public void returnTable(Long reservationId, MultipartFile cleanupPhoto) {
        Reservation findReservation = reservationSearchableRepository.findByReservationId(reservationId)
                .orElseThrow(() -> new NotExistsReservationException());
        Image image = fileUploadUtil.uploadFile(cleanupPhoto);

        tableReturnRepository.save(TableReturn.createReturn(findReservation, image));
    }
}
