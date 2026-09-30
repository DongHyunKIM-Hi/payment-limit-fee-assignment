package com.practice.paymentlimit.payment;

/** 한도 조회(200) 응답. 계약이므로 필드를 바꾸지 않습니다. */
public record LimitResponse(
        String userId,
        long dailyLimit,
        long usedAmount,
        long remainingLimit
) {
}
