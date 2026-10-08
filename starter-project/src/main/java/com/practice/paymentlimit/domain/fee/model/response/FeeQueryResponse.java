package com.practice.paymentlimit.domain.fee.model.response;

import com.practice.paymentlimit.common.enums.Grade;
import com.practice.paymentlimit.common.enums.PayType;
import lombok.AllArgsConstructor;
import lombok.Getter;

/** 수수료 계산(200) 응답. 계약이므로 필드를 바꾸지 않습니다. */
@Getter
@AllArgsConstructor
public class FeeQueryResponse {

    private Grade grade;
    private PayType payType;
    private long amount;
    private long fee;
}
