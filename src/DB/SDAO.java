package DB;

import java.util.ArrayList;

public class SDAO {
    // 테스트 일정 데이터
    // TODO : SDAO 테스트 용이며 DB에서 이러한 데이터를 조회해야함
    private String today = "2025-11-29"; // 테스트용 오늘 날짜
    private String near1 = "2025-11-30"; // 내일
    private String near2 = "2025-12-01"; // 2일 뒤

    // 오늘의 일정를 리스트로 리턴
    public ArrayList<Schedule> getTodaySchedules(String userId) {
        ArrayList<Schedule> list = new ArrayList<>();

        // DB에서 오늘의 일정을 전부 조회해서 일정 객체로 리스트를 생성
        // 예
        list.add(new Schedule(
                1, userId,
                "자료구조 팀플 회의",
                today + " 10:00", today + " 12:00"));

        list.add(new Schedule(
                2, userId,
                "캡스톤 구현 작업",
                today + " 14:00", today + " 17:00"));

        return list;
    }

    // 마감 일정를 리스트로 리턴
    public ArrayList<Schedule> getDeadlineSchedules(String userId) {
        ArrayList<Schedule> list = new ArrayList<>();

        // DB에서 마감되어 가는 일정을 조화 후 해당 일정을 전부 조회해서 일정 객체로 리스트를 생성
        // 예
        list.add(new Schedule(
                3, userId,
                "응용프로젝트 보고서 제출",
                today + " 09:00", near1 + " 23:59"));

        list.add(new Schedule(
                4, userId,
                "DB 모델링 마감",
                today + " 09:00", near2 + " 23:59"));

        return list;
    }
}
