package com.cse.cseprojectroommanagementserver.domain.penalty.repository;

import com.cse.cseprojectroommanagementserver.domain.penalty.domain.repository.PenaltySearchableRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import static com.cse.cseprojectroommanagementserver.domain.penalty.domain.model.QPenalty.*;

@Repository
@RequiredArgsConstructor
public class PenaltySearchRepository implements PenaltySearchableRepository {

    private final JPAQueryFactory queryFactory;
    @Override
    public boolean existsByMemberId(Long memberId) {
        Integer count = queryFactory
                .selectOne()
                .from(penalty)
                .where(penalty.member.memberId.eq(memberId)
                        .and(penalty.startDate.before(LocalDate.now())).and(penalty.endDate.after(LocalDate.now())))
                .fetchFirst();

        return count != null ? true : false;

    }

}
