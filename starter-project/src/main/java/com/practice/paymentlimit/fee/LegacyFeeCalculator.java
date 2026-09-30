package com.practice.paymentlimit.fee;

import com.practice.paymentlimit.payment.Grade;
import com.practice.paymentlimit.payment.PayType;
import org.springframework.stereotype.Component;

/**
 * 예전에 누가 급하게 짜 놓은 수수료 계산 코드입니다. 지금은 아무도 이 코드를 처음부터 짠 사람이
 * 없어서, 맞는지 아무도 확신하지 못합니다. 동작은 하니 일단 그대로 쓰고 있습니다.
 *
 * TODO: 리팩토링 대상입니다. 가이드 6장의 규칙 표를 기준으로 다시 작성하세요.
 * (이 클래스를 고쳐도 되고, 새 클래스를 만들어 컨트롤러에서 그것을 쓰도록 바꿔도 됩니다.)
 */
@Component
public class LegacyFeeCalculator {

    public long calculate(Grade grade, PayType payType, long amount) {
        if (amount < 1000) {
            return 0;
        }
        double rate;
        if (grade == Grade.VIP) {
            if (payType == PayType.DOMESTIC) {
                return 0;
            } else {
                rate = 1.5;
            }
        } else {
            if (payType == PayType.DOMESTIC) {
                rate = 1.0;
            } else {
                rate = 3.0;
            }
            if (grade == Grade.SILVER) {
                rate = rate * 0.8;
            } else if (grade == Grade.GOLD) {
                rate = rate * 0.6;
            } else {
                // BASIC은 그대로
            }
        }
        if (amount > 1000000) {
            rate = rate - 0.2;
        }
        double raw = amount * (rate / 100.0);
        long fee = Math.round(raw);
        if (fee < 100) {
            fee = 100;
        }
        return fee;
    }
}
