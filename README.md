# kakaopay-task
## 목차
[개발 환경](#개발-환경)  
[문제해결 방법](#문제해결-방법)  
[빌드 방법](#빌드-방법)
[실행 방법](#실행-방법)


## 개발 환경
- Java 11
- Spring boot 2.2.5.RELEASE
- JPA
- H2
- gradle
- OpenCSV
- JWT


## 문제해결 방법
### 1. 주소 데이터 정규화
csv파일의 주소 데이터를 정규화하기 위햏 외부 API를 이용
(https://developers.kakao.com/docs/restapi/local#%EC%A3%BC%EC%86%8C-%EA%B2%80%EC%83%89)

### 2. 프로그램과 주소 엔티티 관계
프로그램과 주소의 N:M 관계를 맵핑하는 GREEN_TOUR 테이블을 생성


## 빌드 방법
#### 1. Git clone을 통해 프로젝트 repository를 복사
```zsh
% git clone https://github.com/2shanna/kakaopay-task.git
```

### 2. 빌드
```zsh
% cd kakaopay-task
% ./gradlew clean build 
```

### 3. jar 기동
```zsh
% java -jar build/libs/kakaopay-task-0.0.1-SNAPSHOT.jar
```

### 4. 아래의 정상기동 로그를 확인(예)
```zsh
2020-03-22 17:24:13.336  INFO 31021 --- [           main] c.k.greentour.GreenTourApplication       : Started GreenTourApplication in 8.483 seconds (JVM running for 9.721)
```

## 실행 방법
### 1. signup 계정 생성 API 

> Method : POST  
> URL : http://localhost:8080/users/sign-up

[입력]  
```zsh
% curl -H "Content-Type: application/json" \
-X POST -d '{ "username": "admin", "password": "password" }' http://localhost:8080/users/sign-up
```

[출력]  
```json
{
   "result": "success"
}
```

### 2. signin 로그인 API
> Method : POST
> URL : http://localhost:8080/users/login

[입력]  
```zsh
% curl -H "Content-Type: application/json" \
-X POST -d '{ "username": "admin", "password": "password" }' http://localhost:8080/users/sign-in
```
[출력]  
```json
{
   "token": "aaa.bbb.ccc"
}
```

### 3. refresh 토큰 재발급 API
> Method : POST
> URL : http://localhost:8080/users/refresh

[입력]  
```zsh
% curl -H "Authorization: Bearer {aaa.bbb.ccc}" -H 'Content-Type: application/json' \
-X POST http://localhost:8080/users/refresh
```
[출력]  
```json
{
   "refreshedToken": "xxx.yyy.zzz"
}
```

### 4. 데이터 파일에서 각 레코드를 데이터베이스에 저장하는 API
> Method : POST
> URL : http://localhost:8080/users/refresh

[입력]  
```zsh
% curl -H "Authorization: Bearer {xxx.yyy.zzz}" -H 'Content-Type: application/json' \
-F 'file=@{path/file.csv}' \ -X POST 'http://localhost:8080/upload' 
```
[출력]  
```json
{
    "regionList": [
        {
            "regionCd": "reg00001",
            "regionName": "천황사로",
            "region1DepthName": "전남",
            "region2DepthName": "영암군",
            "region3DepthName": "영암읍",
            "greenTours": []
        },
        ...
    ],
    "programList": [
        {
            "programId": 1,
            "programName": "자연과 문화를 함께 즐기는 설악산 기행",
            "theme": "문화생태체험,자연생태체험,",
            "originalRegion": "강원도 속초",
            "editedRegion": "강원도 속초",
            "outline": "설악산 탐방안내소, 신흥사, 권금성, 비룡폭포",
            "detail": " 설악산은 왜 설악산이고, 신흥사는 왜 신흥사일까요? 설악산에 대해 정확히 알고, 배우고, 느낄 수 있는 당일형 생태관광입니다.",
            "greenTours": []
        },
        ...
    ]
}
```

### 5. 생태 관광정보 데이터를 조회할 수 있는 API
> Method : GET
> URL : http://localhost:8080/greentour/ecoinfo/region/{recionCd}

[입력]  
```zsh
% curl -H "Authorization: Bearer {xxx.yyy.zzz}" \
-H 'Content-Type: application/json' \
-X GET 'http://localhost:8080/greentour/ecoinfo/region/{recionCd}'
```
[출력]  
```json
[
    {
        "programId": 1,
        "programName": "자연과 문화를 함께 즐기는 설악산 기행",
        "theme": "문화생태체험,자연생태체험,",
        "region": "강원도 속초",
        "outline": "설악산 탐방안내소, 신흥사, 권금성, 비룡폭포",
        "detail": " 설악산은 왜 설악산이고, 신흥사는 왜 신흥사일까요? 설악산에 대해 정확히 알고, 배우고, 느낄 수 있는 당일형 생태관광입니다."
    },
    ...
]
```

### 6. 생태 관광정보 데이터를 추가할 수 있는 API
> Method : POST
> URL : http://localhost:8080/greentour/ecoinfo

[입력]  
```zsh
% curl -H "Authorization: Bearer {xxx.yyy.zzz}" -H 'Content-Type: application/json' \
-X POST 'http://localhost:8080/greentour/ecoinfo'
--data-raw '{
    "programId": {000},
    "programName": {테스트 프로그램명},
    "theme": {테스트 프로그램 테마},
    "region": {테스트 프로그램 지역},
    "outline": {테스트 프로그램 소개},
    "detail": {테스트 프로그램 상세 설명입니다}
}'
```
[출력]  
```json
{
    "programId": 567,
    "programName": "테스트 프로그램명",
    "theme": "테스트 프로그램 테마",
    "region": "테스트 프로그램 지역",
    "outline": "테스트 프로그램 소개",
    "detail": "테스트 프로그램 상세 설명입니다"
}
```

### 7. 생태 관광정보 데이터를 수정할 수 있는 API
> Method : PUT
> URL : http://localhost:8080/greentour/ecoinfo

[입력]  
```zsh
% curl -H "Authorization: Bearer {xxx.yyy.zzz}" -H 'Content-Type: application/json' \
-X PUT 'http://localhost:8080/greentour/ecoinfo'
--data-raw '{
    "programId": {000},
    "programName": {테스트 프로그램명22},
    "theme": {테스트 프로그램 테마22},
    "region": {테스트 프로그램 지역22},
    "outline": {테스트 프로그램 소개22},
    "detail": {테스트 프로그램 상세 설명입니다22}
}'
```
[출력]  
```json
{
    "programId": 567,
    "programName": "테스트 프로그램명22",
    "theme": "테스트 프로그램 테마22",
    "region": "테스트 프로그램 지역22",
    "outline": "테스트 프로그램 소개22",
    "detail": "테스트 프로그램 상세 설명입니다22"
}
```

### 8. 생태 관광지 중에 서비스 지역 컬럼에서 특정 지역에서 진행되는 프로그램명과 테마를 출력하는 API
> Method : GET
> URL : http://localhost:8080/greentour/search/region?regionName={지역명}

[입력]  
```zsh
% curl -H "Authorization: Bearer {xxx.yyy.zzz}" -H 'Content-Type: application/json' \
-X GET 'http://localhost:8080/greentour/search/region?regionName={지역명}'
```
[출력]  
```json
{
    "regionCd": {지역코드},
    "programList": [
        {
            "programName": {프로그램명},
            "theme": {프로그램 테마}
        },
        ...
    ]
}
```

### 9. 생태 정보 데이터에 "프로그램 소개” 컬럼에서 특정 문자열이 포함된 레코드에서 서비스 지역 개수를 세어 출력하는 API
> Method : GET
> URL : http://localhost:8080/greentour/search/outline?keyword={키워드}

[입력]  
```zsh
% curl -H "Authorization: Bearer {xxx.yyy.zzz}" -H 'Content-Type: application/json' \
-X GET 'http://localhost:8080/greentour/search/outline?keyword={키워드}'
```
[출력]  
```json
{
    "keyword": {키워드},
    "programList": [
        {
            "region": {주소},
            "count": {갯수}
        }
    ]
}
```

### 10. 모든 레코드에 프로그램 상세 정보를 읽어와서 입력 단어의 출현빈도수를 계산하여 출력 하는 API
> Method : GET
> URL : http://localhost:8080/greentour/search/detail?keyword={키워드}

[입력]  
```zsh
% curl -H "Authorization: Bearer {xxx.yyy.zzz}" -H 'Content-Type: application/json' \
-X GET 'http://localhost:8080/greentour/search/detail?keyword={키워드}'
```
[출력]  
```json
{
    "keyword": {키워드},
    "count": {출현빈도수}
}
```