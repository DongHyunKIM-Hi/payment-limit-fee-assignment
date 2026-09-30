package com.practice.paymentlimit.payment;

/** 결제 승인 성공(200) 응답. 계약이므로 필드를 바꾸지 않습니다. */
public record PaymentApprovedResponse(
        String paymentId,
        String userId,
        long amount,
        long fee,
        String status,
        long remainingLimit
) {
    public static PaymentApprovedResponse approved(String paymentId, String userId, long amount, long fee, long remainingLimit) {
        return new PaymentApprovedResponse(paymentId, userId, amount, fee, "APPROVED", remainingLimit);
    }
}
