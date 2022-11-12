# RC 5기 Rising Test - Coupangeats 모의 외주
coupangeats 모의 외주(클론코딩) - API 서버 구축
*본 프로젝트는 소프트스퀘어드(라이징캠프) 소유의 템플릿을 사용했음을 밝힙니다.

## 바로가기
[1. 진행기간 및 참여인원](#1.-진행기간-및-참여인원)  
[2. 사용 기술](#2.-사용-기술)  
[3. ERD 설계](3.-ERD-설계)     
[4. 프로젝트 기획서 및 API 명세서](4.-프로젝트-기획서-및-API-명세서)   
[5. 핵심 구현 기능](5.-핵심-구현-기능)    
[6. 회고](6.-회고)   

## 1. 진행기간 및 참여인원
2022-05-21 ~ 2022-06-03   
**Server(API)** - [Dona](https://github.com/YeJinHong), Core (2명)   
**Client(Android)** - Nova (1명)

## 2. 사용 기술
- Java 11
- Spring Boot 2.4.2
- Gradle
- MySQL 8
- lombock

## 3. ERD 설계
![ERD Diagram](https://user-images.githubusercontent.com/33932851/198862318-f803146c-5b69-4b04-b1d2-582668198680.png)

## 4. 프로젝트 기획서 및 API 명세서
### 프로젝트 기획서
https://docs.google.com/document/d/14ch80QWX1A5jdYGnlao2rd_hXcRzRb8sIhQVkYK1xl8/edit


### API 명세서
https://docs.google.com/spreadsheets/d/1yS9TPf5F2BfYH447sij0xPvrUuIxi9Cir5M2x-C2jhk/edit?usp=sharing


## 5. 핵심 구현 기능 
배달 메뉴 선택 및 주문
- 가게 선택
- 메뉴 선택
- 카트 담기
- 주문하기

**Dona 최종 발표 영상**   
https://drive.google.com/file/d/1qufQzBhPWlVnVcZGRYT5Qm_9PiaKJNAm/view?usp=sharing


## 6.회고
### 배운것, 경험한 것   
 - 도메인 적용 및 AWS 서버 배포 경험
 - Rest API 개념 및 작성 가이드 숙지
 - Postman - GET, POST 데이터 전송하는 법
 - API 명세서 작성법
 - ERD 도구(aquery) 사용법
 - DB 외부 접속 방법
 - 계층 관계를 가지는 DB 구현 경험
 - application.yml 설정 파일 이해
    - dev, product 별로 설정 파일을 분리해서 적용할 수 있다는 것을 이해했습니다.
 - JWT 사용 경험
 - Spring - Intercepter 구현
    
### 아쉬운 점
- DB 구축시 생각이 유연하지 못했다.(정규화와 FK 설정이 무조건적으로 필수라고 생각)
- 시간에 쫒겨 응용할만한 기술을 적용하지 못함.
 - Jenkins
 - MyBatis
 - AWS ElasticSearch
 - AWS S3, Firebase의 데이터 저장소 미사용
    - 대신 DB에 이미지 저장 url을 파일 url을 줌으로서 구현했다.
 - application.yml 사용법 
    - 설정파일 분리 적용을 위해 여러 코드를 찾았지만, 대개 적용이 되지 않았다.
    - 여러개중 적용이 되는 코드를 찾았을 뿐 설정파일에 대한 이해를 한것 같지 않았다.
 - 휴대폰 인증 기능 미구현
   - 인증용 번호 구매 방법과 코드 작성까지 찾았지만 적용하진 못했다. 
