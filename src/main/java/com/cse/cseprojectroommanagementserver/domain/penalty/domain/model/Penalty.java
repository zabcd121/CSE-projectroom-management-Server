package com.cse.cseprojectroommanagementserver.domain.penalty.domain.model;

import com.cse.cseprojectroommanagementserver.domain.member.domain.model.Member;
import com.cse.cseprojectroommanagementserver.domain.penaltypolicy.domain.model.PenaltyPolicy;
import com.cse.cseprojectroommanagementserver.domain.violation.domain.model.Violation;
import com.cse.cseprojectroommanagementserver.domain.violation.domain.model.ViolationContent;
import com.cse.cseprojectroommanagementserver.global.common.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

import static com.cse.cseprojectroommanagementserver.domain.violation.domain.model.ViolationContent.*;

@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Penalty extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long penaltyId;

    @OneToMany(mappedBy = "penalty", fetch = FetchType.LAZY)
    private List<Violation> violations;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String description;
    private LocalDate startDate;
    private LocalDate endDate;

    public void addViolation(Violation violation) {
        violation.changePenalty(this);
        this.violations.add(violation);
    }

    public static Penalty createPenalty(Member member, PenaltyPolicy penaltyPolicy, List<Violation> violationList) {
        int unusedCnt = 0;
        int notReturnedCnt = 0;
        for (Violation violation : violationList) {
            if(violation.getViolationContent().equals(UN_USED) ) unusedCnt++;
            else if(violation.getViolationContent().equals(NOT_RETURNED) ) notReturnedCnt++;
        }

        String description = UN_USED.getContent() + " " + unusedCnt + "회 " + NOT_RETURNED + " " + notReturnedCnt + "회";

        Penalty penalty = Penalty.builder()
                .member(member)
                .description(description)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(penaltyPolicy.getNumberOfSuspensionDay()))
                .build();

        for (Violation violation : violationList) {
            penalty.addViolation(violation);
        }

        return penalty;
    }
}
