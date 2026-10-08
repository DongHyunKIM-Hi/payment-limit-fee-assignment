package com.practice.paymentlimit.domain.payment.repository;

import com.practice.paymentlimit.common.entity.PaymentRecord;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 이 클래스는 수정하지 않습니다.
 *
 * 사용자별 한도 사용액과 결제 기록을 보관하는 인메모리 저장소입니다.
 * 내부 자료구조는 스레드 안전({@link ConcurrentHashMap})하므로 메서드 호출 하나하나는 안전합니다.
 * 하지만 "사용액을 읽고 → 한도를 판단하고 → 사용액을 쓰는" 여러 호출을 묶은 흐름까지 안전하게
 * 만들어 주지는 않습니다. 그 부분은 여러분이 서비스 코드에서 책임져야 합니다.
 *
 * 모든 메서드는 실제 원격 저장소를 흉내 내기 위해 호출마다 50~150ms의 지연이 있습니다.
 */
@Component
public class LimitStore {

    private final ConcurrentHashMap<String, Long> usedAmounts = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, PaymentRecord> payments = new ConcurrentHashMap<>();

    /** 사용자의 현재 사용액을 읽습니다. 처음 보는 사용자는 0입니다. */
    public long getUsedAmount(String userId) {
        delay();
        return usedAmounts.getOrDefault(userId, 0L);
    }

    /** 사용자의 사용액을 덮어씁니다. */
    public void setUsedAmount(String userId, long amount) {
        delay();
        usedAmounts.put(userId, amount);
    }

    /** 결제 ID로 기록을 조회합니다. 없으면 빈 값입니다. */
    public Optional<PaymentRecord> findPayment(String paymentId) {
        delay();
        return Optional.ofNullable(payments.get(paymentId));
    }

    /**
     * 결제 기록을 저장(또는 덮어쓰기)합니다.
     * 이 메서드 자체는 "조회해서 없으면 저장"의 원자성을 보장하지 않습니다.
     * (그 원자성이 필요하다면 호출하는 쪽에서 만들어야 합니다.)
     */
    public void savePayment(PaymentRecord record) {
        delay();
        payments.put(record.getPaymentId(), record);
    }

    /** 테스트에서만 사용합니다. 저장소를 완전히 비웁니다. */
    public void resetForTest() {
        usedAmounts.clear();
        payments.clear();
    }

    private void delay() {
        try {
            Thread.sleep(50 + ThreadLocalRandom.current().nextLong(101));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(e);
        }
    }
}
