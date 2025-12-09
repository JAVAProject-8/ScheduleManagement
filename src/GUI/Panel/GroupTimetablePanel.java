package GUI.Panel;

import javax.swing.*;
import javax.swing.table.*;

import DB.Group;
import DB.SDAO;
import DB.Schedule;
import DB.User;
import GUI.Dialog.ScheduleDialog;
import DB.Member;

import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

public class GroupTimetablePanel extends JPanel implements ActionListener {
    private JComboBox<String> groupComboBox;                // 콤보박스
    private DefaultComboBoxModel<String> comboBoxModel;     // 기본 콤보박스 모델
    private JTable table;                                   // 테이블
    private DefaultTableModel model;                        // 기본 테이블 모델
    private User u = null;                                  // 사용자 객체
    // 테이블 속성명
    private final String[] DAYS = { "시간", "월", "화", "수", "목", "금", "토", "일" };
    
    private final int START_HOUR = 9;       // 시작 시간
    private final int END_HOUR = 23;        // 종료 시간

    private JPanel topPanel;                // 탑 패널
    private JScrollPane tableScrollPane;    // 스크롤
    
    // 사용자 별 색상
    private Map<String, Color> memberColorMap = new HashMap<>();
    private ArrayList<Group> groups = null;  // 그룹들
    
    private Group selectedGroup = null;    // 그룹
    
    // 패널 초기화
    public GroupTimetablePanel(User u) {
        setLayout(new BorderLayout());
        this.u = u;
        
        initComponent();
        refreshGroupList();
    }
    
    // 선택 콤보박스 초기화
    private void initComponent() {
    	// 상단 패널 - 그룹 선택 콤보박스
    	JLabel informationLabel = new JLabel("그룹 선택: ");
    	informationLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 20));
    	
    	comboBoxModel = new DefaultComboBoxModel<>();
        groupComboBox = new JComboBox<>(comboBoxModel);
        groupComboBox.setBackground(Color.WHITE);
		groupComboBox.setFont(new Font("Malgun Gothic", Font.BOLD, 20));
        
        topPanel = new JPanel();
        topPanel.add(informationLabel);
        topPanel.add(groupComboBox);
        
        initTable();		// 시간표 테이블 초기화
        
        // 그룹 선택 이벤트
        groupComboBox.addActionListener(this);
    }
    
    // 그룹 목록 새로 고침
    public void refreshGroupList() {
    	groups = SDAO.getInstance().getMyGroups(u.getID());	// 사용자 아이디를 인수로 DAO 객체에서 그룹 ArrayList 반환
		
		this.removeAll();					// 모든 컴포넌트를 제거
		comboBoxModel.removeAllElements();	// 모든 원소를 제거
		
		if(groups.size() == 0) {	// 사용자가 가입되어있는 그룹이 없는 경우
			JLabel impormationLabel = new JLabel("초대 코드를 입력하여 그룹에 가입해주세요.", JLabel.CENTER);
			impormationLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 30));
            // 라벨 추가
			add(impormationLabel, BorderLayout.CENTER);
			selectedGroup = null;
			
			this.revalidate();	// 배치 정보 재계산
			this.repaint();		// 재배치
			
			return;
		}

		add(topPanel, BorderLayout.NORTH);          // 콤보박스 상단 패널 추가
        add(tableScrollPane, BorderLayout.CENTER);	// 패널에 테이블 추가
		
        groupComboBox.removeActionListener(this);
        
        loadGroupList();	// Mock DAO로 그룹 목록 불러오기
		
		groupComboBox.setSelectedIndex(0);
		selectedGroup = groups.get(0);
		
		groupComboBox.addActionListener(this);
		loadSchedulesForGroup();
		
		this.revalidate();	// 배치 정보 재계산
		this.repaint();		// 재배치
    }
    
    // 액션 이벤트
    @Override
    public void actionPerformed(ActionEvent e) {
    	int selectedIndex = groupComboBox.getSelectedIndex();
    	
    	if(selectedIndex != -1) {
	    	selectedGroup = groups.get(selectedIndex);
	    	loadSchedulesForGroup();
    	}
    }

    // 테이블 초기화
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
        GroupScheduleRenderer scheduleRenderer = new GroupScheduleRenderer();
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

        // 테이블 마우스 클릭 이벤드
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
        				
        				if(selectedSchedule.getGroupId() != null) {
        					ScheduleDialog scheduleDialog = new ScheduleDialog(null, "일정 수정", u, selectedGroup, selectedSchedule);	// 일정 수정
            				scheduleDialog.setVisible(true);
        				}
        			}
        			else {
            			LocalDate selectedDate = mondayDate.plusDays(col - 1);			// 선택일 계산
            			LocalTime selectedTime = LocalTime.of(START_HOUR + row, 30);	// 선택 시간 계산
            			LocalDateTime selectedDateTime = LocalDateTime.of(selectedDate, selectedTime);
            			
            			new ScheduleDialog(null, "일정 추가", u, selectedGroup, selectedDateTime);
        			}
        			
        			loadSchedulesForGroup();
        		}
        	}
        });
        
        tableScrollPane = new JScrollPane(table);
        tableScrollPane.setBorder(new TitledBorder("그룹 시간표"));
    }

    // DAO로 그룹 목록 가져오기
    private void loadGroupList() {
        for (Group g : groups) {
        	comboBoxModel.addElement(g.getGroupName());
        }
    }

    // 특정 그룹의 전체 일정 로드
    public void loadSchedulesForGroup() {
        // 테이블의 기존 일정을 모두 초기화함
        clearTable();
        
        // 먼저 그룹 일정을 시간표에 표시
        ArrayList<Schedule> groupSchedules = SDAO.getInstance().getGroupSchedules(selectedGroup.getGroupId());
        
        for(Schedule s : groupSchedules) {
        	if (isDateInCurrentWeek(s.getStartAt().toLocalDate())) {
                addScheduleToTable(s);
            }
        }
        
        // 선택된 그룹 기준으로 그룹에 속한 멤버 목록 조회
        ArrayList<Member> members = SDAO.getInstance().getMembersByGroupId(selectedGroup.getGroupId());
        
        // 조회된 그룹원 각각의 일정 조회
        for (Member m : members) {
            // 해당 유저가 선택된 그룹에서 작성한 일정만 조회
            ArrayList<Schedule> schedules = SDAO.getInstance().getSchedules(m.getUserId());

            // 조회된 일정들을 테이블에 배치
            for (Schedule s : schedules) {
                // 이번 주에 포함되는 일정인지 필터링
                if (isDateInCurrentWeek(s.getStartAt().toLocalDate())) {
                    // 필터 통과한 일정을 테이블에 실제 배치
                    addScheduleToTable(s);
                }
            }
        }

        // UI 강제 갱신
        table.repaint();
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

    // 일정 1개를 시간표 테이블에 추가
    private void addScheduleToTable(Schedule schedule) {
        LocalDateTime start = schedule.getStartAt();
        LocalDateTime end = schedule.getEndAt();

        int col = dayOfWeekToColumn(start.getDayOfWeek());
        int startRow = start.getHour() - START_HOUR;
        int endRow = end.getHour() - START_HOUR;
        
        if(end.getMinute() == 30 && endRow > startRow) {
        	endRow--;
        }
        
        // 범위 벗어나면 무시
        if (col < 1 || col > 7) return;
        if (startRow < 0 || startRow >= model.getRowCount()) return;
        if (endRow < 0) endRow = startRow;

        // 일정 시간 블록 채우기
        for (int row = startRow; row <= endRow; row++) {
            model.setValueAt(schedule, row, col);
        }
    }

    // 시간표 초기화 
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

    // 테이블에 일정 색상 렌더러
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
                User findUser = SDAO.getInstance().getUserInfo(s.getWriterId());
                
                if(s.getGroupId() != null) {	// 그룹 일정이라면
                	setText(s.getScheduleDescription());
                	c.setBackground(getColorForSchedule(s.getGroupId()));
                	setToolTipText(s.getScheduleDescription());
                }
                else {
                	setText(findUser.getName() + " 개인일정");
                	c.setBackground(getColorForSchedule(s.getWriterId()));
                	setToolTipText(findUser.getName() + " 개인일정");
                }
            }
            else {
            	setToolTipText(null);
            }

            return c;
        }
    }

    // 사용자 ID으로 해시 색상 생성
    private Color getColorForSchedule(String str) {
        // memberColorMap에 색상이 있을 떄
        if (memberColorMap.containsKey(str)) {
            return memberColorMap.get(str);
        }

        int hash = Math.abs(str.hashCode());
        int r = Math.abs((hash * 37) % 200 + 30);
        int g = Math.abs((hash * 67) % 200 + 30);
        int b = Math.abs((hash * 97) % 200 + 30);
        
        return new Color(r, g, b, 60); // 약간 투명하게
    }
}