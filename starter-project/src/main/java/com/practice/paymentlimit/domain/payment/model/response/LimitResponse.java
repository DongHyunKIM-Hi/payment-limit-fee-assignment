package com.practice.paymentlimit.domain.payment.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** 한도 조회(200) 응답. 계약이므로 필드를 바꾸지 않습니다. */
@Getter
@AllArgsConstructor
public class LimitResponse {

    private String userId;
    private long dailyLimit;
    private long usedAmount;
    private long remainingLimit;
}
