package com.cse.cseprojectroommanagementserver.domain.member.domain.model;

import com.cse.cseprojectroommanagementserver.global.common.BaseTimeEntity;
import com.cse.cseprojectroommanagementserver.global.common.QRImage;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountQR extends BaseTimeEntity {

    @Id @GeneratedValue
    private Long accountQRId;

    @Embedded
    private QRImage qrImage;

    @Setter
    @OneToOne(mappedBy = "accountQR", fetch = FetchType.LAZY)
    private Member member;

}
