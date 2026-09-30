package com.practice.paymentlimit.limit;

/**
 * 이 클래스는 수정하지 않습니다.
 * 저장소에 남는 결제 기록 하나. {@link LimitStore#findPayment(String)}, {@link LimitStore#savePayment(PaymentRecord)}에서 사용합니다.
 */
public record PaymentRecord(
        String paymentId,
        String userId,
        long amount,
        long fee,
        PaymentState state,
        long approvedAtEpochMilli
) {
    /** 같은 내용에서 상태만 바꾼 새 기록을 만듭니다. (취소 처리에 사용) */
    public PaymentRecord withState(PaymentState newState) {
        return new PaymentRecord(paymentId, userId, amount, fee, newState, approvedAtEpochMilli);
    }
}
