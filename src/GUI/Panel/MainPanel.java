package GUI.Panel;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;

import DB.SDAO;
import DB.Schedule;
import DB.User;

// 메인 패널
// 오늘 일정, 마감 일정 표시
public class MainPanel extends JPanel {
    private User u = null;                                // 사용자 객체
    private DefaultTableModel todayModel;                // 오늘 일정 기본 테이블 모델
    private DefaultTableModel deadlineModel;             // 마감 일정 기본 테이블 모델
    private JTable todayTable;                           // 일정 테이블
    private JTable deadlineTable;                        // 일정 테이블
    private String[] t = { "구분", "내용", "날짜", "시간"};   // 공통 테이블 속성명

    // 객체 생성시 사용자 객체 받음
    // 그리고 GUI 기본 설정
    public MainPanel(User u) {
        this.u = u;

        setLayout(new BorderLayout());

        // 상단 라벨
        JLabel label1 = new JLabel("오늘의 일정", JLabel.CENTER);
        label1.setFont(new Font("Malgun Gothic", Font.BOLD, 20));
        JLabel label2 = new JLabel("마감 임박", JLabel.CENTER);
        label2.setFont(new Font("Malgun Gothic", Font.BOLD, 20));
        
        JPanel top = new JPanel(new GridLayout(1, 2));
        top.add(label1);
        top.add(label2);
        add(top, BorderLayout.NORTH);

        // 테이블 초기화
        initTable();

        // DB에서 일정 가져와서 추가함
        loadSchedules();
    }
    
    // 테이블 초기화
    private void initTable() {
        // 오늘 일정 기본 테이블 모델 설장
        todayModel = new DefaultTableModel(t, 0) {
            // 직접 편집 금지
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        // 마감 일정 기본 테이블 모델 설장
        deadlineModel = new DefaultTableModel(t, 0) {
            // 직접 편집 금지
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 직접 편집 금지
            }
        };

        // 일정 테이블 생성
        todayTable = new JTable(todayModel);
        deadlineTable = new JTable(deadlineModel);
        // 테이블 열 크기
        todayTable.setRowHeight(20);
        deadlineTable.setRowHeight(20);
        // 수평선 표시
        todayTable.setShowHorizontalLines(true);
        deadlineTable.setShowHorizontalLines(true);
        // 수직선 표시
        todayTable.setShowVerticalLines(true);
        deadlineTable.setShowVerticalLines(true);
        // 그리드선 색상을 희색으로 변경
        todayTable.setGridColor(Color.GRAY);
        deadlineTable.setGridColor(Color.GRAY);
        // 셀 사이 간격 설정
        todayTable.setIntercellSpacing(new java.awt.Dimension(1, 1));
        deadlineTable.setIntercellSpacing(new java.awt.Dimension(1, 1));
        // 드래그 앤 드롭 지금
        todayTable.setDragEnabled(false);
        deadlineTable.setDragEnabled(false);
        // 테이블 이동 불가
        todayTable.getTableHeader().setReorderingAllowed(false);
        deadlineTable.getTableHeader().setReorderingAllowed(false);
        // 테이블 크기조정 불기
        todayTable.getTableHeader().setResizingAllowed(false);
        deadlineTable.getTableHeader().setResizingAllowed(false);
        // 중앙 영역 패널
        JPanel center = new JPanel(new GridLayout(1, 2));
        // 스크롤에 테스트 에리어를 추가하고 그 스크롤를 중앙 패널에 추가
        center.add(new JScrollPane(todayTable));
        center.add(new JScrollPane(deadlineTable));
        // 패널에 스크롤 추가
        add(center, BorderLayout.CENTER);
    }

    // DB로부터 일정을 가져오는 메소드
    private void loadSchedules() {
        // Schedule 일정 정보
        // DB에서 오늘의 일정
        ArrayList<Schedule> todayList = scheduleList();

        // 3일 이내
        // DB에서 마감 일정
        ArrayList<Schedule> deadlineList = scheduleList(3);

        if (todayList != null) {        // 오늘 일정 리스드가 null이 아니면
            for (Schedule s : todayList) {
                todayModel.addRow(
                    new Object[] { 
                        s.getScheduleType(), 
                        s.getScheduleDescription(),
                        s.getStartAt().toLocalDate(),
                        s.getStartAt().toLocalTime()
                    }
                );
            }
        }
        
        if (deadlineList != null) {         // 마감 일정 리스드가 null이 아니면
            for (Schedule s : deadlineList) {
                deadlineModel.addRow(
                    new Object[] { 
                        s.getScheduleType(), 
                        s.getScheduleDescription(),
                        s.getEndAt().toLocalDate(),
                        s.getEndAt().toLocalTime()
                    }
                );
            }
        }
    }

    // 오늘 일정 
    private ArrayList<Schedule> scheduleList() {
        // 사용자에 모든 일정
        ArrayList<Schedule> schedules = SDAO.getInstance().getSchedules(u.getID());
        // 필터되 일정
        ArrayList<Schedule> res = new ArrayList<>();
        // 현재 날짜
        LocalDate today = LocalDate.now();

        for (Schedule s : schedules) {        
            if (s.getStartAt().toLocalDate().equals(today)) {
                res.add(s);
            }
        }
        
        // 특정 시간 기준으로 오름차순 정렬
        res.sort(Comparator.comparing(Schedule::getStartAt));
        return res;
    }

    // 마감 일정이 오늘으로 부터 daysInclusive일 이내인 일정을 구함
    private ArrayList<Schedule> scheduleList(int daysInclusive) {
        // 사용자에 모든 일정
        ArrayList<Schedule> schedules = SDAO.getInstance().getSchedules(u.getID());
        // 필터되 일정
        ArrayList<Schedule> res = new ArrayList<>();

        // 일정 객체 나, 매개변수가 0 이하면 메소드 리턴
        if (schedules == null || daysInclusive < 0) return res;

        // 오늘 날짜
        LocalDate today = LocalDate.now();
        // 오늘으로 부터 3일 이내 날짜
        LocalDate end = today.plusDays(daysInclusive);

        // 오늘으로 부터 3일 이내인 일정 비교
        for (Schedule s : schedules) {
            // 일정 객체 나 일정 시작 날짜가 null이면 비교 안함
            if (s == null || s.getEndAt() == null) continue;

            // 오늘 < 일정 <= 오늘으로 부터 end일 이내
            if (s.getEndAt().toLocalDate().isAfter(today) && !s.getEndAt().toLocalDate().isAfter(end)) {
                res.add(s);
            }
        }
        // 특정 종료 기준으로 오름차순 정렬
        res.sort(Comparator.comparing(Schedule::getEndAt).thenComparing(Schedule::getEndAt));

        return res;
    }
}