package com.practice.paymentlimit.payment;

/** 에러 응답의 {@code code} 값. 계약이므로 이름을 바꾸지 않습니다. */
public enum ErrorCode {
    INVALID_REQUEST,
    DUPLICATE_PAYMENT,
    LIMIT_EXCEEDED,
    PAYMENT_NOT_FOUND,
    ALREADY_CANCELED
}
