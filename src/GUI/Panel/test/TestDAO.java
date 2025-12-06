package GUI.Panel.test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import DB.Schedule;

// TODO : 테스트용 DAO
public class TestDAO {
    private Schedule[] list = {
        new Schedule(
                1, "id1", "GA", "자료구조 강의", "강의",
                LocalDateTime.of(2025, 12, 6, 9, 0),
                LocalDateTime.of(2025, 12, 8, 12, 15)
        ),
        new Schedule(
                2, "id1", "GA", "헬스장", "운동",
                LocalDateTime.of(2025, 12, 6, 13, 0),
                LocalDateTime.of(2025, 12, 7, 14, 0)
        ),
        new Schedule(
                3, "id1", "GA", "팀 프로젝트 회의", "학교",
                LocalDateTime.of(2025, 12, 26, 15, 0),
                LocalDateTime.of(2025, 12, 26, 16, 0)
        ),
        new Schedule(
                4, "id1", "GA", "자바 과제", "학교",
                LocalDateTime.of(2025, 12, 28, 11, 0),
                LocalDateTime.of(2025, 12, 28, 12, 0)
        ),
        new Schedule(
                4, "id1", "GB", "C 과제", "학교",
                LocalDateTime.of(2025, 12, 23, 10, 0),
                LocalDateTime.of(2025, 12, 24, 12, 0)
        ),
        new Schedule(
                4, "id1", "GB", "SW 공학 과제", "학교",
                LocalDateTime.of(2025, 12, 28, 9, 0),
                LocalDateTime.of(2025, 12, 29, 12, 0)
        ),
        new Schedule(
                4, "id2", "GC", "공학 과제", "학교",
                LocalDateTime.of(2025, 12, 7, 12, 0),
                LocalDateTime.of(2025, 12, 10, 13, 0)
        )
    };

    private TestDAO() {
    }

    private static TestDAO instance = new TestDAO();

    // SDAO 객체 반환
    public static TestDAO getInstance() {
        return instance;
    }

    public ArrayList<Schedule> getSchedules(String userId) {
        ArrayList<Schedule> reslist = new ArrayList<>(); 

        for (Schedule s : list) {
            reslist.add(s);
        }
        return reslist;
    }

    public ArrayList<String> getGroupList() {
        ArrayList<String> reslist = new ArrayList<>();
        
        for (Schedule s : list) {
            if (!reslist.contains(s.getGroupId())) {
                reslist.add(s.getGroupId());
            }
        }
        
        return reslist;
    }

    public ArrayList<Schedule> getGroupSchedules(String groupName) {
        
        ArrayList<Schedule> reslist = new ArrayList<>();
        for (Schedule s : list) {
            if (s.getGroupId().equals(groupName)) {
                reslist.add(s);
            }
        }

        return reslist;
    }

    public ArrayList<Schedule> getScheduleDate(LocalDate ld, String userId) {
        ArrayList<Schedule> reslist = new ArrayList<>();

        for (Schedule s : list) {
            // if (s.getStartAt().getYear() == year && s.getStartAt().getMonthValue() == month && s.getStartAt().getDayOfMonth() == day) {
            if (s.getStartAt().toLocalDate().equals(ld)) {
                reslist.add(s);
            }
        }
        
        return reslist;
    }
}