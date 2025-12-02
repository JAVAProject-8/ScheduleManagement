package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;

public class AddScheduleDialog extends JDialog implements ActionListener {
	// 컴포넌트 필드 선언
	JTextField startYearField, startMonthField, startDateField;	// 시작일자(년, 월, 일)
	JTextField endYearField, endMonthField, endDateField;	// 종료일자(년, 월, 일)
	JTextField startHourField, startMinuteField, endHourField, endMinuteField;	// 시작시간(시, 분), 종료시간(시, 분)
	JTextField categoryField, descriptionField;	// 카테고리, 내용
	JCheckBox weeklyCheckBox, monthlyCheckBox;	// 주 단위, 월 단위 반복 체크박스
	JButton checkButton;	// 확인 버튼
	
	public AddScheduleDialog(JFrame frame, String title) {
		super(frame, title, true);
		setLayout(new BorderLayout(5, 5));
		
		// 라벨 패널
		JPanel labelsPanel = new JPanel();
		labelsPanel.setLayout(new GridLayout(4, 1, 0, 0));
		labelsPanel.add(new JLabel("시작", JLabel.RIGHT));
		labelsPanel.add(new JLabel("종료", JLabel.RIGHT));
		labelsPanel.add(new JLabel("구분", JLabel.RIGHT));
		labelsPanel.add(new JLabel("내용", JLabel.RIGHT));
		labelsPanel.setBorder(new EmptyBorder(10, 10, 0, 0));
		
		// 시작일자, 시간 패널
		startYearField = new JTextField(4);
		startMonthField = new JTextField(2);
		startDateField = new JTextField(2);
		startHourField = new JTextField(2);
		startMinuteField = new JTextField(2);
		
		JPanel startDatePanel = new JPanel();
		startDatePanel.add(startYearField);
		startDatePanel.add(new JLabel("년"));
		startDatePanel.add(startMonthField);
		startDatePanel.add(new JLabel("월"));
		startDatePanel.add(startDateField);
		startDatePanel.add(new JLabel("일"));
		startDatePanel.add(startHourField);
		startDatePanel.add(new JLabel("시"));
		startDatePanel.add(startMinuteField);
		startDatePanel.add(new JLabel("분"));
		
		// 종료일자, 시간 패널
		endYearField = new JTextField(4);
		endMonthField = new JTextField(2);
		endDateField = new JTextField(2);
		endHourField = new JTextField(2);
		endMinuteField = new JTextField(2);
		
		JPanel endDatePanel = new JPanel();
		endDatePanel.add(endYearField);
		endDatePanel.add(new JLabel("년"));
		endDatePanel.add(endMonthField);
		endDatePanel.add(new JLabel("월"));
		endDatePanel.add(endDateField);
		endDatePanel.add(new JLabel("일"));
		endDatePanel.add(endHourField);
		endDatePanel.add(new JLabel("시"));
		endDatePanel.add(endMinuteField);
		endDatePanel.add(new JLabel("분"));
		
		// 구분 패널
		categoryField = new JTextField(10);
		
		JPanel categoryPanel = new JPanel();
		categoryPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		categoryPanel.add(categoryField);
		
		// 내용 패널
		descriptionField = new JTextField(20);
		
		JPanel descriptionPanel = new JPanel();
		descriptionPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		descriptionPanel.add(descriptionField);
		
		// 필드 패널
		JPanel fieldsPanel = new JPanel();
		fieldsPanel.setLayout(new GridLayout(4, 1, 0, 0));
		fieldsPanel.add(startDatePanel);
		fieldsPanel.add(endDatePanel);
		fieldsPanel.add(categoryPanel);
		fieldsPanel.add(descriptionPanel);
		fieldsPanel.setBorder(new EmptyBorder(10, 0, 0, 10));
		
		// 체크박스
		weeklyCheckBox = new JCheckBox("주 단위 반복");
		weeklyCheckBox.setFocusPainted(false);
		
		monthlyCheckBox = new JCheckBox("월 단위 반복");
		monthlyCheckBox.setFocusPainted(false);
		
		// 버튼 그룹 지정
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(weeklyCheckBox);
		buttonGroup.add(monthlyCheckBox);
		
		checkButton = new JButton("확인");
		checkButton.setBackground(Color.WHITE);	// 버튼 배경색
		checkButton.setFocusPainted(false);	// 버튼 선택 시 텍스트 주변 네모박스
		checkButton.addActionListener(this);
		
		// 하단 패널
		JPanel bottomPanel = new JPanel();
		bottomPanel.add(weeklyCheckBox);
		bottomPanel.add(monthlyCheckBox);
		bottomPanel.add(checkButton);
		bottomPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
		
		// 모든 패널을 추가
		add(labelsPanel, BorderLayout.WEST);
		add(fieldsPanel, BorderLayout.EAST);
		add(bottomPanel, BorderLayout.SOUTH);
		
		pack();	// 크기 자동 맞춤
		setResizable(false);	// 크기 조정 불가
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);	// 종료 시 처리
		setLocationRelativeTo(null);	// 모니터 중앙 표시
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		// 입력 유효성 검사 필요
		// 필드에서 데이터를 가져와 String, LocateDateTime 변수에 담아 일정 객체 생성
		// 생성한 일정 객체를 전달
		// DB에 저장
		// 저장 성공 시 JOptionPanel 출력
	}
}
