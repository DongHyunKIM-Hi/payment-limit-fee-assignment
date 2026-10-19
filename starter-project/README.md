# 결제 한도 차감과 수수료 정책 시작 프로젝트

- 과제 설명은 상위 폴더의 `README.md`를 먼저 읽어 주세요.
- 이 프로젝트에는 `TODO`가 있는 결제 컨트롤러, 수정할 수 없는 한도 저장소, 리팩토링할 기존 수수료 코드가 들어 있습니다.
- 이 폴더를 제출 저장소의 루트로 사용합니다.

## 실행과 테스트

```bash
./gradlew bootRun   # 포트 8080
./gradlew test
```

Docker나 다른 서버는 필요하지 않습니다. 문제 2, 4에서 작성한 테스트도 `./gradlew test`로 함께 실행됩니다.

## 수정하면 안 되는 것

경로는 `src/main/java/com/practice/paymentlimit/` 아래 기준입니다.

- 한도 저장소: `domain/payment/repository/LimitStore.java`
- 결제 기록 클래스: `common/entity/PaymentRecord.java`, `common/enums/PaymentState.java`
- API 경로와 요청 및 응답 형식
  - `PaymentController`의 경로와 메서드 시그니처
  - `domain/payment/model/request/`, `domain/payment/model/response/`, `domain/fee/model/response/`
  - 요청 값과 에러 코드: `common/enums/Grade.java`, `PayType.java`, `ErrorCode.java`
- 설정 파일: `src/main/resources/application.yml`

## 자유롭게 고치거나 추가해도 되는 것

- `PaymentController`의 `TODO` 구현
- `domain/fee/service/LegacyFeeCalculator.java` 리팩토링 또는 새 클래스로 교체
- 서비스, 도메인, 유틸 클래스 추가
- 테스트 코드

## 제출 전 확인 사항

- [ ] `docs/answer.md`를 작성했다 (`../docs-template/answer.md`를 복사해서 시작)
- [ ] `./gradlew test`가 통과한다
- [ ] `../requests.http`의 시나리오가 README의 기대 결과와 같다
