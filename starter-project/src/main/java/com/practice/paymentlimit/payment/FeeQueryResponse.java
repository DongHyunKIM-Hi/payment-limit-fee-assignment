package com.practice.paymentlimit.payment;

/** 수수료 계산(200) 응답. 계약이므로 필드를 바꾸지 않습니다. */
public record FeeQueryResponse(
        Grade grade,
        PayType payType,
        long amount,
        long fee
) {
}
