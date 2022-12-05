package com.cse.cseprojectroommanagementserver.domain.penalty.api;

import com.cse.cseprojectroommanagementserver.domain.penalty.application.PenaltySearchService;
import com.cse.cseprojectroommanagementserver.global.common.ResponseConditionCode;
import com.cse.cseprojectroommanagementserver.global.common.dto.ResponseSuccess;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.cse.cseprojectroommanagementserver.domain.penalty.dto.PenaltyResponse.*;

@RestController
@RequestMapping("/api/penalties")
@RequiredArgsConstructor
public class PenaltyApiController {

    private final PenaltySearchService penaltySearchService;

    @GetMapping
    public ResponseSuccess<PenaltyLogResponse> getPenaltyLogList(@RequestParam Long memberId) {
        List<PenaltyLogResponse> penaltyLogResponseList = penaltySearchService.searchPenaltyLogList(memberId);
        return new ResponseSuccess(ResponseConditionCode.PENALTY_LOGS_SEARCH_SUCCESS, penaltyLogResponseList);
    }
}
