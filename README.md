# springboot_assignment

Branch 
 * main (API + Exception)
 * Login (Login) 

## 1) 음악 재생 목록 관리 API - 사용자가 자신의 음악 재생 목록을 만들고 관리할 수 있는 API

- CRUD(등록, 조회(페이징 되어야 함.), 수정, 삭제 기능이 각 1개씩 반드시 포함)를 위한 간단한 REST API 설계 및 구현

    - **페이징 시나리오**: (앱에서 실행하므로 화면에보여지는 데이터는 최대 5개)  5개 이상의 음악이 있을시 페이징 적용
    * 음악은 SQL 로 추가
        ```sql
        INSERT INTO `music` (`artist`, `title`, `play_time`) VALUES
            ('세븐틴', '음악의신',205),
            ('RIIZE','Talk Saxy',191),
            ('츄','Howl',170),
            ('투모로우바이투게더','Chasing That Feeling',183),
            ('아이브','Baddie',154);
        ```


![image](https://github.com/SangWoon123/springboot_assignment/assets/100204926/7c05b299-c28e-49f0-b30c-6074cc9e71db)

#### 음악 REST API 테스트 코드 작성 (CRUD)

1. 플레이리스트 생성후 음악 저장 테스트 (C,R)
2. 플레이리스트 이름 변경 테스트 (U)
3. 플레이리스트에 들어있는 음악 삭제 테스트 (D)
4. 플레이리스트 삭제 테스트 (D)
5. 플레이리스트 생성 오류 테스트
### ERD 설계
참고) 애플뮤직 음악 플레이리스트 

<img width="503" alt="애플뮤직 참고" src="https://github.com/SangWoon123/springboot_assignment/assets/100204926/c6c0b5d4-cb29-48db-95ad-b83155331b71">


### 도메인
![image](https://github.com/SangWoon123/springboot_assignment/assets/100204926/d500ab23-860e-438e-aca5-e89f3427d757)

- 음악 ( id / 노래 제목 / 가수 / 장르 )
- 플레이리스트 ( id / 플레이리스트 이름 )
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
4. 플레이리스트의 이름이 빈값일 때

enum타입 작성

```java

@Getter
public enum ErrorCode {

    PLAYLIST_NAME_NOT_FOUND(404,"플레이리스트 이름을 입력해주세요"),
    MUSIC_NOT_FOUND(404, "해당음악이 존재하지 않습니다."),
    MUSIC_NOT_FOUND_FOR_DELETE(404, "삭제하려는 음악이 존재하지 않습니다."),
    PLAYLIST_NOT_FOUND(404, "플레이리스트가 존재하지 않습니다.");

    ...
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
