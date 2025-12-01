package DB;

import java.sql.*;
import java.util.ArrayList;

public class SDAO {
        // 오늘의 일정을 리스트로 리턴
        public ArrayList<Schedule> getTodaySchedules(String userId) {
                ArrayList<Schedule> list = new ArrayList<>();

                // 오늘 날짜에 포함되는 일정 조회
                String sql = "SELECT schedule_id, writer_id, group_id, schedule_name, "
                                + "DATE_FORMAT(start_at, '%Y-%m-%d %H:%i') as start_str, "
                                + "DATE_FORMAT(end_at, '%Y-%m-%d %H:%i') as end_str "
                                + "FROM schedules " 
                                + "WHERE writer_id = ? "
                                + "AND start_at <= CONCAT(CURDATE(), ' 23:59:59') "
                                + "AND end_at >= CONCAT(CURDATE(), ' 00:00:00') "
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
                                        int gid = rs.getInt("group_id");
                                        String name = rs.getString("schedule_name");
                                        String start = rs.getString("start_str");
                                        String end = rs.getString("end_str");

                                        list.add(new Schedule(id, wId, gid, name, start, end));
                                }
                        }
                } catch (Exception e) {
                        e.printStackTrace();
                }
                return list;
        }

        // 마감 일정를 리스트로 리턴
        public ArrayList<Schedule> getDeadlineSchedules(String userId) {
                ArrayList<Schedule> list = new ArrayList<>();

                String sql = "SELECT schedule_id, writer_id, group_id, schedule_name, "
                                + "DATE_FORMAT(start_at, '%Y-%m-%d %H:%i') as start_str, "
                                + "DATE_FORMAT(end_at, '%Y-%m-%d %H:%i') as end_str "
                                + "FROM schedules "
                                + "WHERE writer_id = ? "
                                + "AND end_at BETWEEN NOW() AND DATE_ADD(NOW(), INTERVAL 2 DAY) "
                                + "ORDER BY end_at ASC";

                try (Connection conn = DBC.connect(); // DB 연결
                                PreparedStatement pstmt = conn.prepareStatement(sql)) {

                        pstmt.setString(1, userId);

                        // 받아온 일정을 객체로 만들어 저장
                        try (ResultSet rs = pstmt.executeQuery()) {
                                while (rs.next()) {
                                        int id = rs.getInt("schedule_id");
                                        String wId = rs.getString("writer_id");
                                        int gid = rs.getInt("group_id");
                                        String name = rs.getString("schedule_name");
                                        String start = rs.getString("start_str");
                                        String end = rs.getString("end_str");

                                        list.add(new Schedule(id, wId, gid, name, start, end));
                                }
                        }
                } catch (Exception e) {
                        e.printStackTrace();
                }

                return list;
        }

        // 일정 추가
        public boolean insertSchedule(Schedule dto) {
           Connection conn = null;
           PreparedStatement pstmt = null;
           
           //개인 일정과 그룹 일정 모두 명시함
           String sql = "INSERT INTO schedules (writer_id, group_id, schedule_name, start_at, end_at) VALUES (?, ?, ?, ?, ?)";
           
           boolean result = false;
           
           try {
              conn = DBC.connect();
              pstmt = conn.prepareStatement(sql);
              
              //작성자 ID
              pstmt.setString(1, dto.getWriterId());
              
              // 그룹 ID 처리
                // Schedule 객체의 groupId가 0이면 -> 개인 일정(DB에 NULL 저장)
                // 0이 아니면 -> 그룹 일정(DB에 숫자 저장)
                if (dto.getGroupId() == 0) {
                    // 개인 일정일 때: DB에 'NULL'을 집어넣으라는 명령어
                    pstmt.setNull(2, java.sql.Types.INTEGER); 
                } else {
                    // 그룹 일정일 때: 실제 그룹 번호(1, 2, 3...)를 집어넣음
                    pstmt.setInt(2, dto.getGroupId());
                }
                
                // 일정 제목
                pstmt.setString(3, dto.getScheduleName());
                
                // 시작 시간 
                pstmt.setString(4, dto.getStartAt());
                
                // 종료 시간
                pstmt.setString(5, dto.getEndAt());
                
                // 실행
                int count = pstmt.executeUpdate();
                if (count > 0) {
                   result = true;
                }
                
           }catch(Exception e) {
              e.printStackTrace();
           }finally {
              // 자원 해제
              try {
                 if(pstmt != null) pstmt.close();
                 DBC.close();    // 연결 끊기   
              }catch(Exception e) {
                 e.printStackTrace();
              }
           }
           return result;
        }
        
        // 특정 유저가 가입한 그룹 불러오기
        public ArrayList<Group> getMyGroups(String userId) {
                ArrayList<Group> list = new ArrayList<>();

                String sql = "SELECT g.group_id, g.group_name, g.invite_code "
                                + "FROM user_groups g "
                                + "JOIN Member m ON g.group_id = m.group_id "
                                + "WHERE m.user_id = ? "
                                + "ORDER BY g.group_id ASC";

                try (Connection conn = DBC.connect();
                                PreparedStatement pstmt = conn.prepareStatement(sql)) {

                        pstmt.setString(1, userId);

                        try (ResultSet rs = pstmt.executeQuery()) {
                                while (rs.next()) {
                                        int id = rs.getInt("group_id");
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

        // 그룹 ID로 그룹 불러오기
        public Group getGroupInfo(int groupId) {
                Group group = null;
                String sql = "SELECT group_id, group_name, invite_code "
                                + "FROM user_groups "
                                + "WHERE group_id = ?";

                try (Connection conn = DBC.connect();
                                PreparedStatement pstmt = conn.prepareStatement(sql)) {

                        pstmt.setInt(1, groupId);

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

        // 초대 코드로 그룹 불러오기 (그룹 가입)
        public Group getGroupByInviteCode(String code) {
                Group group = null;
                String sql = "SELECT group_id, group_name, invite_code "
                                + "FROM user_groups "
                                + "WHERE invite_code = ?";

                try (Connection conn = DBC.connect();
                                PreparedStatement pstmt = conn.prepareStatement(sql)) {

                        pstmt.setString(1, code);

                        try (ResultSet rs = pstmt.executeQuery()) {
                                if (rs.next()) {
                                        int id = rs.getInt("group_id");
                                        String name = rs.getString("group_name");
                                        group = new Group(id, name, code);
                                }
                        }
                } catch (Exception e) {
                        e.printStackTrace();
                }
                return group;
        }

        // 그룹원 가져오기
        public ArrayList<Member> getMembersByGroupId(int groupId) {
                ArrayList<Member> list = new ArrayList<>();

                String sql = "SELECT m.user_id, m.group_id, m.is_admin, m.task, m.progress, "
                                + "DATE_FORMAT(m.deadline, '%Y-%m-%d') as date_str, "
                                + "u.name " // User 테이블에서 이름 가져오기
                                + "FROM group_members m "
                                + "JOIN users u ON m.user_id = u.user_id "
                                + "WHERE m.group_id = ? "
                                + "ORDER BY m.is_admin DESC, u.name ASC";

                try (Connection conn = DBC.connect();
                                PreparedStatement pstmt = conn.prepareStatement(sql)) {

                        pstmt.setInt(1, groupId);

                        try (ResultSet rs = pstmt.executeQuery()) {
                                while (rs.next()) {
                                        // DB에서 값 꺼내기
                                        String user_id = rs.getString("user_id");
                                        int group_id = rs.getInt("group_id");
                                        String is_admin = rs.getString("is_admin");
                                        String task = rs.getString("task");
                                        int progress = rs.getInt("progress");
                                        String deadline = rs.getString("date_str");
                                        String user_name = rs.getString("name"); // 이름

                                        // null 처리 (업무가 아직 없을 경우)
                                        if (task == null)
                                                task = "할당 안됨";
                                        if (deadline == null)
                                                deadline = "-";

                                        // 리스트에 추가
                                        list.add(new Member(user_id, group_id, is_admin, task, progress, deadline,
                                                        user_name));
                                }
                        }
                } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("멤버 조회 오류: " + e.getMessage());
                }

                return list;
        }

        // 메모 불러오기
        public ArrayList<Memo> getMemosByGroupId(int groupId) {
                ArrayList<Memo> list = new ArrayList<>();

                // 해당 그룹의 메모 불러오기
                String sql = "SELECT memo_id, group_id, writer_id, content, "
                                + "DATE_FORMAT(created_at, '%Y-%m-%d %H:%i') as date_str "
                                + "FROM Memo "
                                + "WHERE group_id = ? "
                                + "ORDER BY created_at DESC";

                // DB 연결
                try (Connection conn = DBC.connect();
                                PreparedStatement pstmt = conn.prepareStatement(sql)) {

                        // SQL의 ? 부분 채우기
                        pstmt.setInt(1, groupId);

                        // 실행 및 결과 받기
                        try (ResultSet rs = pstmt.executeQuery()) {
                                while (rs.next()) {
                                        // DB에서 값 꺼내기
                                        int mId = rs.getInt("memo_id");
                                        int gId = rs.getInt("group_id");
                                        String wId = rs.getString("writer_id");
                                        String content = rs.getString("content");
                                        String date = rs.getString("date_str"); // 포맷팅된 날짜 문자열

                                        // 리스트에 추가
                                        list.add(new Memo(mId, gId, wId, content, date));
                                }
                        }
                } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("메모 조회 중 오류 발생");
                }

                return list;
        }

        // 그룹 생성
        boolean createGroup(String groupName, String writerId) {
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
                        // RETURN_GENERATED_KEYS: 방금 만든 그룹의 ID(PK)를 알아내기 위함
                        String sqlGroup = "INSERT INTO user_groups (group_name, invite_code) VALUES (?, ?)";
                        pstmtGroup = conn.prepareStatement(sqlGroup, Statement.RETURN_GENERATED_KEYS);
                        pstmtGroup.setString(1, groupName);
                        pstmtGroup.setString(2, inviteCode);
                        int result1 = pstmtGroup.executeUpdate();

                        // 방금 생성된 group_id 가져오기
                        int newGroupId = 0;
                        rs = pstmtGroup.getGeneratedKeys();
                        if (rs.next()) {
                                newGroupId = rs.getInt(1);
                        }

                        // group_members 테이블에 작성자를 admin으로 추가
                        String sqlMember = "INSERT INTO group_members (user_id, group_id, is_admin, progress) VALUES (?, ?, 'Y', 0)";
                        pstmtMember = conn.prepareStatement(sqlMember);
                        pstmtMember.setString(1, writerId);
                        pstmtMember.setInt(2, newGroupId);
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

        // 그룹 가입
        // 리턴값: 1 = 성공 || 0 = 초대코드 오류 || -1 = 이미 가입됨/DB오류
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
                                int groupId = rs.getInt("group_id");
                                String groupName = rs.getString("group_name");

                                // 해당 그룹에 멤버로 INSERT
                                // 일반 멤버(N), 진척도 0으로 시작
                                String sqlInsert = "INSERT INTO group_members (user_id, group_id, is_admin, progress, task) VALUES (?, ?, 'N', 0, NULL)";

                                pstmtInsert = conn.prepareStatement(sqlInsert);
                                pstmtInsert.setString(1, userId);
                                pstmtInsert.setInt(2, groupId);

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

        // 업무 갱신
        public boolean updateTask(String userId, int groupId, String task, String deadline) {
                String sql = "UPDATE group_members SET task = ?, deadline = ? WHERE user_id = ? AND group_id = ?";

                try (Connection conn = DBC.connect();
                                PreparedStatement pstmt = conn.prepareStatement(sql)) {

                        pstmt.setString(1, task);
                        pstmt.setString(2, deadline);
                        pstmt.setString(3, userId);
                        pstmt.setInt(4, groupId);

                        int result = pstmt.executeUpdate();
                        return result > 0; // 1개 이상 수정되면 성공

                } catch (SQLException e) {
                        e.printStackTrace();
                        System.out.println("업무 할당 실패: " + e.getMessage());
                        return false;
                }
        }

        // 업무 진척도 갱신
        public boolean updateProgress(String userId, int groupId, int progress) {
                // 유효성 검사 (0~100 사이 값만 허용)
                if (progress < 0 || progress > 100) {
                        System.out.println("진척도는 0~100 사이여야 합니다.");
                        return false;
                }

                String sql = "UPDATE group_members SET progress = ? WHERE user_id = ? AND group_id = ?";

                try (Connection conn = DBC.connect();
                                PreparedStatement pstmt = conn.prepareStatement(sql)) {

                        pstmt.setInt(1, progress);
                        pstmt.setString(2, userId);
                        pstmt.setInt(3, groupId);

                        int result = pstmt.executeUpdate();
                        return result > 0;

                } catch (SQLException e) {
                        e.printStackTrace();
                        System.out.println("진척도 수정 실패");
                        return false;
                }
        }

        // 6자리 랜덤 초대 코드 생성
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
}
