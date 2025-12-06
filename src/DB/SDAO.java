package DB;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class SDAO {
    private SDAO() {
    }

    private static SDAO instance = new SDAO();

    // SDAO 객체 반환
    public static SDAO getInstance() {
        return instance;
    }

    // ----------------------------------------------------------------
    // 회원 관리: 로그인, 회원가입, 아이디 중복 검사
    // ----------------------------------------------------------------

    // 기능: 로그인
    // 매개변수: (유저ID, 비밀번호)
    // 반환값: User 객체 (실패 시 null)
    public User loginGetUser(String userId, String userPw) {
        User user = null;

        // 아이디와 비밀번호가 모두 일치하는 행을 찾음
        String sql = "SELECT * FROM users WHERE user_id = ? AND password = ?";

        try (Connection conn = DBC.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);
            pstmt.setString(2, userPw);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // 로그인 성공 User 객체 생성
                    user = new User();

                    user.setID(rs.getString("user_id"));
                    user.setPW(rs.getString("password"));
                    user.setName(rs.getString("name"));
                    user.setOrganization(rs.getString("organization"));

                    // DB의 Date 타입 -> 자바의 LocalDate 타입으로 변환
                    java.sql.Date dbDate = rs.getDate("birth_date");
                    if (dbDate != null) {
                        user.setBirthDate(dbDate.toLocalDate());
                    }

                    user.setPhoneNumber(rs.getString("phone"));
                    user.setEmail(rs.getString("email"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("로그인 중 DB 오류");
        }

        return user; // 실패하면 null 반환
    }

    // 기능: 회원가입
    // 매개변수: (User 객체)
    // 반환값: true(성공), false(실패)
    public boolean registerUser(User user) {
        String sql = "INSERT INTO users (user_id, password, name, organization, birth_date, phone, email) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBC.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getID());
            pstmt.setString(2, user.getPW());
            pstmt.setString(3, user.getName());
            pstmt.setString(4, user.getOrganization());

            // LocalDate -> java.sql.Date 변환
            if (user.getBirthDate() != null) {
                pstmt.setDate(5, java.sql.Date.valueOf(user.getBirthDate()));
            } else {
                pstmt.setDate(5, null);
            }

            pstmt.setString(6, user.getPhoneNumber());
            pstmt.setString(7, user.getEmail());

            int result = pstmt.executeUpdate();
            return result > 0; // 성공 시 true 반환

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("회원가입 실패 (DB 오류 or 중복 아이디)");
            return false;
        }
    }

    // 기능: 아이디 중복 검사
    // 매개변수: (유저ID)
    // 반환값: true(중복), false(중복 없음)
    public boolean checkIdDuplicate(String userId) {
        boolean isDuplicate = false;
        String sql = "SELECT 1 FROM users WHERE user_id = ?"; // 1만 가져와서 존재 여부만 확인

        try (Connection conn = DBC.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    isDuplicate = true; // 결과가 조회되면 이미 있는 아이디
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("아이디 중복 확인 중 오류");
        }

        return isDuplicate;
    }

    // ----------------------------------------------------------------
    // 유저 관리: 조회, 수정
    // ----------------------------------------------------------------

    // 기능: 유저 조회
    // 매개변수: 조회할 user_id
    // 반환값: User 객체 (없으면 null)
    public User getUserInfo(String userId) {
        User user = null;
        String sql = "SELECT * FROM users WHERE user_id = ?";

        try (Connection conn = DBC.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setID(rs.getString("user_id"));
                    user.setName(rs.getString("name"));
                    user.setOrganization(rs.getString("organization"));
                    user.setBirthDate(rs.getDate("birth_date").toLocalDate());
                    user.setPhoneNumber(rs.getString("phone"));
                    user.setEmail(rs.getString("email"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("유저 정보 조회 중 오류 발생");
        }

        return user;
    }

    // 기능: 유저 정보 수정
    // 매개변수: 수정할 정보가 담긴 User 객체 (ID는 WHERE 조건으로 사용)
    // 반환값: true(성공), false(실패)
    public boolean updateUserInfo(User user) {
        // user_id는 PK이므로 수정하지 않고 식별자로 사용합니다.
        // phone 컬럼명 주의 (DB는 phone, 자바 객체는 phoneNumber)
        String sql = "UPDATE users SET "
                + "password = ?, "
                + "name = ?, "
                + "organization = ?, "
                + "birth_date = ?, "
                + "phone = ?, "
                + "email = ? "
                + "WHERE user_id = ?";

        try (Connection conn = DBC.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 1. 비밀번호
            pstmt.setString(1, user.getPW());
            // 2. 이름
            pstmt.setString(2, user.getName());
            // 3. 소속
            pstmt.setString(3, user.getOrganization());

            // 4. 생년월일 (LocalDate -> java.sql.Date 변환)
            if (user.getBirthDate() != null) {
                pstmt.setDate(4, java.sql.Date.valueOf(user.getBirthDate()));
            } else {
                pstmt.setNull(4, java.sql.Types.DATE);
            }

            // 5. 전화번호
            pstmt.setString(5, user.getPhoneNumber());
            // 6. 이메일
            pstmt.setString(6, user.getEmail());

            // 7. WHERE 조건 (유저 아이디)
            pstmt.setString(7, user.getID());

            int result = pstmt.executeUpdate();

            // 1행 이상 수정되었다면 성공
            return result > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("회원 정보 수정 실패: " + e.getMessage());
            return false;
        }
    }

    // ----------------------------------------------------------------
    // 그룹 관리: 조회, 삽입, 초대코드 생성
    // ----------------------------------------------------------------

    // 기능: 그룹 조회
    // 매개변수: 그룹ID
    // 반환값: Group 객체
    public Group getGroupInfo(String groupId) {
        Group group = null;
        String sql = "SELECT group_id, group_name, invite_code "
                + "FROM user_groups "
                + "WHERE group_id = ?";

        try (Connection conn = DBC.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, groupId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("group_name");
                    String code = rs.getString("invite_code");
                    group = new Group(groupId, name, code);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return group;
    }

    // 기능: 소속 그룹 조회
    // 매개변수: (유저ID)
    // 반환값: Group 리스트
    public ArrayList<Group> getMyGroups(String userId) {
        ArrayList<Group> list = new ArrayList<>();

        String sql = "SELECT g.group_id, g.group_name, g.invite_code "
                + "FROM user_groups g "
                + "JOIN group_members m ON g.group_id = m.group_id "
                + "WHERE m.user_id = ? "
                + "ORDER BY g.group_id ASC";

        try (Connection conn = DBC.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String id = rs.getString("group_id");
                    String name = rs.getString("group_name");
                    String code = rs.getString("invite_code");

                    list.add(new Group(id, name, code));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("내 그룹 목록 조회 실패");
        }
        return list;
    }

    // 기능: 그룹 생성
    // 매개변수: 그룹 이름, 생성자 ID
    // 반환값: boolean(성공 true, 실패 false))
    public boolean createGroup(String groupId, String groupName, String writerId) {
        Connection conn = null;
        PreparedStatement pstmtGroup = null;
        PreparedStatement pstmtMember = null;
        ResultSet rs = null;
        boolean isSuccess = false;
        String inviteCode;

        try {
            conn = DBC.connect();

            // 초대 코드 생성
            inviteCode = generateInviteCode(conn);

            // 트랜잭션 시작 (두 개의 INSERT가 모두 성공해야 함)
            conn.setAutoCommit(false);

            // user_groups 테이블에 그룹 정보 INSERT
            String sqlGroup = "INSERT INTO user_groups (group_id, group_name, invite_code) VALUES (?, ?, ?)";
            pstmtGroup = conn.prepareStatement(sqlGroup);
            pstmtGroup.setString(1, groupId);
            pstmtGroup.setString(2, groupName);
            pstmtGroup.setString(3, inviteCode);
            int result1 = pstmtGroup.executeUpdate();

            // group_members 테이블에 작성자를 admin으로 추가
            String sqlMember = "INSERT INTO group_members (user_id, group_id, is_admin) VALUES (?, ?, 'Y')";
            pstmtMember = conn.prepareStatement(sqlMember);
            pstmtMember.setString(1, writerId);
            pstmtMember.setString(2, groupId);
            int result2 = pstmtMember.executeUpdate();

            // 두 단계 모두 성공했다면 커밋(저장)
            if (result1 > 0 && result2 > 0) {
                conn.commit();
                isSuccess = true;
                System.out.println("그룹 생성 완료! 코드: " + inviteCode);
            } else {
                conn.rollback(); // 하나라도 실패하면 취소
            }

        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (conn != null)
                    conn.rollback();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        } finally {
            // 자원 해제 (conn은 닫지 않고, 문맥에 따라 처리하거나 여기서 닫음)
            try {
                if (rs != null)
                    rs.close();
                if (pstmtGroup != null)
                    pstmtGroup.close();
                if (pstmtMember != null)
                    pstmtMember.close();
                if (conn != null)
                    conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return isSuccess;
    }

    // 기능: 6자리 랜덤 초대 코드 생섬
    // 매개변수: DB연결객체
    // 반환값: String(생성된 초대코드)
    private String generateInviteCode(Connection conn) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        java.util.Random random = new java.util.Random();
        String code;

        // 중복 체크
        while (true) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 6; i++) {
                sb.append(chars.charAt(random.nextInt(chars.length())));
            }
            code = sb.toString();

            String sql = "SELECT 1 FROM user_groups WHERE invite_code = ?";
            boolean isDuplicate = false;

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, code);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        isDuplicate = true; // 중복됨
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            if (!isDuplicate) {
                break;
            }
        }
        return code;
    }

    // ----------------------------------------------------------------
    // 그룹원 관리: 조회, 삽입, 갱신, 삭제
    // ----------------------------------------------------------------

    // 기능: 그룹원 조회
    // 매개변수: 그룹ID
    // 반환값: Member 리스트
    public ArrayList<Member> getMembersByGroupId(String groupId) {
        ArrayList<Member> list = new ArrayList<>();

        String sql = "SELECT m.user_id, m.group_id, m.is_admin, m.task, u.name "
                + "FROM group_members m "
                + "JOIN users u ON m.user_id = u.user_id "
                + "WHERE m.group_id = ? "
                + "ORDER BY m.is_admin DESC, u.name ASC";

        try (Connection conn = DBC.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, groupId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    // DB에서 값 꺼내기
                    String user_id = rs.getString("user_id");
                    String group_id = rs.getString("group_id");
                    String is_admin = rs.getString("is_admin");
                    String task = rs.getString("task");
                    String user_name = rs.getString("name"); // 이름

                    // null 처리 (업무가 아직 없을 경우)
                    if (task == null)
                        task = "할당 안됨";

                    // 리스트에 추가
                    list.add(new Member(user_id, group_id, is_admin, task, user_name));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("멤버 조회 오류: " + e.getMessage());
        }

        return list;
    }

    // 기능: 그룹 가입
    // 매개변수: 가입할 아이디, 초대코드
    // 반환값: 1 = 성공 || 0 = 초대코드 오류 || -1 = 이미 가입됨/DB오류
    public int joinGroup(String userId, String inviteCode) {
        Connection conn = null;
        PreparedStatement pstmtFind = null;
        PreparedStatement pstmtInsert = null;
        ResultSet rs = null;

        int resultStatus = 0;

        try {
            conn = DBC.connect();

            // 초대 코드로 그룹 ID 찾기
            String sqlFind = "SELECT group_id, group_name FROM user_groups WHERE invite_code = ?";
            pstmtFind = conn.prepareStatement(sqlFind);
            pstmtFind.setString(1, inviteCode);

            rs = pstmtFind.executeQuery();

            if (rs.next()) {
                // 그룹을 찾음
                String groupId = rs.getString("group_id");
                String groupName = rs.getString("group_name");

                // 해당 그룹에 멤버로 INSERT
                // 일반 멤버(N), 진척도 0으로 시작
                String sqlInsert = "INSERT INTO group_members (user_id, group_id, is_admin, task) VALUES (?, ?, 'N', NULL)";

                pstmtInsert = conn.prepareStatement(sqlInsert);
                pstmtInsert.setString(1, userId);
                pstmtInsert.setString(2, groupId);

                int row = pstmtInsert.executeUpdate();

                if (row > 0) {
                    System.out.println("['" + groupName + "'] 그룹 가입 성공!");
                    resultStatus = 1;
                }
            } else {
                System.out.println("존재하지 않는 초대 코드입니다.");
                resultStatus = 0;
            }

        } catch (SQLException e) {
            // MySQL 에러 코드 1062: Duplicate entry (이미 가입된 경우 PK 중복 발생)
            if (e.getErrorCode() == 1062) {
                System.out.println("이미 가입된 그룹입니다.");
                resultStatus = -1;
            } else {
                e.printStackTrace();
                resultStatus = -1; // 기타 DB 오류
            }
        } finally {
            // 자원 해제
            try {
                if (rs != null)
                    rs.close();
                if (pstmtFind != null)
                    pstmtFind.close();
                if (pstmtInsert != null)
                    pstmtInsert.close();
                if (conn != null)
                    conn.close();
            } catch (Exception e) {
            }
        }

        return resultStatus;
    }

    // 기능: 그룹 탈퇴
    // 매개변수: 탈퇴할 유저ID, 탈퇴할 그룹ID
    // 반환값: true(성공), false(실패)
    public boolean leaveGroup(String userId, String groupId) {
        String sql = "DELETE FROM group_members WHERE user_id = ? AND group_id = ?";

        try (Connection conn = DBC.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);
            pstmt.setString(2, groupId);

            int result = pstmt.executeUpdate();

            // 1행 이상 삭제되었다면 탈퇴 성공
            return result > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("그룹 탈퇴 중 DB 오류 발생: " + e.getMessage());
            return false;
        }
    }

    // 기능: 업무 갱신
    // 매개변수: 대상 멤버ID, 그룹 ID, 변경할 업무 내용
    // 반환값: boolean(성공 시 true, 실패 시 false)
    public boolean updateTask(String userId, String groupId, String task) {
        String sql = "UPDATE group_members SET task = ? WHERE user_id = ? AND group_id = ?";

        try (Connection conn = DBC.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, task);
            pstmt.setString(2, userId);
            pstmt.setString(3, groupId);

            int result = pstmt.executeUpdate();
            return result > 0; // 1개 이상 수정되면 성공

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("업무 할당 실패: " + e.getMessage());
            return false;
        }
    }

    // ----------------------------------------------------------------
    // 일정 관리: 조회, 삽입, 갱신, 삭제
    // ----------------------------------------------------------------

    // 기능: 유저 일정 조회
    // 매개변수: 유저ID
    // 반환값: Schedule 리스트
    public ArrayList<Schedule> getSchedules(String userId) {
        ArrayList<Schedule> list = new ArrayList<>();

        // 오늘 날짜에 포함되는 일정 조회
        String sql = "SELECT schedule_id, writer_id, group_id, schedule_description, schedule_type, start_at, end_at "
                + "FROM schedules "
                + "WHERE writer_id = ? "
                + "ORDER BY start_at ASC";
        // DB 연결
        try (Connection conn = DBC.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);

            // 받아온 일정을 객체로 만들어 저장
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("schedule_id");
                    String wId = rs.getString("writer_id");
                    String gid = rs.getString("group_id");
                    String description = rs.getString("schedule_description");
                    String type = rs.getString("schedule_type");
                    java.sql.Timestamp startTs = rs.getTimestamp("start_at");
                    java.sql.Timestamp endTs = rs.getTimestamp("end_at");

                    // Timestamp를 LocalDateTime으로 변환
                    java.time.LocalDateTime start = (startTs != null) ? startTs.toLocalDateTime() : null;
                    java.time.LocalDateTime end = (endTs != null) ? endTs.toLocalDateTime() : null;
    
                    list.add(new Schedule(id, wId, gid, description, type, start, end));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    
    // 기능: 특정 유저의 일정 중 지정된 그룹에 속하는 일정만 조회한다.
    // 매개변수: userId : 조회할 유저 ID, groupId : 조회 대상 그룹 ID
    // 반환값: 해당 유저가 작성한 일정 중 groupId에 해당하는 Schedule 리스트
    // 필요한 이유
    // getSchedules(userId)는 유저가 작성한 모든 일정을 반환하여
    // 그룹 구분 없이 전체 일정이 섞여 조회되는 문제가 발생한다.
    // 그룹 화면에서는 "해당 그룹에 속한 일정만" 정확히 보여줘야 하므로
    // userId + groupId 조건으로 필터링된 일정 조회 메소드가 필요하다.
    public ArrayList<Schedule> getSchedulesByUserAndGroup(String userId, String groupId) {
        ArrayList<Schedule> list = new ArrayList<>();

        String sql = "SELECT schedule_id, writer_id, group_id, schedule_description, schedule_type, start_at, end_at "
                + "FROM schedules "
                + "WHERE writer_id = ? "
                + "AND group_id = ? "
                + "ORDER BY start_at ASC";

        try (Connection conn = DBC.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);
            pstmt.setString(2, groupId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new Schedule(
                            rs.getInt("schedule_id"),
                            rs.getString("writer_id"),
                            rs.getString("group_id"),
                            rs.getString("schedule_description"),
                            rs.getString("schedule_type"),
                            rs.getTimestamp("start_at").toLocalDateTime(),
                            rs.getTimestamp("end_at").toLocalDateTime()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // 기능: 그룹 일정 조회
    // 매개변수: 유저ID
    // 반환값: Schedule 리스트
    public ArrayList<Schedule> getGroupSchedules(String groupId) {
        ArrayList<Schedule> list = new ArrayList<>();

        // 오늘 날짜에 포함되는 일정 조회
        String sql = "SELECT schedule_id, writer_id, group_id, schedule_description, schedule_type, start_at, end_at "
                + "FROM schedules "
                + "WHERE group_id = ? "
                + "ORDER BY start_at ASC";
        // DB 연결
        try (Connection conn = DBC.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, groupId);

            // 받아온 일정을 객체로 만들어 저장
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("schedule_id");
                    String wId = rs.getString("writer_id");
                    String gid = rs.getString("group_id");
                    String description = rs.getString("schedule_description");
                    String type = rs.getString("schedule_type");
                    java.sql.Timestamp startTs = rs.getTimestamp("start_at");
                    java.sql.Timestamp endTs = rs.getTimestamp("end_at");

                    // Timestamp를 LocalDateTime으로 변환
                    java.time.LocalDateTime start = (startTs != null) ? startTs.toLocalDateTime() : null;
                    java.time.LocalDateTime end = (endTs != null) ? endTs.toLocalDateTime() : null;

                    list.add(new Schedule(id, wId, gid, description, type, start, end));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 기능: 일정 추가
    // 매개변수: Schedule 객체
    // 반환값: true(성공), false(실패)
    public boolean insertSchedule(Schedule dto) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        // 개인 일정과 그룹 일정 모두 명시함
        String sql = "INSERT INTO schedules (writer_id, group_id, schedule_description, schedule_type, start_at, end_at) VALUES (?, ?, ?, ?, ?, ?)";

        boolean result = false;

        try {
            conn = DBC.connect();
            pstmt = conn.prepareStatement(sql);

            // 작성자 ID
            pstmt.setString(1, dto.getWriterId());

            // 그룹 ID 처리
            // Schedule 객체의 groupId가 null이면 -> 개인 일정(DB에 NULL 저장)
            // 0이 아니면 -> 그룹 일정(DB에 숫자 저장)
            if (dto.getGroupId() == null) {
                // 개인 일정일 때: DB에 'NULL'을 집어넣으라는 명령어
                pstmt.setNull(2, java.sql.Types.INTEGER);
            } else {
                // 그룹 일정일 때: 실제 그룹 번호(1, 2, 3...)를 집어넣음
                pstmt.setString(2, dto.getGroupId());
            }

            // 일정 제목
            pstmt.setString(3, dto.getScheduleDescription());

            // 일정 종류
            pstmt.setString(4, dto.getScheduleType());

            // 시작 시간, Timestamp로 변환
            if (dto.getStartAt() != null)
                pstmt.setTimestamp(5, java.sql.Timestamp.valueOf(dto.getStartAt()));
            else
                pstmt.setTimestamp(5, null);

            // 종료 시간, Timestamp로 변환
            if (dto.getEndAt() != null)
                pstmt.setTimestamp(6, java.sql.Timestamp.valueOf(dto.getEndAt()));
            else
                pstmt.setTimestamp(6, null);

            // 적용
            int count = pstmt.executeUpdate();
            if (count > 0) {
                result = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 자원 해제
            try {
                if (pstmt != null)
                    pstmt.close();
                DBC.close(); // 연결 끊기
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    // 기능: 일정 정보 수정
    // 매개변수: 수정된 내용이 담긴 Schedule 객체
    // 반환값: true(성공), false(실패)
    public boolean updateSchedule(Schedule dto) {
        boolean result = false;

        String sql = "UPDATE schedules SET "
                + "group_id = ?, "
                + "schedule_description = ?, "
                + "schedule_type = ?, "
                + "start_at = ?, "
                + "end_at = ? "
                + "WHERE schedule_id = ?";

        try (Connection conn = DBC.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (dto.getGroupId() == null) {
                pstmt.setNull(1, java.sql.Types.VARCHAR);
            } else {
                pstmt.setString(1, dto.getGroupId());
            }

            pstmt.setString(2, dto.getScheduleDescription());
            pstmt.setString(3, dto.getScheduleType());
            pstmt.setTimestamp(4, java.sql.Timestamp.valueOf(dto.getStartAt()));
            pstmt.setTimestamp(5, java.sql.Timestamp.valueOf(dto.getEndAt()));
            pstmt.setInt(6, dto.getScheduleId());

            int count = pstmt.executeUpdate();
            if (count > 0) {
                result = true;
                System.out.println("일정 수정 성공 (ID: " + dto.getScheduleId() + ")");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("일정 수정 실패");
        }

        return result;
    }

    // 기능: 일정 삭제
    // 매개변수: 삭제할 일정의 ID (schedule_id)
    // 반환값: boolean (성공 true, 실패 false)
    public boolean deleteSchedule(int scheduleId) {
        boolean result = false;

        String sql = "DELETE FROM schedules WHERE schedule_id = ?";

        try (Connection conn = DBC.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, scheduleId);

            int count = pstmt.executeUpdate();
            if (count > 0) {
                result = true;
                System.out.println("일정 삭제 성공 (ID: " + scheduleId + ")");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("일정 삭제 실패");
        }

        return result;
    }

    // ----------------------------------------------------------------
    // 메모 관리: 조회, 삽입
    // ----------------------------------------------------------------

    // 기능: 그룹 메모 조회
    // 매개변수: 그룹ID
    // 반환값: Memo 리스트
    public ArrayList<Memo> getMemosByGroupId(String groupId) {
        ArrayList<Memo> list = new ArrayList<>();

        // 해당 그룹의 메모 불러오기
        String sql = "SELECT memo_id, group_id, writer_id, content, created_at "
                + "FROM memos "
                + "WHERE group_id = ? "
                + "ORDER BY created_at DESC";

        // DB 연결
        try (Connection conn = DBC.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // SQL의 ? 부분 채우기
            pstmt.setString(1, groupId);

            // 실행 및 결과 받기
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    // DB에서 값 꺼내기
                    int mId = rs.getInt("memo_id");
                    String gId = rs.getString("group_id");
                    String wId = rs.getString("writer_id");
                    String content = rs.getString("content");

                    Timestamp date_ts = rs.getTimestamp("created_at"); // 포맷팅된 날짜 문자열
                    LocalDateTime date_ldt = date_ts.toLocalDateTime();

                    // 리스트에 추가
                    list.add(new Memo(mId, gId, wId, content, date_ldt));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("메모 조회 중 오류 발생");
        }

        return list;
    }

    // 기능: 메모 추가
    // 매개변수: Memo 객체 (group_id, writer_id, content 필요)
    // 반환값: true(성공), false(실패)
    public boolean insertMemo(Memo memo) {
        String sql = "INSERT INTO memos (group_id, writer_id, content, created_at) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBC.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, memo.getGroupId());
            pstmt.setString(2, memo.getWriterId());
            pstmt.setString(3, memo.getContent());
            pstmt.setTimestamp(4, java.sql.Timestamp.valueOf(memo.getCreatedAt()));

            int result = pstmt.executeUpdate();

            // 1행 이상 삽입되면 성공
            return result > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("메모 삽입 중 DB 오류 발생: " + e.getMessage());
            return false;
        }
    }

}