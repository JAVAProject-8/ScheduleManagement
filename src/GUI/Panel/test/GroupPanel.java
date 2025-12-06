package GUI.Panel.test;

import javax.swing.*;
import javax.swing.table.*;

import DB.Group;
import DB.SDAO;
// import DB.SDAO;
import DB.Schedule;
import DB.User;

import java.awt.*;
import java.time.*;
import java.util.*;

public class GroupPanel extends JPanel {
    private JComboBox<String> groupComboBox;
    private JTable table;
    private DefaultTableModel model;
    private User u = null;
    // 요일
    private final String[] DAYS = { "시간", "월", "화", "수", "목", "금", "토", "일" };
    // 시간
    private final int START_HOUR = 9;
    private final int END_HOUR = 23;

    private Map<String, Color> memberColorMap = new HashMap<>();

    public GroupPanel(User u) {
        this.u = u;

        setLayout(new BorderLayout());
        
        // 상단 패널 - 그룹 선택 콤보박스
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("그룹 선택"));
        groupComboBox = new JComboBox<>();
        
        topPanel.add(groupComboBox);
        add(topPanel, BorderLayout.NORTH);

        // 시간표 테이블 초기화
        initTable();

        // Mock DAO로 그룹 목록 불러오기
        loadGroupList();

        // 그룹 선택 이벤트
        groupComboBox.addActionListener(e -> {
            if (groupComboBox.getSelectedItem() != null) {
                String groupName = groupComboBox.getSelectedItem().toString();
                loadSchedulesForGroup(groupName);
            }
        });
    }

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
        GroupScheduleRenderer scheduleRenderer = new GroupScheduleRenderer();
        for (int col = 1; col <= 7; col++) {
            table.getColumnModel().getColumn(col).setCellRenderer(scheduleRenderer);
        }

        // 테이블 열 크기
        table.setRowHeight(50);
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

    /** Mock DAO로 그룹 목록 가져오기 */
    private void loadGroupList() {
        ArrayList<Group> groups = SDAO.getInstance().getMyGroups(u.getID());
        for (Group g : groups) {
            groupComboBox.addItem(g.getGroupName());
        }
    }

    /** 특정 그룹의 전체 일정 로드 */
    private void loadSchedulesForGroup(String groupName) {
        clearTable();
        // 그룹 명의 di 찾기
        ArrayList<Group> groups = SDAO.getInstance().getMyGroups(u.getID());
        String gid = null;
        for (Group g : groups) {
            if (g.getGroupName().equals(groupName)) {
                gid = g.getGroupId();
                break;
            }
        }
        // 그룹원 일정 로드
        ArrayList<Schedule> schedules = SDAO.getInstance().getGroupSchedules(gid);

        // 테이블 배치
        for (Schedule s : schedules) {
            addScheduleToTable(s);
        }

        table.repaint();
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

    /** 시간표 초기화 */
    private void clearTable() {
        for (int row = 0; row < 10; row++) {
            for (int col = 1; col <= 7; col++) {
                model.setValueAt("", row, col);
            }
        }
    }

    // 요일 -> 컬럼 변환 (월 1 ... 일 7)
    private int dayOfWeekToColumn(DayOfWeek dow) {
        // 그대로 사용(월 = 1)
        return dow.getValue();
    }

    // 일정 색상 렌더러
    private class GroupScheduleRenderer extends DefaultTableCellRenderer {
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
                c.setBackground(getColorForSchedule(s.getWriterId()));
            }

            return c;
        }
    }

    /** 일정 ID 기반 해시 색상 생성 */
    // 렌덤으로 0 ~ 255
    private Color getColorForSchedule(String id) {
        if (memberColorMap.containsKey(id)) {
            return memberColorMap.get(id);
        }

        int hash = Math.abs(id.hashCode());
        int r = Math.abs((hash * 37) % 200 + 30);
        int g = Math.abs((hash * 67) % 200 + 30);
        int b = Math.abs((hash * 97) % 200 + 30);
        
        return new Color(r, g, b, 60); // 약간 투명하게
    }
}
