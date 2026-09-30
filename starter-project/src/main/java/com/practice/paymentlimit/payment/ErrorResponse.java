package com.practice.paymentlimit.payment;

import com.fasterxml.jackson.annotation.JsonInclude;

/** 에러 응답 공통 형식. 계약이므로 필드를 바꾸지 않습니다. {@code paymentId}는 알 수 없을 때 생략됩니다. */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        String code,
        String message,
        String paymentId
) {
    public static ErrorResponse of(ErrorCode code, String message, String paymentId) {
        return new ErrorResponse(code.name(), message, paymentId);
    }

    public static ErrorResponse of(ErrorCode code, String message) {
        return new ErrorResponse(code.name(), message, null);
    }
}
