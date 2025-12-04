package GUI.Panel;

/* 
import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {
    private JTextArea todayArea;
    private JTextArea deadlineArea;

    public MainPanel() {
        setLayout(new BorderLayout());

        JPanel top = new JPanel(new GridLayout(1, 2));
        JLabel label = new JLabel("오늘의 일정");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        top.add(label);
        JLabel label_1 = new JLabel("마감 임박");
        label_1.setHorizontalAlignment(SwingConstants.CENTER);
        top.add(label_1);
        add(top, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(1, 2));
        todayArea = new JTextArea();
        deadlineArea = new JTextArea();

        center.add(new JScrollPane(todayArea));
        center.add(new JScrollPane(deadlineArea));

        add(center, BorderLayout.CENTER);
    }
}
 */

import javax.swing.*;

import DB.Schedule;
import java.awt.*;
import java.util.ArrayList;

import DB.SDAO;

// 메인 패널
// 오늘 일정, 마감 일정 표시
public class MainPanel extends JPanel {
    // 오늘의 일정 테스트 에리어
    private JTextArea todayArea;
    // 마감 일정 테스트 에리어
    private JTextArea deadlineArea;
    // 사용자 id
    private String userId;

    // 객체 생성시 사용자 id, DB를 받음
    // 그리고 GUI 기본 설정
    public MainPanel(String userId) {
        this.userId = userId;

        setLayout(new BorderLayout());

        // 상단 라벨
        JPanel top = new JPanel(new GridLayout(1, 2));
        top.add(new JLabel("오늘의 일정"));
        top.add(new JLabel("마감 임박"));
        add(top, BorderLayout.NORTH);

        // 중앙 영역
        JPanel center = new JPanel(new GridLayout(1, 2));

        // 테스트 에리어
        todayArea = new JTextArea();
        deadlineArea = new JTextArea();
        
        // 테스트 에리어 입력 제한
        todayArea.setEditable(false);
        deadlineArea.setEditable(false);
        todayArea.setEnabled(false);
        deadlineArea.setEnabled(false);
        
        // 스크롤에 테스트 에리어를 추가하고 그 스크롤를 중앙 패널에 추가
        center.add(new JScrollPane(todayArea));
        center.add(new JScrollPane(deadlineArea));

        // 패널에 스크롤 추가
        add(center, BorderLayout.CENTER);

        // DB에서 일정 가져와서 추가함
        loadSchedules();
    }

    // DB로부터 일정을 가져오는 메소드
    private void loadSchedules() {
        // Schedule 일정 정보
        // DB에서 오늘의 일정을 가져옴
        ArrayList<Schedule> todayList = SDAO.getInstance().getSchedules(userId);
        // DB에서 마감 일정을 가져옴
        ArrayList<Schedule> deadlineList = SDAO.getInstance().getSchedules(userId);

        // DB에서 가져온 오늘의 일정를 연결
        StringBuilder today = scheduleBuilder(todayList, "");
        // DB에서 가져온 마감 일정를 연결
        StringBuilder deadline = scheduleBuilder(deadlineList, "마감");

        // 연결된 오늘의 일정 테스트 에리어에 추가
        todayArea.setText(today.toString());
        // 연결된 마감 일정 테스트 에리어에 추가
        deadlineArea.setText(deadline.toString());
    }

    // DB에서 가져온 데이터를 연결하는 메소드
    public StringBuilder scheduleBuilder(ArrayList<Schedule> schedulelList, String type) {
        // StringBuilder를 참조한 링크 : https://onlyfor-me-blog.tistory.com/317
        StringBuilder sb = new StringBuilder();

        // DB에서 가져온 데이터를 연결
        for (int i = 0; i < schedulelList.size(); i++) {
            // 구조 : i. [타입] ([날짜 + 시간])
            sb
                .append(i + 1)
                .append(". ")
                .append(schedulelList.get(i).getScheduleDescription())
                .append("  (").append(type)
                .append(schedulelList.get(i).getStartAt())
                .append(")\n");
        }

        // 연결된 일정를 리턴
        return sb;
    }
}