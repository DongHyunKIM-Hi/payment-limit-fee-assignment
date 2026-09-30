package com.practice.paymentlimit.payment;

import com.practice.paymentlimit.fee.LegacyFeeCalculator;
import com.practice.paymentlimit.limit.LimitStore;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * 결제 승인·취소·한도 조회·수수료 계산 API. 경로와 요청·응답 형식(계약)은 바꾸지 않습니다.
 *
 * 아래 메서드는 아직 구현되어 있지 않습니다 ({@code TODO}). 가이드 5장(계약)과 6장(수수료 규칙)을
 * 읽고 구현하세요. 필요한 만큼 서비스·도메인 클래스를 자유롭게 추가해도 됩니다.
 */
@RestController
public class PaymentController {

    private final LimitStore limitStore;
    private final LegacyFeeCalculator legacyFeeCalculator;

    public PaymentController(LimitStore limitStore, LegacyFeeCalculator legacyFeeCalculator) {
        this.limitStore = limitStore;
        this.legacyFeeCalculator = legacyFeeCalculator;
    }

    @PostMapping("/api/v1/payments")
    public PaymentApprovedResponse approve(@Valid @RequestBody PaymentRequest request) {
        // TODO: 구현하세요.
        //  - 가이드 5-1, 5-5의 규칙(중복 검사, 한도 검사, 수수료 계산)을 지키세요.
        //  - 동시에 여러 요청이 와도 규칙이 지켜져야 합니다 (5장, 8장 시나리오 참고).
        //  - limitStore는 절대 수정하지 마세요. 그 위에서 여러분의 서비스 코드로 안전하게 만드세요.
        throw new UnsupportedOperationException("TODO: 결제 승인을 구현하세요");
    }

    @PostMapping("/api/v1/payments/{paymentId}/cancel")
    public CancelResponse cancel(@PathVariable String paymentId) {
        // TODO: 구현하세요. (없는 결제 404, 이미 취소된 결제 409, 성공 200 + 한도 복원)
        throw new UnsupportedOperationException("TODO: 결제 취소를 구현하세요");
    }

    @GetMapping("/api/v1/users/{userId}/limit")
    public LimitResponse getLimit(@PathVariable String userId) {
        // TODO: 구현하세요. 처음 보는 사용자는 usedAmount 0을 반환합니다.
        throw new UnsupportedOperationException("TODO: 한도 조회를 구현하세요");
    }

    @GetMapping("/api/v1/fees")
    public FeeQueryResponse getFee(@RequestParam Grade grade, @RequestParam PayType payType, @RequestParam long amount) {
        // TODO: 구현하세요. 6장의 규칙 표를 기준으로 계산해야 합니다.
        //  legacyFeeCalculator는 표와 다른 부분이 있는 예전 코드입니다. 표를 기준으로
        //  이 코드를 고치거나, 새 클래스를 만들어 대체하세요.
        throw new UnsupportedOperationException("TODO: 수수료 계산을 구현하세요");
    }
}
