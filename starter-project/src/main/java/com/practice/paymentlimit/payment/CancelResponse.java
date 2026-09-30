package com.practice.paymentlimit.payment;

/** 결제 취소 성공(200) 응답. 계약이므로 필드를 바꾸지 않습니다. */
public record CancelResponse(
        String paymentId,
        String status,
        long restoredAmount,
        long remainingLimit
) {
    public static CancelResponse canceled(String paymentId, long restoredAmount, long remainingLimit) {
        return new CancelResponse(paymentId, "CANCELED", restoredAmount, remainingLimit);
    }
}
