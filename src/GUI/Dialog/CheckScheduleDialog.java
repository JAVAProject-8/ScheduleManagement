package GUI.Dialog;

import DB.User;
import DB.Schedule;
import DB.SDAO;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CheckScheduleDialog extends JDialog implements ActionListener {	
	JComboBox<String> optionComboBox;	// 옵션 선택
	JTextField startYearField, startMonthField, startDateField;	// 시작일자(년, 월, 일)
	JTextField endYearField, endMonthField, endDateField;	// 종료일자(년, 월, 일)
	JTextField inputField;	// 입력
	JTable scheduleTable;	// 일정 테이블
	JButton searchButton, editButton, deleteButton;	// 검색, 수정, 삭제 버튼
	
	TableRowSorter<DefaultTableModel> sorter;	// 테이블 행 검색(필터)
	DefaultTableModel tableModel;
	ArrayList<Schedule> personalSchedules = null;
	
	User user = null;
	
	public CheckScheduleDialog(JFrame frame, String title, User _u) {
		super(frame, title, true);
		user = _u;
		
		String option[] = {"구분", "내용"};
		optionComboBox = new JComboBox<>(option);
		optionComboBox.setBackground(Color.WHITE);
		
		inputField = new JTextField(25);
		
		searchButton = new JButton("검색");
		searchButton.setBackground(Color.WHITE);
		searchButton.setFocusPainted(false);
		
		// 검색 상단 패널
		JPanel topPanel = new JPanel();
		topPanel.add(optionComboBox);
		topPanel.add(inputField);
		topPanel.add(searchButton);
		
		// 시작일자 패널
		startYearField = new JTextField(4);
		startMonthField = new JTextField(2);
		startDateField = new JTextField(2);
		
		JPanel startDatePanel = new JPanel();
		startDatePanel.add(startYearField);
		startDatePanel.add(new JLabel("년"));
		startDatePanel.add(startMonthField);
		startDatePanel.add(new JLabel("월"));
		startDatePanel.add(startDateField);
		startDatePanel.add(new JLabel("일"));
		
		// 종료일자 패널
		endYearField = new JTextField(4);
		endMonthField = new JTextField(2);
		endDateField = new JTextField(2);
		
		JPanel endDatePanel = new JPanel();
		endDatePanel.add(endYearField);
		endDatePanel.add(new JLabel("년"));
		endDatePanel.add(endMonthField);
		endDatePanel.add(new JLabel("월"));
		endDatePanel.add(endDateField);
		endDatePanel.add(new JLabel("일"));
		
		// 검색 하단 패널
		JPanel bottomPanel = new JPanel();
		bottomPanel.add(startDatePanel);
		bottomPanel.add(new JLabel(" ~ "));
		bottomPanel.add(endDatePanel);
		
		// 검색 패널
		JPanel searchPanel = new JPanel();
		searchPanel.setLayout(new BorderLayout());
		searchPanel.add(topPanel, BorderLayout.NORTH);
		searchPanel.add(bottomPanel, BorderLayout.SOUTH);
		searchPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
		
		tableModel = new DefaultTableModel() {
			public boolean isCellEditable(int r, int c) {	// 셀 수정 비활성화 처리
				return false;
			}
		};
		tableModel.addColumn("구분");
		tableModel.addColumn("내용");
		tableModel.addColumn("시작");
		tableModel.addColumn("종료");
		
		refreshScheduleTable();	// 테이블 갱신
		
		// 테이블 설정
		scheduleTable = new JTable(tableModel);
		scheduleTable.setRowHeight(20);	// 행 높이 설정
		scheduleTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);	// 테이블 자동 크기 조절 끄기
		scheduleTable.getColumnModel().getColumn(0).setPreferredWidth(60);	// 첫 번째 열 너비 설정
		scheduleTable.getColumnModel().getColumn(1).setPreferredWidth(135);	// 두 번째 열 너비 설정
		scheduleTable.getColumnModel().getColumn(2).setPreferredWidth(180);	// 세 번째 열 너비 설정
		scheduleTable.getColumnModel().getColumn(3).setPreferredWidth(180);	// 네 번째 열 너비 설정
		
		scheduleTable.getTableHeader().setReorderingAllowed(false);	// 열 순서 변경 금지 처리
		scheduleTable.getTableHeader().setResizingAllowed(false);	// 열 너비 변경 금지 처리
		
		sorter = new TableRowSorter<>(tableModel);
		scheduleTable.setRowSorter(sorter);
		
		// 스크롤 패널 등록
		JScrollPane scheduleScroll = new JScrollPane(scheduleTable);
		scheduleScroll.setPreferredSize(new Dimension(560, 250));
		
		// 버튼 패널
		editButton = new JButton("수정");
		editButton.setBackground(Color.WHITE);
		editButton.setFocusPainted(false);
		
		deleteButton = new JButton("삭제");
		deleteButton.setBackground(Color.WHITE);
		deleteButton.setFocusPainted(false);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(editButton);
		buttonPanel.add(new JLabel("\t\t\t\t"));
		buttonPanel.add(deleteButton);
		buttonPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
		
		add(searchPanel, BorderLayout.NORTH);
		add(scheduleScroll, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
		
		inputField.addActionListener(this);
		searchButton.addActionListener(this);
		editButton.addActionListener(this);
		deleteButton.addActionListener(this);
		
		inputField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				search();
			}
			
			@Override
            public void removeUpdate(DocumentEvent e) {
                search();
            }
			
			@Override
            public void changedUpdate(DocumentEvent e) {
                search();
            }
		});
		
		pack();	// 크기 자동 맞춤
		setResizable(false);	// 크기 조정
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);	// 종료 시 처리
		setLocationRelativeTo(null);	// 모니터 중앙 표시
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		
		// 검색
		if(obj == inputField || obj == searchButton) {
			search();
		}
		// 일정 수정
		else if(obj == editButton) {
			int selectedIndex = scheduleTable.getSelectedRow();	// 선택한 행의 인덱스를 가져옴
			
			// 선택된 행이 있다면
			if(selectedIndex != -1) {
				int selectedModelIndex = scheduleTable.convertRowIndexToModel(selectedIndex);	// 테이블 모델의 인덱스로 변환
				Schedule selectedSchedule = personalSchedules.get(selectedModelIndex);
				
				Window mainFrame = SwingUtilities.getWindowAncestor(this);
				ScheduleDialog scheduleDialog = new ScheduleDialog((JFrame)mainFrame, "일정 수정", user, selectedSchedule);
				scheduleDialog.setVisible(true);	// Modal이 설정되어 있으므로 Dialog dispose()시까지 스레드 대기
				
				refreshScheduleTable();	// 테이블 갱신
			}
			// 선택된 행이 없다면
			else {
				JOptionPane.showMessageDialog(null, "행을 선택해주세요.", "Information", JOptionPane.INFORMATION_MESSAGE);
			}
		}
		// 일정 삭제
		else if(obj == deleteButton) {
			int selectedIndex = scheduleTable.getSelectedRow();	// 선택한 행의 인덱스를 가져옴
			
			// 선택된 행이 있다면
			if(selectedIndex != -1) {
				int selectedModelIndex = scheduleTable.convertRowIndexToModel(selectedIndex);	// 테이블 모델의 인덱스로 변환
				Schedule selectedSchedule = personalSchedules.get(selectedModelIndex);
				
				// 삭제 여부 재확인
				int var = JOptionPane.showConfirmDialog(null, "정말 삭제하시겠습니까?", "삭제", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
				
				if(var == JOptionPane.YES_OPTION) {
					boolean result = SDAO.getInstance().deleteSchedule(selectedSchedule.getScheduleId());	// 일정 아이디를 인수로 DAO 객체에서 일정 삭제 여부 반환
					
					// 삭제 성공
					if(result) {
						JOptionPane.showMessageDialog(null, "일정 삭제 성공", "Information", JOptionPane.PLAIN_MESSAGE);
						refreshScheduleTable();	// 테이블 갱신
					}
					// 삭제 실패
					else {
						JOptionPane.showMessageDialog(null, "일정 삭제 실패. 다시 시도해 주세요.", "Warning", JOptionPane.WARNING_MESSAGE);
					}
				}
			}
			// 선택된 행이 없다면
			else {
				JOptionPane.showMessageDialog(null, "행을 선택해주세요.", "Information", JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}
	
	public void search() {
		ArrayList<RowFilter<Object, Object>> filters = new ArrayList<>();
		
		// 검색 옵션 설정
		String inputText = inputField.getText().trim();	// 검색어 저장
		
		if(!inputText.equals("")) {		// 입력 내용이 있는 경우
			String option = optionComboBox.getSelectedItem().toString();	// 선택한 옵션을 가져옴
			int optionIndex = -1;
			 
			if(option.equals("구분")) {optionIndex = 0;}	// 옵션에 따라 인덱스를 저장
			else if(option.equals("내용")) {optionIndex = 1;}
			 
			filters.add(RowFilter.regexFilter(inputText, optionIndex));	// 필터에 추가
		}
		
		// 검색 기간 설정(Gemini 참조)
		String startDate = startYearField.getText().trim() + startMonthField.getText().trim() + startDateField.getText().trim();	// 사용자가 입력한 시작일
		String endDate = endYearField.getText().trim() + endMonthField.getText().trim() + endDateField.getText().trim();	// 사용자가 입력한 종료일
		
		filters.add(new RowFilter<Object, Object>() {
			@Override
			public boolean include(Entry<?, ?> entry) {
				String rawStartDate = entry.getStringValue(2).toString().replaceAll("[^0-9]", "").substring(0, 8);	// 인덱스로 시작일을 가져와 10글자로 가공("yyyy-MM-dd")
				String rawEndDate = entry.getStringValue(3).toString().replaceAll("[^0-9]", "").substring(0, 8);	// 인덱스로 종료일을 가져와 10글자로 가공

				String checkStart;
				if(startDate.length() == 8) checkStart = startDate;
				else checkStart = "00000101";
				
				String checkEnd;
				if(endDate.length() == 8) checkEnd = endDate;
				else checkEnd = "99991231";
				
				boolean condition1 = rawEndDate.compareTo(checkStart) >= 0;	// 일정 종료일이 검색 시작일보다 같거나 큰지
				boolean condition2 = rawStartDate.compareTo(checkEnd) <= 0;	// 일정 시작일이 검색 종료일보다 같거나 작은지
				
				return condition1 && condition2;	// 두 값이 모두 True 일 시 해당 행을 화면에 출력
			}
		});
			
		// 검색(필터링)
		if(filters.size() == 0) {		// 저장된 필터가 없는 경우
			sorter.setRowFilter(null);	// 모든 행을 출력
		}
		else {							// 저장된 필터가 있는 경우
			sorter.setRowFilter(RowFilter.andFilter(filters));	// 필터에 맞추어 행을 출력
		}
	}
	
	public void refreshScheduleTable() {
		tableModel.setRowCount(0);	// 테이블 모델 초기화
		
		personalSchedules = SDAO.getInstance().getSchedules(user.getID());	// 사용자 아이디를 인수로 DAO 객체에서 일정 ArrayList 반환 
		
		// DB에서 전달받은 ArrayList를 시작 시간을 기준으로 하여 정렬
		Collections.sort(personalSchedules, new Comparator<Schedule>() {
			@Override
			public int compare(Schedule s1, Schedule s2) {
				return s1.getStartAt().compareTo(s2.getStartAt());
			}
		});
		
		// 정렬한 ArrayList를 테이블 모델에 추가
		for(int i = 0; i < personalSchedules.size(); i++) {
			Object[] data = new Object[4];
			
			data[0] = personalSchedules.get(i).getScheduleType();
			data[1] = personalSchedules.get(i).getScheduleDescription();
			data[2] = personalSchedules.get(i).getStartAt().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분"));
			data[3] = personalSchedules.get(i).getEndAt().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분"));
			
			tableModel.addRow(data);
		}
	}
}