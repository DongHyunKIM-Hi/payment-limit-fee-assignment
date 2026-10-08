package com.practice.paymentlimit.domain.payment.model.response;

import lombok.Getter;

/** 결제 승인 성공(200) 응답. 계약이므로 필드를 바꾸지 않습니다. */
@Getter
public class PaymentApprovedResponse {

    private final String paymentId;
    private final String userId;
    private final long amount;
    private final long fee;
    private final String status;
    private final long remainingLimit;

    public PaymentApprovedResponse(String paymentId, String userId, long amount, long fee, String status, long remainingLimit) {
        this.paymentId = paymentId;
        this.userId = userId;
        this.amount = amount;
        this.fee = fee;
        this.status = status;
        this.remainingLimit = remainingLimit;
    }

    public static PaymentApprovedResponse approved(String paymentId, String userId, long amount, long fee, long remainingLimit) {
        return new PaymentApprovedResponse(paymentId, userId, amount, fee, "APPROVED", remainingLimit);
    }
}
