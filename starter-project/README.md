# 결제 한도 차감과 수수료 정책 시작 프로젝트

- 과제 설명은 과제 저장소 최상위의 `README.md`(이하 과제 설명)를 먼저 읽어 주세요.
- 이 프로젝트에는 `TODO`가 있는 결제 컨트롤러, 수정할 수 없는 한도 저장소, 리팩토링할 기존 수수료 계산 코드가 들어 있습니다.
- 이 폴더 안의 파일이 제출 저장소의 최상위가 됩니다. 제출할 때 이 파일은 실행 방법과 직접 확인한 시나리오를 적은 제출 README로 바꿔 써 주세요.

## 실행과 테스트

```bash
./gradlew bootRun   # 포트 8080
./gradlew test
```

Docker나 다른 서버는 필요하지 않습니다. 문제 2, 4에서 작성한 테스트도 `./gradlew test`로 함께 실행됩니다.

## 수정하면 안 되는 것

과제 설명 2장과 같은 내용입니다. 경로는 `src/main/java/com/practice/paymentlimit/` 아래 기준입니다.

- 한도 저장소: `domain/payment/repository/LimitStore.java` (메서드 호출은 괜찮습니다)
- 결제 기록 클래스: `common/entity/PaymentRecord.java`, `common/enums/PaymentState.java`
- API 경로와 요청 및 응답 형식
  - `PaymentController`의 경로와 메서드 시그니처(메서드 이름, 매개변수, 반환 타입)
  - `domain/*/model/`의 요청과 응답 클래스, `common/exception/ErrorResponse.java`의 필드
  - `common/enums/Grade.java`, `PayType.java`, `ErrorCode.java`의 값 목록 (메서드나 필드 추가는 괜찮습니다)
- 설정 파일: `src/main/resources/application.yml`

## 자유롭게 고치거나 추가해도 되는 것

- `PaymentController`의 `TODO` 구현
- `domain/fee/service/LegacyFeeCalculator.java` 리팩토링 또는 새 클래스로 교체
- 서비스, 도메인, 유틸 클래스 추가
- 테스트 코드

## 제출 전 확인 사항

- [ ] `docs/answer.md`를 작성했다 (과제 저장소의 `docs-template/answer.md`를 복사해서 시작)
- [ ] `./gradlew test`가 통과한다
- [ ] 과제 저장소의 `requests.http` 요청 결과가 과제 설명의 기대 결과와 같다
