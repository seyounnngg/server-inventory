# server-inventory

인프라/클라우드 엔지니어를 목표로 하며, 회사에서 쓰는 CMDB를 축소해서
만들어본 서버 인벤토리 관리 프로그램입니다.

여러 팀원이 나눠 담당하는 서버 자산을 등록하고, 점검 이력을 기록/추적하여 방치된 서버를 찾아낼 수 있는
"감사 도구"를 목표로 설계했습니다. 실시간 자동 모니터링 대신, 필요할 때 수동으로 트리거하는
방식을 택해 CRUD 학습이라는 프로젝트 본래 목적에 집중했습니다.

## 목차

- 주요 기능
- 사용 기술
- 프로젝트 구조
- 실행 방법
- DB 테이블 구조
- 실행 예시
- Git Branch 전략
- 왜 인터페이스 기반으로 설계했는가
- 왜 로그인 기능을 추가했는가
- 개발 중 어려웠던 점과 해결 방법
- 향후 개선 방향

## 주요 기능

**인증**

- 회원가입 / 로그인 (아이디·비밀번호 기반)

**계정 관리 (본인 계정만 대상)**

- 내 정보 수정
- 회원 탈퇴

**서버 관리**

- 서버 등록 / 목록 조회 / 내 담당 서버 자동 조회 / 수정 / 삭제
- 담당자(User)와 서버(Server)를 1:N 관계로 연결

**점검 관리**

- 실제 IP에 핑(ping)을 쏴서 서버 상태(UP/DOWN)를 확인하고 기록
- 전체 서버 일괄 점검
- 서버별 점검 이력 조회
- 현재 DOWN 상태인 서버만 모아서 조회

## 사용 기술

- Language: Java 21
- Build Tool: Maven
- Database: MariaDB (Docker)
- JDBC Driver: mariadb-java-client 3.5.7
- Container: Docker, Docker Compose
- IDE: IntelliJ IDEA

## 프로젝트 구조

    server-inventory
    ├── pom.xml
    ├── docker-compose.yml
    ├── schema.sql
    └── src
        └── main
            ├── java
            │   └── com.example.serverinventory
            │       ├── Main.java
            │       ├── model
            │       │   ├── User.java (id, name, team, username, password)
            │       │   ├── Server.java
            │       │   └── ServerCheck.java
            │       ├── repository
            │       │   ├── UserRepository.java / ServerRepository.java / ServerCheckRepository.java  (interface)
            │       │   ├── User/Server/ServerCheckMemoryRepository.java  (메모리 기반 구현체)
            │       │   ├── User/Server/ServerCheckFileRepository.java   (파일 기반 구현체)
            │       │   └── User/Server/ServerCheckDbRepository.java     (DB 기반 구현체)
            │       ├── service
            │       │   └── UserService.java / ServerService.java / ServerCheckService.java
            │       ├── util
            │       │   └── DBConnection.java
            │       └── view
            │           └── ConsoleView.java
            └── resources
                └── db.properties

## 실행 방법

1. 저장소 클론 및 db.properties 설정

       cp src/main/resources/db.properties.example src/main/resources/db.properties

2. Docker로 MariaDB 실행

       docker compose up -d

3. 스키마 적용

       docker exec -i java-crud-mariadb mariadb -u user -ppassword crud_db < schema.sql

4. Repository 버전 선택 (Main.java)

       // 메모리 버전
       UserRepository userRepository = new UserMemoryRepository();

       // 파일 버전
       UserRepository userRepository = new UserFileRepository();

       // DB 버전 (기본값)
       UserRepository userRepository = new UserDbRepository();

5. 실행

   IntelliJ에서 Main.java 실행, 또는:

       mvn compile exec:java -Dexec.mainClass="com.example.serverinventory.Main"

   최초 실행 시 화면에서 "2. 회원가입"으로 계정을 먼저 만든 뒤, "1. 로그인"으로 들어가야 합니다.

## DB 테이블 구조

    CREATE TABLE user (
        id       BIGINT AUTO_INCREMENT PRIMARY KEY,
        name     VARCHAR(50) NOT NULL,
        team     VARCHAR(50),
        username VARCHAR(50) NOT NULL UNIQUE,
        password VARCHAR(100) NOT NULL
    );

    CREATE TABLE server (
        id          BIGINT AUTO_INCREMENT PRIMARY KEY,
        hostname    VARCHAR(100) NOT NULL,
        ip_address  VARCHAR(50),
        type        VARCHAR(20),
        owner_id    BIGINT,
        FOREIGN KEY (owner_id) REFERENCES user(id)
    );

    CREATE TABLE server_check (
        id         BIGINT AUTO_INCREMENT PRIMARY KEY,
        server_id  BIGINT NOT NULL,
        checked_at DATETIME NOT NULL,
        status     VARCHAR(10),
        FOREIGN KEY (server_id) REFERENCES server(id)
    );

user 1:N server 1:N server_check 구조로, 마스터 데이터(User)와 사용자별 데이터(Server), 이력 데이터(ServerCheck)를
레이어로 분리했습니다.

## 실행 예시

(여기에 콘솔 실행 화면 캡처를 넣어주세요 — 회원가입/로그인, 서버 등록, 점검 결과, 내 담당 서버 조회, DOWN 서버 조회 등)

## Git Branch 전략

이번 프로젝트는 학습 초기 단계에 기능 구현 흐름에 집중하기 위해 별도의 feature 브랜치 없이
main 브랜치에서 기능 단위로 커밋하며 진행했습니다. (예: "feat: UserService 구현", "feat: 로그인 기능 구현" 등)
향후 프로젝트에서는 develop/feature 브랜치 전략을 적용해볼 계획입니다.

## 왜 인터페이스 기반으로 설계했는가

Repository를 인터페이스로 먼저 정의하고, Memory/File/Db 세 가지 구현체를 만들었습니다.
Service는 구체 클래스가 아닌 인터페이스 타입에만 의존하도록 작성했기 때문에,
Main.java에서 구현체를 교체하는 한 줄만으로 저장 방식(메모리 → 파일 → DB)이 전환되며,
Service와 View 코드는 전혀 수정하지 않았습니다. 이는 개방-폐쇄 원칙(OCP)을 실습해본 부분이며,
Spring의 @Repository + JpaRepository, @Autowired 의존성 주입이 내부적으로 해결해주는 문제를
직접 손으로 구현해보며 이해하고자 한 시도였습니다.

## 왜 로그인 기능을 추가했는가

처음 설계 단계에서 User를 Server, ServerCheck와 별도 테이블로 분리한 이유가
"여러 사람이 나눠 쓰는 서비스"라는 목적 때문이었는데, 초기 버전에서는 담당자 ID를 매번 사람이
직접 입력해야 해서 이 설계 의도가 실제로 체감되지 않는 한계가 있었습니다.

로그인 기능을 추가해 "로그인한 사람 = 현재 세션의 담당자"로 자동 연결되도록 하면서,
findByOwnerId 조회가 실제로 의미 있게 쓰이는 시나리오(내 담당 서버 자동 조회)를 완성했습니다.

비밀번호 해싱, 세션 관리 등은 Spring Security의 영역이라 판단해 이번 프로젝트 범위에서는
평문 비교 수준으로 단순화했습니다.

## 개발 중 어려웠던 점과 해결 방법

- Java 개발 경험이 거의 없는 상태로 시작해, 필드 선언·생성자 오버로딩 같은 기초 문법부터 익혀야 했습니다.
- Repository를 인터페이스로 설계하는 이유(다형성, DI)를 개념적으로 이해하는 데 시간이 걸렸으나,
  UserRepository repository = new UserMemoryRepository(); 처럼 선언 타입과 실제 타입을 분리해보며
  런타임에 실제 구현체가 결정된다는 원리를 체득했습니다.
- JDBC(PreparedStatement, ResultSet)는 처음 다뤄보는 개념이라,
  기존 Memory 버전의 로직을 재사용하며 "저장 방식만 바뀐다"는 감각으로 접근해 이해도를 높였습니다.
- User-Server-ServerCheck 관계 설계 시, 마스터 데이터와 사용자별 데이터를 분리해야 한다는 피드백을
  반영해 1:N 관계로 재설계했습니다.
- 로그인 기능을 뒤늦게 추가하면서, User 생성자 시그니처 변경이 Repository 3개 구현체(Memory/File/Db)에
  전부 영향을 미치는 것을 경험했습니다. 이를 통해 모델 변경이 계층 전체에 파급되는 범위를 체감했습니다.
- 로그인 실패나 계정 탈퇴 이후의 화면 흐름을 어떻게 되돌릴지 고민하며, 인증 루프와 메인 메뉴 루프를
  분리하여 상태를 관리하는 구조로 다듬었습니다.

## 향후 개선 방향

- Server.type, ServerCheck.status를 Enum으로 리팩토링 (현재는 String)
- 비밀번호 해싱(BCrypt 등) 적용
- CSV 리포트 내보내기 기능
- 자동 스케줄러 기반 모니터링 (현재는 의도적으로 수동 트리거 방식 채택)