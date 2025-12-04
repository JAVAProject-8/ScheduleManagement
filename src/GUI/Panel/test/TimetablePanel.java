package GUI.Panel.test;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import DB.Schedule;

import java.awt.*;
import java.util.ArrayList;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
// import java.time.format.DateTimeFormatter;
import java.util.Random;

public class TimetablePanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private final TestDAO mockDAO = new TestDAO();
    private String userId = null;
    private Random random = new Random();
    private int rd = random.nextInt(256);
    // 요일
    private final String[] DAYS = { "시간", "월", "화", "수", "목", "금", "토", "일" };
    // 시간
    private final int START_HOUR = 9;
    private final int END_HOUR = 18;
    
    // 패널 초기화
    public TimetablePanel(String userId) {
        this.userId = userId;
        setLayout(new BorderLayout());
        initTable();
        loadTestData(); // Mock DAO 호출
    }

    /** 테이블 초기화 */
    private void initTable() {
        int rows = END_HOUR - START_HOUR + 1;

        model = new DefaultTableModel(DAYS, rows) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 직접 편집 금지
            }
        };

        table = new JTable(model);
        
        // 시간 표시(0열)
        for (int i = 0; i < model.getRowCount(); i++) {
            int hour = START_HOUR + i;
            model.setValueAt(String.format("%02d:30", hour), i, 0);
        }

        // 일정 표시용 렌더러 적용
        // 전체 컬럼에 렌더러 적용 -> 버그 발생
        // table.setDefaultRenderer(Object.class, new ScheduleCellRenderer());
        
        // 해결 방안 -> 요일 컬럼(1~7)에만 일정 렌더러 적용
        ScheduleCellRenderer scheduleRenderer = new ScheduleCellRenderer();
        for (int col = 1; col <= 7; col++) {
            table.getColumnModel().getColumn(col).setCellRenderer(scheduleRenderer);
        }

        // 수평선 표시
        table.setShowHorizontalLines(true);
        // 수직선 표시
        table.setShowVerticalLines(true);
        // 그리드선 색상을 희색으로 변경
        table.setGridColor(Color.GRAY);
        // 셀 사이 간격 설정
        table.setIntercellSpacing(new java.awt.Dimension(1, 1));
        // 드래그 앤 드롭 지금
        table.setDragEnabled(false);
        // 테이블 이동 불가
        table.getTableHeader().setReorderingAllowed(false);
        // 테이블 크기조정 불기
        table.getTableHeader().setResizingAllowed(false);
        // 패널에 테이블 추가
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    /** Mock DAO 데이터 로드 */
    private void loadTestData() {
        ArrayList<Schedule> schedules = mockDAO.getPersonalSchedule(userId);
        for (Schedule s : schedules) {
            addScheduleToTable(s);
        }
    }

    /** 일정 1개를 시간표 테이블에 삽입 */
    private void addScheduleToTable(Schedule schedule) {
        LocalDateTime start = schedule.getStartAt();
        LocalDateTime end = schedule.getEndAt();

        int col = dayOfWeekToColumn(start.getDayOfWeek());
        int startRow = start.getHour() - START_HOUR;
        int endRow = end.getHour() - START_HOUR;

        // 범위 벗어나면 무시
        if (col < 1 || col > 7)
            return;
        if (startRow < 0 || startRow >= model.getRowCount())
            return;
        if (endRow < 0)
            endRow = startRow;

        // 일정 시간 블록 채우기
        for (int row = startRow; row <= endRow; row++) {
            model.setValueAt(schedule, row, col);
        }
    }

    /**
     * yyyy-MM-dd HH:mm 파싱
     * 
     * @param dt yyyy-MM-dd HH:mm 형식에 날짜+시간
     * @return 날짜 반환
     */
    /* private LocalDateTime parseDateTime(String dt) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return LocalDateTime.parse(dt, fmt);
    } */

    // 요일 -> 컬럼 변환 (월 1 ... 일 7)
    private int dayOfWeekToColumn(DayOfWeek dow) {
        // 그대로 사용(월 = 1)
        return dow.getValue();
    }

    // 일정 색상 렌더러
    private class ScheduleCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {

            Component c = super.getTableCellRendererComponent(table, value,
                    isSelected, hasFocus, row, column);

            // 기본값
            c.setBackground(Color.WHITE);

            // 일정 추가된 위치 색상 추가
            if (value instanceof Schedule) {
                Schedule s = (Schedule) value;
                setText(s.getScheduleDescription());
                c.setBackground(getColorForSchedule(rd));
            }

            return c;
        }
    }

    /** 일정 ID 기반 해시 색상 생성 */
    // 렌덤으로 0 ~ 255 
    private Color getColorForSchedule(int rd) {
        int r = (rd * 37) % 200 + 30;
        int g = (rd * 67) % 200 + 30;
        int b = (rd * 97) % 200 + 30;
        return new Color(r, g, b, 60); // 약간 투명하게
    }
}
