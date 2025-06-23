# 유레카 종합 프로젝트 - Yoajung Service

## 프로젝트 개요
- 사용자의 통신 성향을 분석해 요금제 및 상품을 비교·추천하는 AI 챗봇 서비스를 개발했습니다.
- 챗봇은 멀티턴 대화와 금칙어 필터링 기능을 지원하며, 이를 통해 사용자에게 맞춤형 상담 경험을 제공합니다.

## 팀원 소개

| 이름   | 역할             | 주요 구현 내용                                 | GitHub                                                                                                                                                          |
|--------|------------------|----------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 이희용 | AI, 백엔드(팀장)  | 프롬프트 엔지니어링, AI 챗봇                   | <a href="https://github.com/eddie-backdev"><img src="https://avatars.githubusercontent.com/u/50799519?v=4" width="100" height="100" alt="eddie-backdev" /></a> |
| 박소연 | AI, 백엔드       | 리뷰 비즈니스 로직, AI 챗봇                    | <a href="https://github.com/so-yeon1"><img src="https://avatars.githubusercontent.com/u/82212460?v=4" width="100" height="100" alt="so-yeon1" /></a>           |
| 신혜원 | 백엔드           | 요금제 비즈니스 로직, 요금제 추천 AI           | <a href="https://github.com/hyew0nn"><img src="https://avatars.githubusercontent.com/u/113279618?v=4" width="100" height="100" alt="hyew0nn" /></a>            |
| 이재윤 | AI, 백엔드       | 프롬프트 엔지니어링, AI 챗봇                   | <a href="https://github.com/iju42829"><img src="https://avatars.githubusercontent.com/u/116072376?v=4" width="100" height="100" alt="iju42829" /></a>          |
| 정동현 | 백엔드           | 서비스 인증 / 인가                            | <a href="https://github.com/Iamcalmdown"><img src="https://avatars.githubusercontent.com/u/144317474?v=4" width="100" height="100" alt="Iamcalmdown" /></a>     |
| 홍석준 | 백엔드           | 요금제 비즈니스 로직, 리뷰 요약 AI             | <a href="https://github.com/seokjuun"><img src="https://avatars.githubusercontent.com/u/45346977?v=4" width="100" height="100" alt="seokjuun" /></a>           |

## 아키텍처

## 챗봇 흐름도
![chatbot](https://github.com/user-attachments/assets/1ea7ca53-a415-4d7c-96f7-bc2ed17a7522)

## 주요 기능

### 서비스

* 인증 인가
    * 로그인, 로그아웃, 회원가입


* 요금제
    * 상세, 필터, 정렬, 페이징


* 리뷰
    * CRUD, 좋아요, 평점


* AI
    * 키워드 추출, 멀티턴, 욕설 필터링, 요금제 추천

### 관리자

* 인증 인가
    * 로그인, 로그아웃


* 요금제 관리
    * CRUD


* 리뷰 관리
    * 조회, 삭제


* 통계
    * 일간, 주간, 월간 챗봇(채팅량, 사용자 수) 통계
    * 월간 요금제 선호도 통계

---

## 기술 스택

- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Security
- Spring AI
- OAuth 2.0
- Lombok
- MySQL
- Query DSL
- JUnit5
- Docker
- Redis
- Swagger

---

## ERD

![ERD](erd.png)

---
