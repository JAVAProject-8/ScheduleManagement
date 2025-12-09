package GUI.Panel;

import GUI.Dialog.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import DB.SDAO;
import DB.Schedule;
import DB.User;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

public class TimetablePanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private User u = null;
    // 요일
    private final String[] DAYS = { "시간", "월", "화", "수", "목", "금", "토", "일" };
    // 시간
    private final int START_HOUR = 9;
    private final int END_HOUR = 23;
    
    // 패널 초기화
    public TimetablePanel(User u) {
        this.u = u;
        setLayout(new BorderLayout());
        
        initTable();
        loadScheduleData(); // Mock DAO 호출
    }

    /** 테이블 초기화 */
    private void initTable() {
    	LocalDate today = LocalDate.now();	// 현재 날짜
    	LocalDate mondayDate = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));	// 이전 날짜 중 가장 가까운 월요일을 탐색
    	
    	for(int i = 1; i < 8; i++) {
    		DAYS[i] += mondayDate.plusDays(i - 1).format(DateTimeFormatter.ofPattern("(MM/dd)"));
    	}
    	
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

        // 요일 컬럼(1~7)에만 일정 렌더러 적용
        ScheduleCellRenderer scheduleRenderer = new ScheduleCellRenderer();
        for (int col = 1; col <= 7; col++) {
            table.getColumnModel().getColumn(col).setCellRenderer(scheduleRenderer);
        }   

        // 테이블 열 크기
        table.setRowHeight(40);
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
        table.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		if(e.getClickCount() == 2) {	// 더블 클릭 시
        			int row = table.getSelectedRow();
        			int col = table.getSelectedColumn();
        			
        			if (col < 1) {	// 시간표 외 영역 선택 시
        				return;
        			}
        			
        			Object obj = model.getValueAt(row, col);	// 모델에 존재하는 Object를 가져옴
        			
        			if(obj instanceof Schedule) {	// 해당 Object가 Schedule 객체라면
        				Schedule selectedSchedule = (Schedule)obj;	// 형변환
        				
        				ScheduleDialog scheduleDialog = new ScheduleDialog(null, "일정 수정", u, selectedSchedule);	// 일정 수정
        				scheduleDialog.setVisible(true);
        			}
        			else {
            			LocalDate selectedDate = mondayDate.plusDays(col - 1);			// 선택일 계산
            			LocalTime selectedTime = LocalTime.of(START_HOUR + row, 30);	// 선택 시간 계산
            			LocalDateTime selectedDateTime = LocalDateTime.of(selectedDate, selectedTime);
            			
            			new ScheduleDialog(null, "일정 추가", u, selectedDateTime);
        			}
        			
        			loadScheduleData();
        		}
        	}
        });
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setBorder(new TitledBorder("개인 시간표"));
        // 패널에 테이블 추가
        add(tableScrollPane, BorderLayout.CENTER);
    }

    /** DAO 데이터 로드 */
    public void loadScheduleData() {
    	clearTable();
    	
        ArrayList<Schedule> schedules = SDAO.getInstance().getSchedules(u.getID());
        for (Schedule s : schedules) {
            // startOfWeek <= x <= endOfWeek
            if (isDateInCurrentWeek(s.getStartAt().toLocalDate())) addScheduleToTable(s);
        }
    }
    
    // today가 요번주에 포함되는지 검사
    // 포함에면 t, 아니면 f
    private static boolean isDateInCurrentWeek(LocalDate date) {
        LocalDate today = LocalDate.now();

        // 이번 주의 첫 번째 날 (월요일) 구하기
        // previousOrSame(MONDAY)는 오늘이 월요일이면 오늘, 아니면 지난 월요일을 반환
        LocalDate firstDayOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        // 이번 주의 마지막 날 (일요일) 구하기
        // nextOrSame(SUNDAY)는 오늘이 일요일이면 오늘, 아니면 다음 일요일을 반환
        LocalDate lastDayOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        // 확인할 날짜가 시작일(포함)과 마지막 날(포함) 사이에 있는지 확인
        return !(date.isBefore(firstDayOfWeek) || date.isAfter(lastDayOfWeek));
    }

    /** 일정 1개를 시간표 테이블에 삽입 */
    private void addScheduleToTable(Schedule schedule) {
        //System.out.println(schedule.getScheduleId() + ", " + schedule.getScheduleDescription() + ", " + schedule.getStartAt().toLocalDate() + ", " + schedule.getStartAt());
        LocalDateTime start = schedule.getStartAt();
        LocalDateTime end = schedule.getEndAt();
        
        int col = dayOfWeekToColumn(start.getDayOfWeek());
        int startRow = start.getHour() - START_HOUR;
        int endRow = end.getHour() - START_HOUR;
        
        if(end.getMinute() == 30 && endRow > startRow) {
        	endRow--;
        }
        
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
        for (int row = 0; row < (END_HOUR - START_HOUR + 1); row++) {
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
                c.setBackground(getColorForSchedule(s.getScheduleDescription()));
                setToolTipText(s.getScheduleDescription());
            }
            else{
            	setToolTipText(null);
            }

            return c;
        }
    }

    /** 일정 내용 기반 해시 색상 생성 */
    // 렌덤으로 0 ~ 255 
    private Color getColorForSchedule(String str) {
        int hash = Math.abs(str.hashCode());
        int r = Math.abs((hash * 37) % 200 + 30);
        int g = Math.abs((hash * 67) % 200 + 30);
        int b = Math.abs((hash * 97) % 200 + 30);

        return new Color(r, g, b, 60); // 약간 투명하게
    }
}
