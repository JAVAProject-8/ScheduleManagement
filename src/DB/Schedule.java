package DB;

// 일정 객체
public class Schedule {
    // 일정 id
    private int scheduleId;
    // 사용자 id
    private String writerId;
    // 그룹 id
    private int groupId;
    // 일정 이름
    private String scheduleName;
    // 일정 종류
    private String scheduleType;
    // 시작 날짜 + 시간
    // "2025-11-29 10:00"
    private String startAt;
    // 마감 날짜 + 시간
    // "2025-11-29 12:00"
    private String endAt;
    
    // 일정 객체 생성자
    public Schedule(int id, String writerId, int groupId, String name, String type, String startAt, String endAt) {
        this.scheduleId = id;
        this.writerId = writerId;
        this.groupId = groupId;
        this.scheduleName = name;
        this.scheduleType = type;
        this.startAt = startAt;
        this.endAt = endAt;
    }
    // 일정 id 리턴
    public int getScheduleId() {
        return scheduleId;
    }
    // 사용자 id 리턴
    public String getWriterId() {
        return writerId;
    }
    // 그룹 id 리턴
    public int getGroupId() {
		return groupId;
	}
    // 일정 이름 리턴
    public String getScheduleName() {
        return scheduleName;
    }
    // 일정 종류 리턴
    public String getScheduleType() {
        return scheduleType;
    }
    // 시작 날짜 + 시간 리턴
    public String getStartAt() {
        return startAt;
    }
    // 마감 날짜 + 시간 리턴
    public String getEndAt() {
        return endAt;
    }
	
}
