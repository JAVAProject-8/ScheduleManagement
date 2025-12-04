package GUI.Panel.test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import DB.Schedule;

// TODO : 테스트용 DAO
public class TestDAO {
    private TestDAO() {
    }

    private static TestDAO instance = new TestDAO();

    // SDAO 객체 반환
    public static TestDAO getInstance() {
        return instance;
    }

    public ArrayList<Schedule> getPersonalSchedule(String userId) {
        ArrayList<Schedule> list = new ArrayList<>(); 

        // "2025-11-27 09:00"
        // "2025-11-27 12:15"
        // public Schedule(int id, String writerId, String groupId, String name, String type, LocalDateTime startAt, LocalDateTime endAt)
        list.add(new Schedule(
                1, userId, "자료구조 강의", "일", "학교",
                LocalDateTime.of(2025, 12, 04, 9, 0),
                LocalDateTime.of(2025, 12, 04, 12, 15)
        ));
        // "2025-11-25 13:00",
        // "2025-11-25 14:00"
        list.add(new Schedule(
                2, userId, "헬스장", "일", "학교",
                LocalDateTime.of(2025, 12, 25, 13, 0),
                LocalDateTime.of(2025, 12, 25, 14, 0)
        ));
        // "2025-11-26 15:00",
        // "2025-11-26 16:00"
        list.add(new Schedule(
                3, userId, "팀 프로젝트 회의", "일", "학교",
                LocalDateTime.of(2025, 12, 26, 15, 0),
                LocalDateTime.of(2025, 12, 26, 16, 0)
        ));
        // "2025-11-28 11:00",
        // "2025-11-28 12:00"
        list.add(new Schedule(
                4, userId, "자바 과제", "일", "학교",
                LocalDateTime.of(2025, 12, 28, 11, 0),
                LocalDateTime.of(2025, 12, 28, 12, 0)
        ));

        return list;
    }

    public ArrayList<String> getGroupList() {
        ArrayList<String> glist = new ArrayList<>();
        glist.add("GA");
        glist.add("GB");
        
        return glist;
    }

    public ArrayList<Schedule> getGroupSchedules(String groupName) {
        ArrayList<Schedule> list = new ArrayList<>();
        
        if (groupName.equals("GA")) {
                list.add(new Schedule(
                        "user1", 
                        "GA", 
                        "회의", 
                        "일",
                        LocalDateTime.of(2025, 12, 01, 10, 0),
                        LocalDateTime.of(2025, 12, 02, 11, 0)
                ));
                list.add(new Schedule(
                        "user2", 
                        "GA", 
                        "설계 작업", 
                        "일",
                        LocalDateTime.of(2025, 12, 02, 14, 0),
                        LocalDateTime.of(2025, 12, 02, 15, 0)
                ));
                list.add(new Schedule(
                        "user1", 
                        "GA", 
                        "보고서", 
                        "일",
                        LocalDateTime.of(2025, 12, 03, 9, 0),
                        LocalDateTime.of(2025, 12, 03, 10, 0)
                ));
        }

        else if (groupName.equals("GB")) {
                list.add(new Schedule(
                        "kim", 
                        "GB", 
                        "스터디", 
                        "일",
                        LocalDateTime.of(2025, 12, 01, 15, 0),
                        LocalDateTime.of(2025, 12, 01, 17, 0)
                ));
                list.add(new Schedule(
                        "lee", 
                        "GB", 
                        "자료 준비", 
                        "일",
                        LocalDateTime.of(2025, 12, 02, 13, 0),
                        LocalDateTime.of(2025, 12, 02, 15, 0)
                ));
        }
        else {
                list = null;
        }

        return list;
    }

    public ArrayList<Schedule> getScheduleDate(int year, int month, int day, String userId) {
        ArrayList<Schedule> list = new ArrayList<>();
        ArrayList<Schedule> reslist = new ArrayList<>();

        list.add(new Schedule(
            userId, "자료구조 강의", "일",
            LocalDateTime.of(2025, 12, 04, 9, 0),
            LocalDateTime.of(2025, 12, 04, 12, 15)));
        
        list.add(new Schedule(
            userId, "헬스장", "일",
            LocalDateTime.of(2025, 12, 04, 13, 0),
            LocalDateTime.of(2025, 12, 04, 14, 0)));

        list.add(new Schedule(
            userId, "팀 프로젝트 회의", "일",
            LocalDateTime.of(2025, 12, 26, 15, 0),
            LocalDateTime.of(2025, 12, 26, 16, 0)));
    
        list.add(new Schedule(
            userId, "자바 과제", "일",
                    LocalDateTime.of(2025, 12, 28, 11, 0),
            LocalDateTime.of(2025, 12, 28, 12, 0)));

        for (Schedule s : list) {
            if (s.getStartAt().getYear() == year && s.getStartAt().getMonthValue() == month && s.getStartAt().getDayOfMonth() == day) {
                reslist.add(s);
            }
        }
        
        return reslist;
    }
}