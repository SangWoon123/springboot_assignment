# springboot_assignment

Branch 
 * main (Login + API + Exception)

## 1) 음악 재생 목록 관리 API 

![image](https://github.com/SangWoon123/springboot_assignment/assets/100204926/7c05b299-c28e-49f0-b30c-6074cc9e71db)

CRUD를 위한 간단한 REST API 설계 및 구현

- 페이징 시나리오: 5개 이상의 음악이 있을시 페이징 적용
* 음악은 SQL 문을 활용
    ```sql
    INSERT INTO `music` (`artist`, `title`, `play_time`) VALUES
        ('세븐틴', '음악의신',205),
        ('RIIZE','Talk Saxy',191),
        ('츄','Howl',170),
        ('투모로우바이투게더','Chasing That Feeling',183),
        ('아이브','Baddie',154);
    ```




#### 음악 REST API 테스트 코드 작성 (CRUD)

1. 플레이리스트 생성후 음악 저장 테스트 (C,R)
2. 플레이리스트 이름 변경 테스트 (U)
3. 플레이리스트에 들어있는 음악 삭제 테스트 (D)
4. 플레이리스트 삭제 테스트 (D)
5. 플레이리스트 생성 오류 테스트

### ERD 설계

### 도메인
![image](https://github.com/SangWoon123/springboot_assignment/assets/100204926/d500ab23-860e-438e-aca5-e89f3427d757)

- 음악 ( id / 노래 제목 / 가수 / 장르 )
- 플레이리스트 ( id / 플레이리스트 이름 )
- 연관관계: One-To-Many (플레이리스트 - 음악) / 양방향 설정

    
## 2) JWT 로그인 구현 (Security 없이)


**⭐️[프로세스]⭐️**

1) 최초 로그인시

    회원가입 → 로그인 → accessToken/refreshToken 생성 → refreshToken 서버DB저장 → accessToken/refreshToken 반환 → refreshToken 쿠키 저장 및 accessToken 헤더 저장

2) accessToken 만료시

    클라이언트 쿠키에 저장되어있는 refreshToken을 서버로 전달 → 서버에서 유효성을 검증 → 새로운 accessToken을 생성 → 리턴

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
