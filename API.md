# 커피숍 시스템 API 명세서

---

## 공통

### Base URL
```
http://{host}
```

### 공통 응답 형식

모든 API는 아래 형식으로 응답합니다.

```json
{
  "success": true,
  "data": { },
  "code": 200,
  "error": null,
  "timestamp": "2025/04/06"
}
```

| 필드 | 타입 | 설명 |
|------|------|------|
| `success` | boolean | 요청 성공 여부 |
| `data` | object \| null | 응답 데이터 |
| `code` | int | HTTP 상태 코드 |
| `error` | string \| null | 에러 메시지 (실패 시) |
| `timestamp` | string | 응답 시각 (`yyyy/MM/dd`) |

---

## 1. 커피 메뉴 목록 조회

### `GET /api/menus`

전체 메뉴 목록을 조회합니다.

#### Request

없음

#### Response

**HTTP Status: `200 OK`**

```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "name": "아메리카노",
      "price": 4500.00
    },
    {
      "id": 2,
      "name": "카페라떼",
      "price": 5000.00
    }
  ],
  "code": 200,
  "error": null,
  "timestamp": "2025/04/06"
}
```

**data 배열 항목**

| 필드 | 타입 | 설명 |
|------|------|------|
| `id` | Long | 메뉴 ID |
| `name` | String | 메뉴 이름 |
| `price` | BigDecimal | 메뉴 가격 |

---

## 2. 포인트 충전

### `POST /api/points/charge`

사용자의 포인트를 충전합니다.

#### Request

**Content-Type:** `application/json`

```json
{
  "userId": 1,
  "chargeAmount": 10000
}
```

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| `userId` | Long | ✅ | 사용자 ID |
| `chargeAmount` | BigDecimal | ✅ | 충전 금액 (최소 100원) |

**유효성 검사**
- `userId`: null 불가
- `chargeAmount`: null 불가, 100 이상이어야 함

#### Response

**HTTP Status: `201 Created`**

```json
{
  "success": true,
  "data": {
    "userId": 1,
    "balance": 15000.00
  },
  "code": 201,
  "error": null,
  "timestamp": "2025/04/06"
}
```

**data 항목**

| 필드 | 타입 | 설명 |
|------|------|------|
| `userId` | Long | 사용자 ID |
| `balance` | BigDecimal | 충전 후 잔액 |

---

## 3. 커피 주문/결제

### `POST /api/orders`

메뉴를 주문하고 결제합니다.

#### Request

**Content-Type:** `application/json`

```json
{
  "userId": 1,
  "menuId": 2
}
```

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| `userId` | Long | ✅ | 사용자 ID |
| `menuId` | Long | ✅ | 주문할 메뉴 ID |

**유효성 검사**
- `userId`: null 불가
- `menuId`: null 불가

#### Response

**HTTP Status: `201 Created`**

```json
{
  "success": true,
  "data": {
    "orderId": 101,
    "userId": 1,
    "menuId": 2,
    "paidAt": "2025-04-06T10:30:00"
  },
  "code": 201,
  "error": null,
  "timestamp": "2025/04/06"
}
```

**data 항목**

| 필드 | 타입 | 설명 |
|------|------|------|
| `orderId` | Long | 주문 ID |
| `userId` | Long | 사용자 ID |
| `menuId` | Long | 주문된 메뉴 ID |
| `paidAt` | LocalDateTime | 결제 완료 시각 |

---

## 4. 인기 메뉴 목록 조회

### `GET /api/menus/popular`

최근 7일간 주문 건수 기준 상위 3개 인기 메뉴를 조회합니다.

#### Request

없음

#### Response

**HTTP Status: `200 OK`**

```json
{
  "success": true,
  "data": [
    {
      "menuId": 1,
      "name": "아메리카노",
      "price": 4500.00,
      "orderCount": 320.0
    },
    {
      "menuId": 3,
      "name": "콜드브루",
      "price": 5500.00,
      "orderCount": 210.0
    },
    {
      "menuId": 2,
      "name": "카페라떼",
      "price": 5000.00,
      "orderCount": 180.0
    }
  ],
  "code": 200,
  "error": null,
  "timestamp": "2025/04/06"
}
```

**data 배열 항목**

| 필드 | 타입 | 설명 |
|------|------|------|
| `menuId` | Long | 메뉴 ID |
| `name` | String | 메뉴 이름 |
| `price` | BigDecimal | 메뉴 가격 |
| `orderCount` | double | 최근 7일간 주문 수량 |