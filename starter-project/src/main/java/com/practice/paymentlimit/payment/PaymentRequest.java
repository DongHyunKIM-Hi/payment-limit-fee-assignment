package com.practice.paymentlimit.payment;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** 결제 승인 요청. 계약이므로 필드를 추가·삭제·이름 변경하지 않습니다. */
public record PaymentRequest(
        @NotNull @Size(min = 1, max = 50) String paymentId,
        @NotNull @Size(min = 1, max = 50) String userId,
        @NotNull @Min(1) @Max(100_000_000) Long amount,
        @NotNull Grade grade,
        @NotNull PayType payType
) {
}
