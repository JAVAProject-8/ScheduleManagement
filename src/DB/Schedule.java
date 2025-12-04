package DB;

import java.time.LocalDateTime;

// 일정 객체
public class Schedule {
    private int scheduleId; 		// 일정 id
    private String writerId;		// 사용자 id
    private String groupId;			// 그룹 id
    private String scheduleDescription;	// 일정 내용
    private String scheduleType;	// 일정 종류
    private LocalDateTime startAt;	// 시작 날짜 + 시간 "2025-11-29 10:00"
    private LocalDateTime endAt;	// 마감 날짜 + 시간 "2025-11-29 12:00"

    // 개인 일정 생성자
    public Schedule(String writerId, String description, String type, LocalDateTime startAt,
            LocalDateTime endAt) {
        this.writerId = writerId;
        this.scheduleDescription = description;
        this.scheduleType = type;
        this.startAt = startAt;
        this.endAt = endAt;
    }

    // 그룹 일정 생성자
    public Schedule(String writerId, String groupId, String description, String type, LocalDateTime startAt,
            LocalDateTime endAt) {
        this.writerId = writerId;
        this.groupId = groupId;
        this.scheduleDescription = description;
        this.scheduleType = type;
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public Schedule(int id, String writerId, String groupId, String description, String type, LocalDateTime startAt,
            LocalDateTime endAt) {
        this.scheduleId = id;
        this.writerId = writerId;
        this.groupId = groupId;
        this.scheduleDescription = description;
        this.scheduleType = type;
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public int getScheduleId() { // 일정 id 리턴
        return scheduleId;
    }

    public String getWriterId() { // 사용자 id 리턴
        return writerId;
    }

    public String getGroupId() { // 그룹 id 리턴
        return groupId;
    }

    public String getScheduleDescription() { // 일정 이름 리턴
        return scheduleDescription;
    }

    public String getScheduleType() { // 일정 종류 리턴
        return scheduleType;
    }

    // 시작 날짜 + 시간 리턴
    public LocalDateTime getStartAt() {
        return startAt;
    }

    // 마감 날짜 + 시간 리턴
    public LocalDateTime getEndAt() {
        return endAt;
    }
}