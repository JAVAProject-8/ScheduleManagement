package DB;

import java.time.LocalDateTime;

public class Memo {
    int memo_id; 		// 메모 id
    String group_id; 	// 그룹 id
    String writer_id;	// 작성자 id (사용자 id)
    String content;		// 내용
    LocalDateTime created_at;	// 작성일시
    
    // 생성자
    public Memo(String group_id, String writer_id, String content, LocalDateTime created_at) {
        this.group_id = group_id;
        this.writer_id = writer_id;
        this.content = content;
        this.created_at = created_at;
    }

    public Memo(int memo_id, String group_id, String writer_id, String content, LocalDateTime created_at) {
        this.memo_id = memo_id;
        this.group_id = group_id;
        this.writer_id = writer_id;
        this.content = content;
        this.created_at = created_at;
    }

    public int getMemoId() {
        return memo_id;
    }

    public String getGroupId() {
        return group_id;
    }

    public String getWriterId() {
        return writer_id;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return created_at;
    }
}