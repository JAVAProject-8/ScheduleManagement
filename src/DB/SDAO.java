package DB;

import java.sql.*;
import java.util.ArrayList;

public class SDAO {
        // 오늘의 일정을 리스트로 리턴
        public ArrayList<Schedule> getTodaySchedules(String userId) {
                ArrayList<Schedule> list = new ArrayList<>();

                // 오늘 날짜에 포함되는 일정 조회
                String sql = "SELECT schedule_id, writer_id, title, "
                                + "DATE_FORMAT(start_datetime, '%Y-%m-%d %H:%i') as start_str, "
                                + "DATE_FORMAT(end_datetime, '%Y-%m-%d %H:%i') as end_str "
                                + "FROM Schedules "
                                + "WHERE writer_id = ? "
                                + "AND start_datetime <= CONCAT(CURDATE(), ' 23:59:59') "
                                + "AND end_datetime >= CONCAT(CURDATE(), ' 00:00:00') "
                                + "ORDER BY start_datetime ASC";

                // DB 연결
                try (Connection conn = DBC.connect();
                                PreparedStatement pstmt = conn.prepareStatement(sql)) {

                        pstmt.setString(1, userId);

                        // 받아온 일정을 객체로 만들어 저장
                        try (ResultSet rs = pstmt.executeQuery()) {
                                while (rs.next()) {
                                        int id = rs.getInt("schedule_id");
                                        String wId = rs.getString("writer_id");
                                        String name = rs.getString("title");
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

                String sql = "SELECT schedule_id, writer_id, title, "
                                + "DATE_FORMAT(start_datetime, '%Y-%m-%d %H:%i') as start_str, " // Timestamp 타입을
                                                                                                 // String으로 변환
                                + "DATE_FORMAT(end_datetime, '%Y-%m-%d %H:%i') as end_str, "
                                + "From Schedule "
                                + "WHERE writer_id = ? "
                                + "AND end_datetime BEWTWEEN NOW() AND DATE_ADD(NOW(), INTERVAL 2 DAY)" // 마감 2일전부터 불러오기
                                + "ORDER BY end_datetime ASC";

                try (Connection conn = DBC.connect(); // DB 연결
                                PreparedStatement pstmt = conn.prepareStatement(sql)) {

                        pstmt.setString(1, userId);

                        // 받아온 일정을 객체로 만들어 저장
                        try (ResultSet rs = pstmt.executeQuery()) {
                                while (rs.next()) {
                                        int id = rs.getInt("schedule_id");
                                        String wId = rs.getString("writer_id");
                                        String name = rs.getString("title");
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
