package DB;

// 일정 객체
public class Schedule {
    // 일정 id
    private int scheduleId;
    // 사용자 id
    private String writerId;
    // 일정 이름
    private String scheduleName;
    // 시작 날짜 + 시간
    // "2025-11-29 10:00"
    private String startAt;
    // 마감 날짜 + 시간
    // "2025-11-29 12:00"
    private String endAt;
    
    // 일정 객체 생성자
    public Schedule(int id, String writerId, String name, String startAt, String endAt) {
        this.scheduleId = id;
        this.writerId = writerId;
        this.scheduleName = name;
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
    // 일정 이름 리턴
    public String getScheduleName() {
        return scheduleName;
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
