package com.cse.cseprojectroommanagementserver.domain.penalty.domain.repository;

import com.cse.cseprojectroommanagementserver.domain.penalty.domain.model.Penalty;

import java.util.List;
import java.util.Optional;

public interface PenaltySearchableRepository {

    boolean existsByMemberId(Long memberId);
    Optional<Penalty> findInProgressByMemberId(Long memberId);

    List<Penalty> findAllByMemberId(Long memberId);
}
