package com.practice.paymentlimit.domain.payment.model.request;

import com.practice.paymentlimit.common.enums.Grade;
import com.practice.paymentlimit.common.enums.PayType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

/** 결제 승인 요청. 계약이므로 필드를 추가·삭제·이름 변경하지 않습니다. */
@Getter
@NoArgsConstructor
public class PaymentRequest {

    @NotNull
    @Size(min = 1, max = 50)
    private String paymentId;

    @NotNull
    @Size(min = 1, max = 50)
    private String userId;

    @NotNull
    @Min(1)
    @Max(100_000_000)
    private Long amount;

    @NotNull
    private Grade grade;

    @NotNull
    private PayType payType;
}
