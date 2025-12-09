# 2025-2학기 컴퓨터공학부 2학년 JAVA응용프로젝트 12분반 8조

## "개인과 팀의 업무 효율을 높이는 일정 및 협업 관리 프로그램 개발"

### 팀 구성
- 문석용 2022243016
- 원세찬 2022243050
- 윤홍규 2022243008
- 전세호 2022243034
---------------------------------

## 1. 프로그램 주제 및 목표

### 주제
- 개인과 팀의 업무 효율을 높이는 일정 및 협업 관리

### 개발 목적
- 팀 프로젝트 상황에서, 팀원 간의 일정 조율과 업무 분담이 제대로 되지 않아 불필요한 시간 낭비가 발생하는 것을 자주 경험하였습니다.
- 특히 개인의 학업 일정과 팀 프로젝트의 일정이 뒤섞여 관리가 어렵다는 점에서 개인의 스케줄 관리와 그룹의 협업 관리를 하나의 프로그램에서    직관적으로 해결해보자는 목표로 본 주제를 선정하게 되었습니다.
---------------------------------

## 2. 프로그램 설치 가이드

### 2.1 필수 설치 사항

#### 데이터베이스
- **MySQL Server** 설치 및 실행 중이어야 합니다.

#### 개발 도구
- Eclipse
- Visual Studio Code
- Mysql Workbench

#### 외부 라이브러리 및 프로그램 설치
| 구분 | 용도 | 링크 |
|-----------|------|--------------|
| **MySQL Connector/J** | JDBC 드라이버 (필수) | [다운로드](https://dev.mysql.com/downloads/connector/j/) |
| **MySQL Mysql Workbench** | Mysql Workbench | [다운로드](https://dev.mysql.com/downloads/workbench/) |

### 2.2 데이터베이스 설정
#### 2.2.1 Mysql Workbench 프로그램 활용하여 데이터베이스 생성
- root 계정으로 로그인해 **ScheduleManagement** 데이터베이스를 생성

#### 2.2.2 테이블 생성
- **파일 1 :** `src/DB/tables.sql`을 횔용
- **테스트용 데이터 :** `src/DB/testData.sql`을 활용

#### 2.2.3 Java 코드에 MySQL 연결
- 프로젝트에서 데이터베이스 연결을 위해 다음과 같이 설정해야 합니다.

**파일 2 :** `src/DB/DBC.java`

```java
public class DBC {
    public static final String databaseDriver = "com.mysql.cj.jdbc.Driver";
    public static final String databaseUrl = "jdbc:mysql://localhost:3306/ScheduleManagement?       serverTimezone=Asia/Seoul&characterEncoding=UTF8&useSSL=false";
    public static final String databaseUser = "root";
    public static final String databasePassword = "(본인 My SQL 비밀번호)";
    
    ...
}
```
---------------------------------
## 3. 프로그램 실행 가이드

### 3.1 메인 프로그램 실행

#### 3.1.1 Eclipse에서 실행
1. `src/GUI/ScheduleInformation.java` 파일 열기
2. 파일 우클릭 -> **Run As** -> **Java Application**
3. 메인 메뉴 창이 실행됩니다

#### 3.1.2 Visual Studio Code에서 실행
1. **Visual Studio Code**확장에서 **Java Extension Pack**가 설치가 되어 있어야 합니다.
2. `src/GUI/ScheduleInformation.java` 파일 열기
3. 코드를 작성하면 main method 위에 **Run | Debug** 버튼이 생깁니다.
4. Run 또는 Debug 버튼 클릭하여 실행 할 수 있습니다.
---------------------------------

### 4. 코드 문의처

###### GUI 및 기능
- ?

###### BD
- ?