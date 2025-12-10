package GUI.Dialog;

import DB.Schedule;
import DB.User;
import DB.Group;
import DB.SDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;
import java.time.*;

public class ScheduleDialog extends JDialog implements ActionListener {
	// 컴포넌트 필드 선언
	JTextField startYearField, startMonthField, startDateField;	// 시작일자(년, 월, 일)
	JTextField endYearField, endMonthField, endDateField;	// 종료일자(년, 월, 일)
	JTextField startHourField, startMinuteField, endHourField, endMinuteField;	// 시작시간(시, 분), 종료시간(시, 분)
	JTextField typeField, descriptionField;	// 카테고리, 내용
	JCheckBox weeklyCheckBox, monthlyCheckBox;	// 주 단위, 월 단위 반복 체크박스
	JButton checkButton;	// 확인 버튼
	
	User user = null;
	Group group = null;
	Schedule schedule = null;
	
	int type = -1;	// 실행 타입(0: 개인 일정 추가, 1: 개인 일정 수정, 2: 그룹 일정 추가, 3: 그룹 일정 수정)
	
	// 개인 일정 추가 생성자(메뉴)
	public ScheduleDialog(JFrame frame, String title, User _u) {
		super(frame, title, true);
		user = _u;
		type = 0;
		
		initComponents();
		setVisible(true);
	}
	
	// 개인 일정 추가 생성자(캘린더)
	public ScheduleDialog(JFrame frame, String title, User _u, LocalDate date) {
		super(frame, title, true);
		user = _u;
		type = 0;
		
		initComponents();
		
		// 시작 시간(년, 월, 일) 자동 완성
        startYearField.setText(String.valueOf(date.getYear())); 
        startMonthField.setText(String.valueOf(date.getMonthValue()));
        startDateField.setText(String.valueOf(date.getDayOfMonth()));
        
        // 종료 시간(년, 월, 일) 자동 완성
        endYearField.setText(String.valueOf(date.getYear())); 
        endMonthField.setText(String.valueOf(date.getMonthValue()));
        endDateField.setText(String.valueOf(date.getDayOfMonth()));
		
		setVisible(true);
	}
	
	// 개인 일정 추가 생성자(시간표)
	public ScheduleDialog(JFrame frame, String title, User _u, LocalDateTime dateTime) {
		super(frame, title, true);
		user = _u;
		type = 0;
		
		initComponents();
		
		// 시작 시간(년, 월, 일, 시, 분) 자동 완성
        startYearField.setText(String.valueOf(dateTime.getYear())); 
        startMonthField.setText(String.valueOf(dateTime.getMonthValue()));
        startDateField.setText(String.valueOf(dateTime.getDayOfMonth()));
        startHourField.setText(String.valueOf(dateTime.getHour()));
        startMinuteField.setText(String.valueOf(dateTime.getMinute()));
        
        // 종료 시간(년, 월, 일) 자동 완성
        endYearField.setText(String.valueOf(dateTime.getYear())); 
        endMonthField.setText(String.valueOf(dateTime.getMonthValue()));
        endDateField.setText(String.valueOf(dateTime.getDayOfMonth()));
		
		setVisible(true);
	}
	
	// 개인 일정 수정 생성자
	public ScheduleDialog(Frame frame, String title, User _u, Schedule _s) {
		super(frame, title, true);
		user = _u;
		schedule = _s;
		type = 1;
		
		initComponents();
		
		// 시작 시간(년, 월, 일, 시, 분) 자동 완성
        startYearField.setText(String.valueOf(schedule.getStartAt().getYear()));
        startMonthField.setText(String.valueOf(schedule.getStartAt().getMonthValue()));
        startDateField.setText(String.valueOf(schedule.getStartAt().getDayOfMonth()));
        startHourField.setText(String.valueOf(schedule.getStartAt().getHour()));
        startMinuteField.setText(String.valueOf(schedule.getStartAt().getMinute()));
        
        // 종료 시간(년, 월, 일, 시, 분) 자동 완성
        endYearField.setText(String.valueOf(schedule.getEndAt().getYear()));
        endMonthField.setText(String.valueOf(schedule.getEndAt().getMonthValue()));
        endDateField.setText(String.valueOf(schedule.getEndAt().getDayOfMonth()));
        endHourField.setText(String.valueOf(schedule.getEndAt().getHour()));
        endMinuteField.setText(String.valueOf(schedule.getEndAt().getMinute()));
        
        // 구분 자동 완성
        typeField.setText(schedule.getScheduleType());

        // 내용 자동 완성
        descriptionField.setText(schedule.getScheduleDescription());
        
		//setVisible(true);
	}
	
	// 그룹 일정 추가 생성자(버튼)
	public ScheduleDialog(Frame frame, String title, User _u, Group _g) {
		super(frame, title, true);
		user = _u;
		group = _g;
		type = 2;
		
		initComponents();
		setVisible(true);
	}
	
	// 그룹 일정 추가 생성자(시간표)
	public ScheduleDialog(Frame frame, String title, User _u, Group _g, LocalDateTime dateTime) {
		super(frame, title, true);
		user = _u;
		group = _g;
		type = 2;
		
		initComponents();
		
		// 시작 시간(년, 월, 일, 시, 분) 자동 완성
        startYearField.setText(String.valueOf(dateTime.getYear())); 
        startMonthField.setText(String.valueOf(dateTime.getMonthValue()));
        startDateField.setText(String.valueOf(dateTime.getDayOfMonth()));
        startHourField.setText(String.valueOf(dateTime.getHour()));
        startMinuteField.setText(String.valueOf(dateTime.getMinute()));
        
        // 종료 시간(년, 월, 일) 자동 완성
        endYearField.setText(String.valueOf(dateTime.getYear())); 
        endMonthField.setText(String.valueOf(dateTime.getMonthValue()));
        endDateField.setText(String.valueOf(dateTime.getDayOfMonth()));
		
		setVisible(true);
	}
	
	// 그룹 일정 수정 생성자
	public ScheduleDialog(Frame frame, String title, User _u, Group _g, Schedule _s) {
		super(frame, title, true);
		user = _u;
		group = _g;
		schedule = _s;
		type = 3;
		
		initComponents();
		
		// 시작 시간(년, 월, 일, 시, 분) 자동 완성
        startYearField.setText(String.valueOf(schedule.getStartAt().getYear()));
        startMonthField.setText(String.valueOf(schedule.getStartAt().getMonthValue()));
        startDateField.setText(String.valueOf(schedule.getStartAt().getDayOfMonth()));
        startHourField.setText(String.valueOf(schedule.getStartAt().getHour()));
        startMinuteField.setText(String.valueOf(schedule.getStartAt().getMinute()));
        
        // 종료 시간(년, 월, 일, 시, 분) 자동 완성
        endYearField.setText(String.valueOf(schedule.getEndAt().getYear()));
        endMonthField.setText(String.valueOf(schedule.getEndAt().getMonthValue()));
        endDateField.setText(String.valueOf(schedule.getEndAt().getDayOfMonth()));
        endHourField.setText(String.valueOf(schedule.getEndAt().getHour()));
        endMinuteField.setText(String.valueOf(schedule.getEndAt().getMinute()));
        
        // 구분 자동 완성
        typeField.setText(schedule.getScheduleType());

        // 내용 자동 완성
        descriptionField.setText(schedule.getScheduleDescription());
		
		//setVisible(true);
	}
	
	// 컴포넌트 배치 메소드
    private void initComponents() {
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
        typeField = new JTextField(10);

        JPanel categoryPanel = new JPanel();
        categoryPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        categoryPanel.add(typeField);

        // 내용 패널
        descriptionField = new JTextField(20);
        descriptionField.addActionListener(this);

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
        checkButton.setBackground(Color.WHITE); // 버튼 배경색
        checkButton.setFocusPainted(false); // 버튼 선택 시 텍스트 주변 네모박스
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
    }
	
    // ActionListener 메소드 구현
    @Override
	public void actionPerformed(ActionEvent e) {
		// 입력 유효성 검사	
		int startYear, startMonth, startDate, startHour, startMinute, endYear, endMonth, endDate, endHour, endMinute;
		
		try {
			startYear = Integer.parseInt(startYearField.getText());
			startMonth = Integer.parseInt(startMonthField.getText());
			startDate = Integer.parseInt(startDateField.getText());
			startHour = Integer.parseInt(startHourField.getText());
			startMinute = Integer.parseInt(startMinuteField.getText());
			endYear = Integer.parseInt(endYearField.getText());
			endMonth = Integer.parseInt(endMonthField.getText());
			endDate = Integer.parseInt(endDateField.getText());
			endHour = Integer.parseInt(endHourField.getText());
			endMinute = Integer.parseInt(endMinuteField.getText());
		}
		catch(Exception exception) {
			JOptionPane.showMessageDialog(null, "날짜, 시간 입력을 확인해주세요.", "Warning", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		// LocateDateTime 객체 생성
		LocalDateTime startDateTime = LocalDateTime.of(startYear, startMonth, startDate, startHour, startMinute);
		LocalDateTime endDateTime = LocalDateTime.of(endYear, endMonth, endDate, endHour, endMinute);
		
		// 구분 및 내용 저장
		String type_ = typeField.getText().trim();
		String description = descriptionField.getText().trim();
		
		// 입력 유효성 검사
		if(type_.equals("")) {
			JOptionPane.showMessageDialog(null, "구분 입력을 확인해주세요.", "Warning", JOptionPane.WARNING_MESSAGE);
			return;
		}
		else if(description.equals("")) {
			JOptionPane.showMessageDialog(null, "내용 입력을 다시 확인해주세요.", "Warning", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		boolean result;
		Schedule newSchedule = null;
		
		// 일정 추가
		if(type == 0 || type == 2) {
			if(type == 0) newSchedule = new Schedule(user.getID(), description, type_, startDateTime, endDateTime);	// 일정 객체 생성
			else if(type == 2) newSchedule = new Schedule(user.getID(), group.getGroupId(), description, type_, startDateTime, endDateTime);
			
			result = SDAO.getInstance().insertSchedule(newSchedule);	// 일정 객체를 인수로 DAO 객체에서 일정 추가 성공 여부 반환
			
			// 일정 추가 성공
			if(result) {
				JOptionPane.showMessageDialog(null, "일정 추가 성공", "Information", JOptionPane.PLAIN_MESSAGE);
				dispose();
			}
			// 일정 추가 실패
			else {
				JOptionPane.showMessageDialog(null, "일정 추가 실패. 시간을 다시 확인해주세요.", "Warning", JOptionPane.WARNING_MESSAGE);
			}
		}
		// 일정 수정
		else if(type == 1 || type == 3) {
			if(type == 1) newSchedule = new Schedule(schedule.getScheduleId(), user.getID(), description, type_, startDateTime, endDateTime);	// 일정 객체 생성
			else if(type == 3) newSchedule = new Schedule(schedule.getScheduleId(), user.getID(), group.getGroupId(), description, type_, startDateTime, endDateTime);
			
			result = SDAO.getInstance().updateSchedule(newSchedule);	// 일정 객체를 인수로 DAO 객체에서 일정 수정 성공 여부 반환
			
			// 일정 수정 성공
			if(result) {
				JOptionPane.showMessageDialog(null, "일정 수정 성공", "Information", JOptionPane.PLAIN_MESSAGE);
				dispose();
			}
			// 일정 수정 실패
			else {
				JOptionPane.showMessageDialog(null, "일정 수정 실패. 시간을 다시 확인해주세요.", "Warning", JOptionPane.WARNING_MESSAGE);
			}
		}
	}
}