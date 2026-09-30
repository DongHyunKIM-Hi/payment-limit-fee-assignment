# 과제 2. 결제 한도 차감 + 수수료 정책 — 시작 프로젝트

과제 설명은 상위 폴더의 `README.md`(가이드)를 먼저 읽어 주세요. 이 프로젝트는 `TODO`가 있는 결제 컨트롤러, 수정 금지 저장소(`limit` 패키지), 리팩토링 대상 레거시 수수료 코드(`fee` 패키지)를 갖춘 뼈대입니다.

## 실행

```
./gradlew bootRun
```
포트는 `8080`입니다. Docker나 다른 서버는 필요 없습니다.

## 테스트

```
./gradlew test
```
문제 2, 4에서 여러분이 작성한 테스트가 여기서 함께 실행됩니다.

## 수정하면 안 되는 것

- `limit` 패키지 전체 (`LimitStore`, `PaymentRecord`, `PaymentState`)
- 요청·응답 DTO와 에러 코드 (`payment` 패키지의 `PaymentRequest`, `*Response`, `ErrorCode`, `Grade`, `PayType`)
- `build.gradle`, `settings.gradle`, `application.yml`

## 자유롭게 고치거나 추가해도 되는 것

- `PaymentController`의 `TODO` 구현
- `fee.LegacyFeeCalculator` 리팩토링 (또는 새 클래스로 교체)
- 서비스, 도메인, 유틸 클래스 추가
- 테스트 코드

## 제출 전 체크

- [ ] `docs/answer.md`를 채웠는가 (`../docs-template/answer.md`를 복사해서 시작하세요)
- [ ] `./gradlew test`가 통과하는가
- [ ] `requests.http`의 시나리오가 가이드의 기대 결과와 같은가
