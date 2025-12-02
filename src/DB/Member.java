package DB;

public class Member {
    // 그룹원 id
    String user_id;
    // 그룹 id
    int group_id;
    // 관리자 권한
    String is_admin;
    // 업무
    String task;
    // 유저 이름
    String user_name;

    public Member(String user_id, int group_id, String is_admin, String task, String user_name) {
        this.user_id = user_id;
        this.group_id = group_id;
        this.is_admin = is_admin;
        this.task = task;
        this.user_name = user_name;
    }
}
