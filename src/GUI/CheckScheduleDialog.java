package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;

public class CheckScheduleDialog extends JDialog implements ActionListener {	
	JComboBox<String> optionComboBox;	// 옵션 선택
	JTextField startYearField, startMonthField, startDateField;	// 시작일자(년, 월, 일)
	JTextField endYearField, endMonthField, endDateField;	// 종료일자(년, 월, 일)
	JTextField inputField;	// 입력
	JTable scheduleTable;	// 일정 테이블
	JButton searchButton, editButton, deleteButton;	// 검색, 수정, 삭제 버튼
	
	// 원소 검색을 위한 TableRowSorter 필요
	
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
		
		// DB에서 받은 일정 객체를 DefaultTableModel로 변경하고
		// 테이블 정보를 생성하는 메소드 필요
		
		String header[] = {"구분", "내용", "시작", "종료"};
		String testContents[][] = {
				{"수업", "1", "0000", "0001"},
				{"수업", "2", "0000", "0001"},
				{"수업", "3", "0000", "0001"},
				{"수업", "4", "0000", "0001"},
				{"수업", "5", "0000", "0001"},
				{"수업", "6", "0000", "0001"},
				{"수업", "7", "0000", "0001"},
				{"수업", "8", "0000", "0001"},
				{"수업", "9", "0000", "0001"},
				{"수업", "10", "0000", "0001"},
				{"수업", "11", "0000", "0001"},
				{"수업", "12", "0000", "0001"},
				{"수업", "13", "0000", "0001"},
				{"수업", "14", "0000", "0001"},
				{"수업", "15", "0000", "0001"},
				{"수업", "16", "0000", "0001"},
				{"수업", "17", "0000", "0001"},
				{"수업", "18", "0000", "0001"}
		};
		scheduleTable = new JTable(testContents, header);
		scheduleTable.setDragEnabled(false);
		//scheduleTable.setEnabled(false);
		
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
			
		}
		// 일정 수정
		else if(obj == editButton) {
			
		}
		// 일정 삭제
		else if(obj == deleteButton) {
			
		}
	}
}