package DB;

public class Member {
    String user_id;		// 그룹원 id
    String group_id;	// 그룹 id
    String is_admin;	// 관리자 권한
    String task;		// 업무
    String user_name;	// 유저 이름

    public Member(String user_id, String group_id, String is_admin, String task, String user_name) {
        this.user_id = user_id;
        this.group_id = group_id;
        this.is_admin = is_admin;
        this.task = task;
        this.user_name = user_name;
    }
    
    public String getUserName() {
    	return this.user_name;
    }
    
    public String getTask() {
    	return this.task;
    }
    
    public String getPosition() {
    	return this.is_admin;
    }
}