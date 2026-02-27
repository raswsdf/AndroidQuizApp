## 프로젝트 소개
: 시사·경제 퀴즈 학습 애플리케이션

본 프로젝트는 공공데이터포털에서 제공하는  
**「재정경제부_시사경제용어 정보」 XLSX 데이터**를 기반으로 제작된  
카테고리별 시사·경제 퀴즈 학습 안드로이드 애플리케이션

---

## 프로젝트 목적

- 공공데이터를 활용한 학습 애플리케이션 구현 및 **앱프로그래밍 기초 및 실습 수업** 과제
- 안드로이드 Activity 생명주기 이해
- Intent를 활용한 데이터 전달 구조 설계
- 오답 기반 반복 학습 시스템 구현
- SharedPreferences를 활용한 데이터 영속성 관리

---

## 사용 데이터

- **출처**: 공공데이터포털 (data.go.kr)
- **데이터명**: 재정경제부_시사경제용어 정보
- **형식**: XLSX
- **활용 방식**
    - 용어 및 설명 데이터를 기반으로 객관식 문제 구성
    - 카테고리별(경제 / 금융 / 공공) 분류
    - 앱 내부 문제 데이터 구조로 가공 후 활용

---

## 주요 기능

### 1. 카테고리별 퀴즈 선택

- Economy (경제)
- Finance (금융)
- Public (공공)

선택한 카테고리의 문제만 출제

---

### 2. 퀴즈 진행 기능

- 객관식 문제 제공
- 선택 즉시 정답 여부 판별
- 점수 자동 계산
- 오답 자동 저장

**관련 클래스**
- `QuizActivity`
- `QuizData`
- `Question`

---

### 3. 결과 화면

- 총 점수 출력
- 정답/오답 확인
- 오답노트 이동 기능 제공

**관련 클래스**
- `ResultActivity`

---

### 4. 오답노트 기능

#### ▪ 오답 자동 저장
- 틀린 문제는 `SharedPreferences`에 저장
- 카테고리별 분리 저장
- 앱 재실행 후에도 유지

#### ▪ 오답노트 조회
- 카테고리별 오답 목록 확인
- 복습 퀴즈 재시작 가능

**관련 클래스**
- `WrongNoteActivity`
- `WrongNoteActivityDetail`
- `SharedPreferencesManager`
- `WrongAnswer`
- `WrongAnswerStorage`

---

### 5. 오답 복습 퀴즈

- 저장된 오답 문제만 재출제
- 반복 학습 구조 제공

**관련 클래스**
- `QuizReviewActivity`

---

### 저장 구조

- Android `SharedPreferences` 사용
- 카테고리 Key 기반 저장
- JSON 직렬화 방식 사용
- 앱 종료 후에도 데이터 유지

---
### 화면 흐름 구조

MainActivity
↓
QuizActivity
↓
ResultActivity
↓
WrongNoteActivity
↓
WrongNoteActivityDetail

(복습 흐름)
WrongNoteActivityDetail
↓
QuizReviewActivity