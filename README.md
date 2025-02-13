# WEB2_3_ERoom_BE
### 1. 커밋 유형 지정

- 커밋 유형은 영어 소문자로 작성하기
    
    
    | 커밋 유형 | 의미 |
    | --- | --- |
    | `feat` | 새로운 기능 추가 |
    | `fix` | 버그 수정 |
    | `docs` | 문서 수정 |
    | `style` | 코드 formatting, 세미콜론 누락, 코드 자체의 변경이 없는 경우 |
    | `refactor` | 코드 리팩토링 |
    | `test` | 테스트 코드, 리팩토링 테스트 코드 추가 |
    | `chore` | 패키지 매니저 수정, 그 외 기타 수정 ex) .gitignore |
    | `design` | CSS 등 사용자 UI 디자인 변경 |
    | `comment` | 필요한 주석 추가 및 변경 |
    | `rename` | 파일 또는 폴더 명을 수정하거나 옮기는 작업만인 경우 |
    | `remove` | 파일을 삭제하는 작업만 수행한 경우 |
    | `!BREAKING CHANGE` | 커다란 API 변경의 경우 |
    | `!HOTFIX` | 급하게 치명적인 버그를 고쳐야 하는 경우 |

### 2. 제목과 본문을 빈행으로 분리

- 커밋 유형 이후 제목과 본문은 한글로 작성하여 내용이 잘 전달될 수 있도록 할 것
- 본문에는 변경한 내용과 이유 설명 (어떻게보다는 무엇 & 왜를 설명)

### 3. 제목 첫 글자는 소문자로, 끝에는 `.` 금지

### 4. 제목은 영문 기준 50자 이내로 할 것

### 5. 자신의 코드가 직관적으로 바로 파악할 수 있다고 생각하지 말자

### 6. 여러가지 항목이 있다면 글머리 기호를 통해 가독성 높이기

### 7. 예시 "update: suggestion폴더 생성", "fix: 일정 관리 동시성 문제 해결", 등등

### 8. git-flow를 사용한다, Merge는 dev branch에 하고 branch는 기능별로 생성한다
[git-flow 참고](https://techblog.woowahan.com/2553/)

### 9. 추가한 dependencies
- Spring Boot DevTools
- Lombok
- Spring Web
- Thymeleaf
- Spring Data JPA
- MariaDB Driver
- Spring Data Redis(Access+Driver)
