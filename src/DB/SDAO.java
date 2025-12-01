package DB;

import java.sql.*;
import java.util.ArrayList;

public class SDAO {
        // 오늘의 일정을 리스트로 리턴
        public ArrayList<Schedule> getTodaySchedules(String userId) {
                ArrayList<Schedule> list = new ArrayList<>();

                // 오늘 날짜에 포함되는 일정 조회
                String sql = "SELECT schedule_id, writer_id, schedule_name, "
                                + "DATE_FORMAT(start_at, '%Y-%m-%d %H:%i') as start_str, "
                                + "DATE_FORMAT(end_at, '%Y-%m-%d %H:%i') as end_str "
                                + "FROM schedules " // ★ 테이블명 확인!
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
                                        String name = rs.getString("schedule_name");
                                        String start = rs.getString("start_str");
                                        String end = rs.getString("end_str");

                                        list.add(new Schedule(id, wId, name, start, end));
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

                String sql = "SELECT schedule_id, writer_id, schedule_name, "
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
                                        String name = rs.getString("schedule_name");
                                        String start = rs.getString("start_str");
                                        String end = rs.getString("end_str");

                                        list.add(new Schedule(id, wId, name, start, end));
                                }
                        }
                } catch (Exception e) {
                        e.printStackTrace();
                }

                return list;
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

}
