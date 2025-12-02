package GUI.Panel.test;

import java.util.ArrayList;
import DB.Schedule;

public class TestDAO {
    public ArrayList<Schedule> getPersonalSchedule(String userId) {
        ArrayList<Schedule> list = new ArrayList<>();

        list.add(new Schedule(
                1, userId, 0, "자료구조 강의", "일",
                "2025-11-27 09:00",
                "2025-11-27 12:15"
        ));
        list.add(new Schedule(
                2, userId, 0, "헬스장", "일",
                "2025-11-25 13:00",
                "2025-11-25 14:00"
        ));
        list.add(new Schedule(
                3, userId, 0, "팀 프로젝트 회의", "일",
                "2025-11-26 15:00",
                "2025-11-26 16:00"
        ));
        list.add(new Schedule(
                4, userId, 0, "자바 과제", "일",
                "2025-11-28 11:00",
                "2025-11-28 12:00"
        ));

        return list;
    }

    public ArrayList<String> getGroupList() {
        ArrayList<String> glist = new ArrayList<>();
        glist.add("GA");
        glist.add("GB");
        glist.add("GC");
        glist.add("GD");

        return glist;
    }

    public ArrayList<Schedule> getGroupSchedules(String groupName) {
        ArrayList<Schedule> list = new ArrayList<>();
        
        if (groupName.equals("GA")) {
            list.add(new Schedule(
                        1, "user1", 1, "회의", "일",
                        "2025-12-01 10:00", "2025-12-01 11:00")
                );
            list.add(new Schedule(
                        2, "user2", 1, "설계 작업", "일",
                        "2025-12-02 14:00", "2025-12-02 15:00")
                );
            list.add(new Schedule(
                        3, "user1", 1, "보고서", "일",
                        "2025-12-03 09:00", "2025-12-03 10:00")
                );
        }

        else if (groupName.equals("GB")) {
            list.add(new Schedule(4, "kim", 2, "스터디", "일",
                                    "2025-12-01 15:00", "2025-12-01 17:00"));
            list.add(new Schedule(5, "lee", 2, "자료 준비", "일",
                                    "2025-12-02 13:00", "2025-12-02 15:00"));
        }
        else {
                list = null;
        }

        return list;
    }
}


