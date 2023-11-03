# springboot_assignment

Branch 
 * main (API + Exception)
 * Login (Login) 

## 1) 음악 재생 목록 관리 API - 사용자가 자신의 음악 재생 목록을 만들고 관리할 수 있는 API

- CRUD(등록, 조회(페이징 되어야 함.), 수정, 삭제 기능이 각 1개씩 반드시 포함)를 위한 간단한 REST API 설계 및 구현
    - **페이징 시나리오**: (앱에서 실행하므로 화면에보여지는 데이터는 최대 5개)  5개 이상의 음악이 있을시 페이징 적용
    * 음악은 따로 X (SQL 로 추가)
### ERD 설계
참고) 애플뮤직 음악 플레이리스트 

<img width="403" alt="스크린샷 2023-11-01 오전 1 28 38" src="https://github.com/SangWoon123/springboot_assignment/assets/100204926/5b60d4d1-5ba0-4023-8af8-257254fa5999">

### 도메인
 <img width="649" alt="음악 ERD" src="https://github.com/SangWoon123/springboot_assignment/assets/100204926/44ad4b7b-7a35-4202-a3ba-0d8ce184ee31">

- 음악 ( id / 노래 제목 / 가수 / 장르 )
- 플레이리스트 ( 음악리스트 )
- 연관관계: One-To-Many (플레이리스트 - 음악) / 양방향 설정

    
## 2) JWT 로그인 구현 (Security 없이)

구현방식 2가지

- **필터적용 O / filter ( jwt 필터 → 인증 필터) → 컨트롤러(로그인)**
- 필터적용 X

### 순서

1. jwt필터
    * 클라이언트 요청에서 jwt토큰을 분리하여 검증하는 필터
2. 인증필터

## 3) Global Exeption 분리

음악 API Global exception 적용목록

1. 음악 번호가 없을때 
2. 플레이리스트가 존재하지않을 때 
3. 삭제하려는 음악이 없을때

enum타입 작성

```java
@Getter
public enum ErrorCode {
    MUSIC_NOT_FOUND(404, "해당음악이 존재하지 않습니다."),
    MUSIC_NOT_FOUND_FOR_DELETE(404, "삭제하려는 음악이 존재하지 않습니다."),
    PLAYLIST_NOT_FOUND(404, "플레이리스트가 존재하지 않습니다.");

    private int status;
    private String message;
}
```

ExceptionHandler

```java
@RestControllerAdvice
@Slf4j
public class ExceptionController {
    @ExceptionHandler({BusinessLogicException.class})
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessLogicException e){
        ErrorCode errorCode = e.getErrorCode();

        // ErrorResponse는 에러 응답을 나타내는 dto클래스
        ErrorResponse errorResponse = new ErrorResponse(errorCode.getStatus(), errorCode.getMessage());

        log.error("handleBusinessException: "+errorCode.getMessage());

        return new ResponseEntity(errorResponse,HttpStatus.valueOf(errorCode.getStatus()));
    }

}
```
