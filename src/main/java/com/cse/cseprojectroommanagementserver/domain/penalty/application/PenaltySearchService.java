package com.cse.cseprojectroommanagementserver.domain.penalty.application;

import com.cse.cseprojectroommanagementserver.domain.penalty.domain.model.Penalty;
import com.cse.cseprojectroommanagementserver.domain.penalty.domain.repository.PenaltySearchableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.cse.cseprojectroommanagementserver.domain.penalty.dto.PenaltyResponse.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PenaltySearchService {

    private final PenaltySearchableRepository penaltySearchableRepository;

    public List<PenaltyLogResponse> searchPenaltyLogList(Long memberId) {
        List<Penalty> penaltyList = penaltySearchableRepository.findAllByMemberId(memberId);

        List<PenaltyLogResponse> penaltyLogResponseList = new ArrayList<>();
        for (Penalty penalty : penaltyList) {
            penaltyLogResponseList.add(new PenaltyLogResponse().of(penalty));
        }

        return penaltyLogResponseList;
    }

}
