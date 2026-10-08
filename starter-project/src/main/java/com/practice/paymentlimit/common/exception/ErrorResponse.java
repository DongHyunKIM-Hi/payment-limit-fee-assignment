package com.practice.paymentlimit.common.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.practice.paymentlimit.common.enums.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/** 에러 응답 공통 형식. 계약이므로 필드를 바꾸지 않습니다. {@code paymentId}는 알 수 없을 때 생략됩니다. */
@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private String code;
    private String message;
    private String paymentId;

    public static ErrorResponse of(ErrorCode code, String message, String paymentId) {
        return new ErrorResponse(code.name(), message, paymentId);
    }

    public static ErrorResponse of(ErrorCode code, String message) {
        return new ErrorResponse(code.name(), message, null);
    }
}
