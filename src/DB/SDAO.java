package DB;

import java.sql.*;
import java.util.ArrayList;

public class SDAO {
        // 테스트 일정 데이터
        // TODO : SDAO 테스트 용이며 DB에서 이러한 데이터를 조회해야함
        // private String today = "2025-11-29"; // 테스트용 오늘 날짜
        // private String near1 = "2025-11-30"; // 내일
        // private String near2 = "2025-12-01"; // 2일 뒤

        // 오늘의 일정을 리스트로 리턴
        public ArrayList<Schedule> getTodaySchedules(String userId) {
                ArrayList<Schedule> list = new ArrayList<>();

                // 오늘 날짜에 포함되는 일정 조회
                String sql = "SELECT schedule_id, writer_id, title, "
                                + "DATE_FORMAT(start_datetime, '%Y-%m-%d %H:%i') as start_str, "
                                + "DATE_FORMAT(end_datetime, '%Y-%m-%d %H:%i') as end_str "
                                + "FROM Schedule "
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
                                + "WHERE writer_id = " + userId + " "
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

                // DB에서 마감되어 가는 일정을 조화 후 해당 일정을 전부 조회해서 일정 객체로 리스트를 생성
                // 예
                // list.add(new Schedule(
                // 3, userId,
                // "응용프로젝트 보고서 제출",
                // today + " 09:00", near1 + " 23:59"));

                // list.add(new Schedule(
                // 4, userId,
                // "DB 모델링 마감",
                // today + " 09:00", near2 + " 23:59"));

        }
}
