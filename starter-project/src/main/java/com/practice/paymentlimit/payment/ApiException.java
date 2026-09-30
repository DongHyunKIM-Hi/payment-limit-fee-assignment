package com.practice.paymentlimit.payment;

import org.springframework.http.HttpStatus;

/** 계약에 정의된 에러 상황(400/404/409/422)을 표현하는 예외. */
public class ApiException extends RuntimeException {

    private final HttpStatus status;
    private final ErrorCode code;
    private final String paymentId;

    public ApiException(HttpStatus status, ErrorCode code, String message, String paymentId) {
        super(message);
        this.status = status;
        this.code = code;
        this.paymentId = paymentId;
    }

    public HttpStatus status() {
        return status;
    }

    public ErrorCode code() {
        return code;
    }

    public String paymentId() {
        return paymentId;
    }
}
