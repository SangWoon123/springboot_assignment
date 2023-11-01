# springboot_assignment
gdsc 활동

### 주제: 음악 재생 목록 관리 API - 사용자가 자신의 음악 재생 목록을 만들고 관리할 수 있는 API

- CRUD(등록, 조회(페이징 되어야 함.), 수정, 삭제 기능이 각 1개씩 반드시 포함)를 위한 간단한 REST API 설계 및 구현
    - **페이징 시나리오**: (앱에서 실행하므로 화면에보여지는 데이터는 최대 5개)  5개 이상의 음악이 있을시 페이징 적용
### ERD 설계
참고) 애플뮤직 음악 플레이리스트 
<img width="703" alt="스크린샷 2023-11-01 오전 1 28 38" src="https://github.com/SangWoon123/springboot_assignment/assets/100204926/5b60d4d1-5ba0-4023-8af8-257254fa5999">

### 도메인
 <img width="649" alt="음악 ERD" src="https://github.com/SangWoon123/springboot_assignment/assets/100204926/44ad4b7b-7a35-4202-a3ba-0d8ce184ee31">

- 음악 ( id / 노래 제목 / 가수 / 장르 )
- 플레이리스트 ( 음악리스트 )
- 연관관계: One-To-Many (플레이리스트 - 음악) / 양방향 설정

    

