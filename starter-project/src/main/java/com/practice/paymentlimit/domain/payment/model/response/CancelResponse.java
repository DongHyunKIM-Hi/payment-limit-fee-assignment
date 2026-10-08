package com.practice.paymentlimit.domain.payment.model.response;

import lombok.Getter;

/** 결제 취소 성공(200) 응답. 계약이므로 필드를 바꾸지 않습니다. */
@Getter
public class CancelResponse {

    private final String paymentId;
    private final String status;
    private final long restoredAmount;
    private final long remainingLimit;

    public CancelResponse(String paymentId, String status, long restoredAmount, long remainingLimit) {
        this.paymentId = paymentId;
        this.status = status;
        this.restoredAmount = restoredAmount;
        this.remainingLimit = remainingLimit;
    }

    public static CancelResponse canceled(String paymentId, long restoredAmount, long remainingLimit) {
        return new CancelResponse(paymentId, "CANCELED", restoredAmount, remainingLimit);
    }
}
