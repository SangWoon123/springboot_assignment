# springboot_assignment
gdsc 활동

### 주제: 음악 재생 목록 관리 API - 사용자가 자신의 음악 재생 목록을 만들고 관리할 수 있는 API
## ERD 설계

- 음악 ( id / 노래 제목 / 가수 / 장르 )
- 플레이리스트 ( 음악리스트 )
- 연관관계: **전제조건은 '나만의 플레이리스트'이기때문에 플레이리스트 하나는 여러음악을 가질수있고, 하나의 음악은 하나의플레이리스트에 속할수있다 로 봐야할거같다.**
- 단방향 : 플레이리스트에서 음악을 관리 

<img width="649" alt="음악 ERD" src="https://github.com/SangWoon123/springboot_assignment/assets/100204926/44ad4b7b-7a35-4202-a3ba-0d8ce184ee31">
