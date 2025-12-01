package DB;

public class Group {
    // 그룹 id
    int group_id;
    // 그룹 이름
    String group_name;
    // 초대 코드
    String invite_code;

    // 생성자
    public Group(int group_id, String group_name, String invite_code) {
        this.group_id = group_id;
        this.group_name = group_name;
        this.invite_code = invite_code;
    }

    public int getGroupId() {
        return group_id;
    }

    public String getGroupName() {
        return group_name;
    }

    public String getInviteCode() {
        return invite_code;
    }
}
