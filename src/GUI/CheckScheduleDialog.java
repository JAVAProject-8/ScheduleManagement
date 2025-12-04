package GUI;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;
import java.util.ArrayList;

public class CheckScheduleDialog extends JDialog implements ActionListener {	
	JComboBox<String> optionComboBox;	// 옵션 선택
	JTextField startYearField, startMonthField, startDateField;	// 시작일자(년, 월, 일)
	JTextField endYearField, endMonthField, endDateField;	// 종료일자(년, 월, 일)
	JTextField inputField;	// 입력
	JTable scheduleTable;	// 일정 테이블
	JButton searchButton, editButton, deleteButton;	// 검색, 수정, 삭제 버튼
	
	TableRowSorter<DefaultTableModel> sorter;	// 테이블 행 검색(필터)
	DefaultTableModel tableModel;
	
	public CheckScheduleDialog(JFrame frame, String title) {
		super(frame, title, true);
		
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
		
		tableModel = new DefaultTableModel();
		tableModel.addColumn("구분");
		tableModel.addColumn("내용");
		tableModel.addColumn("시작");
		tableModel.addColumn("종료");
		
		// 테스트 모델
		// DB에서 받은 일정 ArrayList를 바탕으로 테이블 모델을 생성하는 메소드 필요
		for(int i = 1; i <= 12; i++) {
			tableModel.addRow(new Object[] {"수업", i, "2025-01-01", "2025-12-31"});
		}
		
		// 테이블 설정
		scheduleTable = new JTable(tableModel);
		scheduleTable.setDragEnabled(false);
		sorter = new TableRowSorter<>(tableModel);
		scheduleTable.setRowSorter(sorter);
		//scheduleTable.setEnabled(false);
		
		// 스크롤 패널 등록
		JScrollPane scheduleScroll = new JScrollPane(scheduleTable);
		scheduleScroll.setPreferredSize(new Dimension(0, 250));
		//scheduleScroll.setBorder(new EmptyBorder(20, 20, 20, 20));
		
		// 버튼 패널
		editButton = new JButton("수정");
		editButton.setBackground(Color.WHITE);
		editButton.setFocusPainted(false);
		
		deleteButton = new JButton("삭제");
		deleteButton.setBackground(Color.WHITE);
		deleteButton.setFocusPainted(false);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(editButton);
		buttonPanel.add(new JLabel("                      "));
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
			// 테이블에서 선택한 행의 정보를 가져와서
			// 자동완성을 지원하는 별도의 JDIalog 필요
			// 입력 내용을 기반으로 일정 객체를 생성하여 DB에 전달
			// DB에서 업데이트하고 성공 여부를 반환
			// 성공 여부에 따라 JOptionPane 출력
		}
		// 일정 삭제
		else if(obj == deleteButton) {
			// 삭제 여부 재확인
			int var = JOptionPane.showConfirmDialog(null, "정말 삭제하시겠습니까?", "삭제", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
			
			if(var == JOptionPane.YES_OPTION) {
				// 테이블에서 선택한 행의 정보를 가져와서
				// 삭제할 일정의 id를 DB에 전달
				// DB에서 삭제하고 성공 여부를 반환
				// 성공 여부에 따라 JOptionPane 출력
			}
			else {
				return;
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
		String startDate = startYearField.getText().trim() + "-" + startMonthField.getText().trim() + "-" + startDateField.getText().trim();	// 사용자가 입력한 시작일
		String endDate = endYearField.getText().trim() + "-" + endMonthField.getText().trim() + "-" + endDateField.getText().trim();	// 사용자가 입력한 종료일
		if(startDate.length() == 10 || endDate.length() == 10) {	// 둘 중 하나라도 입력 되었다면
			filters.add(new RowFilter<Object, Object>(){	// RowFilter 클래스를 상속받는 익명 클래스 생성
				@Override
				public boolean include(Entry<?, ?> entry) {	// 메소드 재정의
					String rowStartDate = entry.getStringValue(2).substring(0, 10);	// 인덱스로 시작일을 가져와 10글자로 가공("yyyy-MM-dd")
					String rowEndDate = entry.getStringValue(3).substring(0, 10);	// 인덱스로 종료일을 가져와 10글자로 가공
					
					boolean isOverStart = (startDate.length() != 10) || (rowEndDate.compareTo(startDate) >= 0);	// 입력되지 않았거나, 일정 종료일이 검색 시작일 이후인지
					boolean isOverEnd = (endDate.length() != 10) || (rowStartDate.compareTo(endDate) <= 0);		// 입력되지 않았거나, 일정 시작일이 검색 종료일 이전인지
					
					return isOverStart && isOverEnd;	// 두 값이 모두 True 일 시 해당 행을 화면에 출력
				}
			});
		}
		
		// 검색(필터링)
		if(filters.size() == 0) {		// 저장된 필터가 없는 경우
			sorter.setRowFilter(null);	// 모든 행을 출력
		}
		else {							// 저장된 필터가 있는 경우
			sorter.setRowFilter(RowFilter.andFilter(filters));	// 필터에 맞추어 행을 출력
		}
	}
}