#!/usr/bin/env bash
# 시나리오 자동 확인 (S1~S12)
#
# 사용법
#   1) 서버 실행: ./gradlew bootRun   (8080 포트. Docker나 다른 서버는 필요 없습니다)
#   2) bash check-scenarios.sh
#
# - 실행할 때마다 새 결제 ID와 사용자 ID를 쓰므로 서버를 재시작하지 않고 몇 번이든 돌릴 수 있습니다.
# - 한도 저장소가 호출마다 50~150ms 지연되므로 전체 1분 정도 걸립니다.
# - 윈도우(Git Bash 등)에서 curl이 줄 끝에 캐리지리턴(\r)을 붙여도 비교가 깨지지 않도록
#   응답을 받는 곳에서 \r을 제거합니다.
# - 다른 포트로 띄웠다면: SERVER_URL=http://localhost:8081 bash check-scenarios.sh

S=${SERVER_URL:-http://localhost:8080}
H='Content-Type: application/json'

res1=""; res2=""; res3=""; res4=""; res5=""; res6=""
res7=""; res8=""; res9=""; res10=""; res11=""; res12=""
PASS=0; FAIL=0

ok(){ eval "res$1=\"\$(printf '\033[32m[통과]\033[0m S%-3s %s' \"$1\" \"$2\")\""; PASS=$((PASS+1)); }
no(){ eval "res$1=\"\$(printf '\033[31m[실패]\033[0m S%-3s %s — %s' \"$1\" \"$2\" \"$3\")\""; FAIL=$((FAIL+1)); }
code(){ curl -s -o /dev/null -w '%{http_code}' --max-time 15 "$@" | tr -d '\r'; }
pay(){ curl -s -o /dev/null -w '%{http_code}\n' --max-time 15 -X POST "$S/api/v1/payments" -H "$H" \
  -d "{\"paymentId\":\"$1\",\"userId\":\"$2\",\"amount\":$3,\"grade\":\"$4\",\"payType\":\"$5\"}" | tr -d '\r'; }
cancel(){ curl -s -o /dev/null -w '%{http_code}\n' --max-time 15 -X POST "$S/api/v1/payments/$1/cancel" | tr -d '\r'; }
used(){ curl -s --max-time 15 "$S/api/v1/users/$1/limit" | tr -d '\r' | sed -n 's/.*"usedAmount":\([0-9-]*\).*/\1/p'; }
feeof(){ curl -s --max-time 15 "$S/api/v1/fees?grade=$1&payType=$2&amount=$3" | tr -d '\r' | sed -n 's/.*"fee":\([0-9-]*\).*/\1/p'; }
field(){ printf '%s' "$1" | tr -d '\r' | sed -n "s/.*\"$2\":\"\{0,1\}\([^\",}]*\)\"\{0,1\}.*/\1/p" | head -1; }

echo "서버($S) 연결을 확인합니다..."
[ "$(code "$S/api/v1/users/PING-0/limit")" = "000" ] && { echo "서버에 연결할 수 없습니다. ./gradlew bootRun 후 다시 실행해 주세요."; exit 1; }

R=$RANDOM$RANDOM

# ---- 1) 기본 승인과 중복 (S1, S2) ----
echo "승인과 중복 시나리오(S1~S2)를 확인합니다..."
ID1="P1-$R"; U1="U1-$R"
r=$(curl -s -w '\n%{http_code}' --max-time 15 -X POST "$S/api/v1/payments" -H "$H" \
  -d "{\"paymentId\":\"$ID1\",\"userId\":\"$U1\",\"amount\":50000,\"grade\":\"GOLD\",\"payType\":\"DOMESTIC\"}" | tr -d '\r')
c=${r##*$'\n'}; b=${r%$'\n'*}
f=$(field "$b" fee); rl=$(field "$b" remainingLimit); st=$(field "$b" status)
if [ "$c" = "200" ] && [ "$f" = "250" ] && [ "$rl" = "950000" ] && [ "$st" = "APPROVED" ]; then
  ok 1 "정상 승인 200 (fee 250, 잔여 950,000, APPROVED)"
else no 1 "정상 승인 200 (fee 250, 잔여 950,000, APPROVED)" "응답 $c, fee ${f:-없음}, 잔여 ${rl:-없음}, 상태 ${st:-없음}"; fi

r=$(curl -s -w '\n%{http_code}' --max-time 15 -X POST "$S/api/v1/payments" -H "$H" \
  -d "{\"paymentId\":\"$ID1\",\"userId\":\"$U1\",\"amount\":50000,\"grade\":\"GOLD\",\"payType\":\"DOMESTIC\"}" | tr -d '\r')
c=${r##*$'\n'}; b=${r%$'\n'*}
ec=$(field "$b" code); u=$(used "$U1")
if [ "$c" = "409" ] && [ "$ec" = "DUPLICATE_PAYMENT" ] && [ "$u" = "50000" ]; then
  ok 2 "같은 ID 재요청 409 DUPLICATE_PAYMENT, 사용액 그대로"
else no 2 "같은 ID 재요청 409 DUPLICATE_PAYMENT, 사용액 그대로" "응답 $c(${ec:-코드 없음}), 사용액 ${u:-?}"; fi

# ---- 2) 동시 요청 (S3~S5) ----
echo "동시 요청 시나리오(S3~S5)를 확인합니다..."
ID3="P3-$R"; U3="U3-$R"
codes=$( ( for i in 1 2 3 4 5 6 7 8 9 10; do pay "$ID3" "$U3" 10000 BASIC DOMESTIC & done; wait ) )
n200=$(printf '%s\n' "$codes" | tr -d '\r' | grep -c '^200$'); n409=$(printf '%s\n' "$codes" | tr -d '\r' | grep -c '^409$')
u=$(used "$U3")
if [ "$n200" = "1" ] && [ "$n409" = "9" ] && [ "$u" = "10000" ]; then
  ok 3 "같은 사용자 같은 ID 동시 10건 → 200 1건, 409 9건, 사용액 10,000"
else no 3 "같은 사용자 같은 ID 동시 10건 → 1건만 승인" "200 ${n200}건, 409 ${n409}건, 사용액 ${u:-?}"; fi

ID4="P4-$R"
codes=$( ( for i in 1 2 3 4 5 6 7 8 9 10; do pay "$ID4" "U4-$R-$i" 10000 BASIC DOMESTIC & done; wait ) )
n200=$(printf '%s\n' "$codes" | tr -d '\r' | grep -c '^200$'); n409=$(printf '%s\n' "$codes" | tr -d '\r' | grep -c '^409$')
sum=0
for i in 1 2 3 4 5 6 7 8 9 10; do u=$(used "U4-$R-$i"); sum=$((sum + ${u:-0})); done
if [ "$n200" = "1" ] && [ "$n409" = "9" ] && [ "$sum" = "10000" ]; then
  ok 4 "다른 사용자 10명 같은 ID 동시 → 200 1건, 409 9건, 사용액 합 10,000"
else no 4 "다른 사용자 10명 같은 ID 동시 → 1건만 승인" "200 ${n200}건, 409 ${n409}건, 사용액 합 $sum"; fi

U5="U5-$R"
codes=$( ( pay "P5A-$R" "$U5" 400000 BASIC DOMESTIC & pay "P5B-$R" "$U5" 400000 BASIC DOMESTIC & \
           pay "P5C-$R" "$U5" 400000 BASIC DOMESTIC & wait ) )
n200=$(printf '%s\n' "$codes" | tr -d '\r' | grep -c '^200$'); n422=$(printf '%s\n' "$codes" | tr -d '\r' | grep -c '^422$')
u=$(used "$U5")
if [ "$n200" = "2" ] && [ "$n422" = "1" ] && [ "$u" = "800000" ]; then
  ok 5 "400,000원 3건 동시 → 200 2건, 422 1건, 사용액 800,000"
else no 5 "400,000원 3건 동시 → 한도 안에서만 승인" "200 ${n200}건, 422 ${n422}건, 사용액 ${u:-?}"; fi

# ---- 3) 한도 경계와 입력 오류 (S6, S7) ----
echo "한도 경계와 입력 오류(S6~S7)를 확인합니다..."
U6="U6-$R"
c1=$(pay "P6A-$R" "$U6" 1000000 BASIC DOMESTIC)
c2=$(pay "P6B-$R" "$U6" 1 BASIC DOMESTIC)
c3=$(cancel "P6A-$R")
c4=$(pay "P6B-$R" "$U6" 1 BASIC DOMESTIC)
u=$(used "$U6")
if [ "$c1 $c2 $c3 $c4" = "200 422 200 200" ] && [ "$u" = "1" ]; then
  ok 6 "한도 경계: 1,000,000 승인 → 1원 422 → 취소 → 거절됐던 ID 재요청 200"
else no 6 "한도 경계 (200, 422, 200, 200 순서)" "응답 [$c1 $c2 $c3 $c4], 최종 사용액 ${u:-?} (기대 1)"; fi

c1=$(pay "P7A-$R" "U7-$R" 0 BASIC DOMESTIC)
c2=$(curl -s -o /dev/null -w '%{http_code}' --max-time 15 -X POST "$S/api/v1/payments" -H "$H" \
  -d "{\"userId\":\"U7-$R\",\"amount\":1000,\"grade\":\"BASIC\",\"payType\":\"DOMESTIC\"}" | tr -d '\r')
c3=$(pay "P7C-$R" "U7-$R" 1000 DIAMOND DOMESTIC)
c4=$(code "$S/api/v1/fees?grade=BASIC&payType=DOMESTIC&amount=0")
c5=$(code "$S/api/v1/fees?grade=DIAMOND&payType=DOMESTIC&amount=1000")
if [ "$c1 $c2 $c3 $c4 $c5" = "400 400 400 400 400" ]; then
  ok 7 "입력 오류 400 (금액 0, 필수값 누락, 없는 등급, 수수료 API 2건)"
else no 7 "입력 오류 400" "금액0=$c1, 누락=$c2, 등급=$c3, 수수료금액0=$c4, 수수료등급=$c5"; fi

# ---- 4) 취소 (S8~S10) ----
echo "취소 시나리오(S8~S10)를 확인합니다..."
ID8="P8-$R"; U8="U8-$R"
c0=$(pay "$ID8" "$U8" 50000 BASIC DOMESTIC)
r=$(curl -s -w '\n%{http_code}' --max-time 15 -X POST "$S/api/v1/payments/$ID8/cancel" | tr -d '\r')
c=${r##*$'\n'}; b=${r%$'\n'*}
ra=$(field "$b" restoredAmount); rl=$(field "$b" remainingLimit)
c404=$(code -X POST "$S/api/v1/payments/NO-SUCH-$R/cancel")
u=$(used "$U8")
if [ "$c0" = "200" ] && [ "$c" = "200" ] && [ "$ra" = "50000" ] && [ "$rl" = "1000000" ] && [ "$u" = "0" ] && [ "$c404" = "404" ]; then
  ok 8 "취소 200 (복원 50,000, 잔여 1,000,000) + 없는 결제 404"
else no 8 "취소 200과 한도 복원 + 없는 결제 404" "승인 $c0, 취소 $c(복원 ${ra:-없음}, 잔여 ${rl:-없음}), 사용액 ${u:-?}, 404 확인 $c404"; fi

ID9="P9-$R"; U9="U9-$R"
pay "$ID9" "$U9" 50000 BASIC DOMESTIC >/dev/null
codes=$( ( for i in 1 2 3 4 5 6 7 8 9 10; do cancel "$ID9" & done; wait ) )
n200=$(printf '%s\n' "$codes" | tr -d '\r' | grep -c '^200$'); n409=$(printf '%s\n' "$codes" | tr -d '\r' | grep -c '^409$')
u=$(used "$U9")
if [ "$n200" = "1" ] && [ "$n409" = "9" ] && [ "$u" = "0" ]; then
  ok 9 "같은 결제 취소 동시 10건 → 200 1건, 409 9건, 사용액 한 번만 복원"
else no 9 "같은 결제 취소 동시 10건 → 1건만 성공" "200 ${n200}건, 409 ${n409}건, 사용액 ${u:-?} (기대 0)"; fi

ID10="P10-$R"; U10="U10-$R"
pay "$ID10" "$U10" 70000 BASIC DOMESTIC >/dev/null
cancel "$ID10" >/dev/null
c=$(pay "$ID10" "$U10" 70000 BASIC DOMESTIC)
u=$(used "$U10")
if [ "$c" = "409" ] && [ "$u" = "0" ]; then
  ok 10 "취소한 ID로 재승인 요청 409, 사용액 그대로"
else no 10 "취소한 ID로 재승인 요청 409, 사용액 그대로" "응답 $c, 사용액 ${u:-?} (기대 0)"; fi

# ---- 5) 승인과 취소 혼합 (S11, 3회 반복) ----
echo "승인과 취소 혼합(S11)을 3회 반복 확인합니다..."
fails11=""
for k in 1 2 3; do
  UK="U11-$R-$k"; A="P11A-$R-$k"; B="P11B-$R-$k"
  ca=$(pay "$A" "$UK" 900000 BASIC DOMESTIC)
  codes=$( ( cancel "$A" & pay "$B" "$UK" 100000 BASIC DOMESTIC & wait ) )
  sorted=$(printf '%s\n' "$codes" | tr -d '\r' | sort | tr '\n' ' ' | sed 's/ *$//')
  u=$(used "$UK")
  if [ "$ca" != "200" ] || [ "$sorted" != "200 200" ] || [ "$u" != "100000" ]; then
    fails11="$fails11 ${k}회차(응답 [$sorted], 사용액 ${u:-?})"
  fi
done
if [ -z "$fails11" ]; then
  ok 11 "900,000원 취소와 100,000원 승인 동시 3회 → 모두 둘 다 200, 사용액 100,000"
else no 11 "승인과 취소 동시 → 최종 사용액 100,000 (3회 반복)" "실패:$fails11"; fi

# ---- 6) 수수료 규칙 (S12) ----
echo "수수료 규칙(S12)을 확인합니다..."
vectors="BASIC DOMESTIC 999 0
BASIC DOMESTIC 1000 100
BASIC DOMESTIC 12349 123
BASIC DOMESTIC 12350 123
BASIC DOMESTIC 999999 9999
BASIC DOMESTIC 1000000 8000
SILVER DOMESTIC 100000 800
GOLD DOMESTIC 50000 250
GOLD DOMESTIC 1000000 3000
GOLD OVERSEAS 100000 1500
VIP DOMESTIC 5000000 0
VIP OVERSEAS 500 0
VIP OVERSEAS 1000000 13000
BASIC OVERSEAS 1000000 28000
BASIC OVERSEAS 10000000 50000"
nv=0; okv=0; bad12=""
while read -r g p a e; do
  [ -z "$g" ] && continue
  nv=$((nv+1))
  f=$(feeof "$g" "$p" "$a")
  if [ "$f" = "$e" ]; then okv=$((okv+1))
  else bad12="$bad12 [$g $p ${a}원: ${f:-응답없음} (기대 $e)]"; fi
done <<VEC
$vectors
VEC
if [ "$okv" = "$nv" ]; then ok 12 "수수료 경계값 벡터 ${nv}개 모두 표와 일치"
else no 12 "수수료 경계값 벡터 ${nv}개 확인" "$((nv-okv))개 불일치:$bad12"; fi

# ---- 결과 ----
echo; echo "================ 시나리오 확인 결과 ================"
for i in 1 2 3 4 5 6 7 8 9 10 11 12; do eval "printf '%b\n' \"\$res$i\""; done
echo "===================================================="
echo "통과 $PASS / 12"
[ "$FAIL" -gt 0 ] && echo "실패한 시나리오는 README 8장 시나리오 체크리스트에서 해당 항목을 확인해 주세요."
exit 0
