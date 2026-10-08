package com.practice.paymentlimit.common.entity;

import com.practice.paymentlimit.common.enums.PaymentState;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 이 클래스는 수정하지 않습니다.
 * 저장소에 남는 결제 기록 하나. {@code LimitStore}의 조회·저장 메서드에서 사용합니다.
 */
@Getter
@AllArgsConstructor
public class PaymentRecord {

    private final String paymentId;
    private final String userId;
    private final long amount;
    private final long fee;
    private final PaymentState state;
    private final long approvedAtEpochMilli;

    /** 같은 내용에서 상태만 바꾼 새 기록을 만듭니다. (취소 처리에 사용) */
    public PaymentRecord withState(PaymentState newState) {
        return new PaymentRecord(paymentId, userId, amount, fee, newState, approvedAtEpochMilli);
    }
}
