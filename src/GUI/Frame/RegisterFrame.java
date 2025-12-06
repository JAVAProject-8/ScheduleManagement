package GUI.Frame;

import DB.SDAO;
import DB.User;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;
import java.time.*;

public class RegisterFrame extends JFrame implements ActionListener {
	JTextField IDField, nameField, organizationField; 					// 아이디, 이름, 소속
	JTextField birthYearField, birthMonthField, birthDateField;			// 생년월일
	JTextField phoneNumberField1, phoneNumberField2, phoneNumberField3;	// 전화번호
	JTextField emailLocalField, emailDomainField;		// 이메일 로컬, 도메인
	JComboBox<String> emailComboBox;					// 도메인 콤보 박스
	JPasswordField PWField, PWCheckField;				// 비밀번호, 비밀번호 확인
	JLabel PWStateLabel;								// 비밀번호 일치 여부 확인 라벨
	JButton checkButton, duplicationButton, exitButton;	// 중복검사, 확인 버튼
	
	LoginFrame loginFrame;
	boolean duplicationCheckFlag = false;	// 중복 검사 확인 플래그
			
	public RegisterFrame(LoginFrame _lf) {
		loginFrame = _lf;
		
		Container ct = getContentPane();
		ct.setLayout(new BorderLayout(0, 10));
		
		// 화면 상단 구성
		JLabel topLabel = new JLabel("회원가입", JLabel.CENTER);
		topLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 25));
		topLabel.setBorder(new EmptyBorder(30, 0, 20, 0));
		
		// 좌측 라벨 패널
		JPanel labelsPanel1 = new JPanel();
		labelsPanel1.setLayout(new GridLayout(5, 1, 0, 0));
		labelsPanel1.add(new JLabel("ID", JLabel.RIGHT));
		labelsPanel1.add(new JLabel("PW", JLabel.RIGHT));
		labelsPanel1.add(new JLabel("재입력", JLabel.RIGHT));
		labelsPanel1.add(new JLabel("이름", JLabel.RIGHT));
		labelsPanel1.add(new JLabel("소속", JLabel.RIGHT));
		
		// 아이디 입력 패널
		IDField = new JTextField(15);
		duplicationButton = new JButton("중복검사");
		duplicationButton.setBackground(Color.WHITE);
		duplicationButton.setFocusPainted(false);
		
		JPanel IDPanel = new JPanel();
		IDPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		IDPanel.add(IDField);
		IDPanel.add(duplicationButton);
		
		// 비밀번호 입력 패널
		PWField = new JPasswordField(15);
		JPanel PWPanel = new JPanel();
		PWPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		PWPanel.add(PWField);
		
		PWCheckField = new JPasswordField(15);
		PWStateLabel = new JLabel("", JLabel.LEFT);
		PWStateLabel.setPreferredSize(new Dimension(105, 20));
		
		// 비밀번호 확인 패널
		JPanel PWCheckPanel = new JPanel();
		PWCheckPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		PWCheckPanel.add(PWCheckField);
		PWCheckPanel.add(PWStateLabel);
		
		// 이름 입력 패널
		nameField = new JTextField(15);
		JPanel namePanel = new JPanel();
		namePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		namePanel.add(nameField);
		
		// 소속 입력 패널
		organizationField = new JTextField(15);
		JPanel organizationPanel = new JPanel();
		organizationPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		organizationPanel.add(organizationField);
		
		// 좌측 필드 패널
		JPanel fieldsPanel1 = new JPanel();
		fieldsPanel1.setLayout(new GridLayout(5, 1, 0, 0));
		fieldsPanel1.add(IDPanel);
		fieldsPanel1.add(PWPanel);
		fieldsPanel1.add(PWCheckPanel);
		fieldsPanel1.add(namePanel);
		fieldsPanel1.add(organizationPanel);
		
		// 좌측 라벨 + 필드 패널
		JPanel inputPanel1 = new JPanel();
		inputPanel1.setLayout(new BorderLayout(5, 5));
		inputPanel1.add(labelsPanel1, BorderLayout.WEST);	// 두 패널을 패널에 나란히 추가
		inputPanel1.add(fieldsPanel1, BorderLayout.EAST);
		inputPanel1.setBorder(new EmptyBorder(0, 0, 0, 10));
		
		// 좌측 라벨 패널
		JPanel labelsPanel2 = new JPanel();
		labelsPanel2.setLayout(new GridLayout(5, 1, 0, 0));
		labelsPanel2.add(new JLabel("생년월일", JLabel.RIGHT));
		labelsPanel2.add(new JLabel("전화번호", JLabel.RIGHT));
		labelsPanel2.add(new JLabel("이메일", JLabel.RIGHT));
		
		// 생일 입력 패널
		birthYearField = new JTextField(4);
		birthMonthField = new JTextField(2);
		birthDateField = new JTextField(2);
		
		JPanel birthPanel = new JPanel();
		birthPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		birthPanel.add(birthYearField);
		birthPanel.add(new JLabel("년"));
		birthPanel.add(birthMonthField);
		birthPanel.add(new JLabel("월"));
		birthPanel.add(birthDateField);
		birthPanel.add(new JLabel("일"));
		
		// 전화번호 입력 패널
		phoneNumberField1 = new JTextField(3);
		phoneNumberField2 = new JTextField(4);
		phoneNumberField3 = new JTextField(4);
		
		JPanel phoneNumberPanel = new JPanel();
		phoneNumberPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		phoneNumberPanel.add(phoneNumberField1);
		phoneNumberPanel.add(new JLabel("-"));
		phoneNumberPanel.add(phoneNumberField2);
		phoneNumberPanel.add(new JLabel("-"));
		phoneNumberPanel.add(phoneNumberField3);
		
		// 이메일 입력 패널
		emailLocalField = new JTextField(10);
		emailDomainField = new JTextField(10);
		emailDomainField.setEditable(false);
		emailDomainField.setBackground(Color.WHITE);
		emailDomainField.setText("naver.com");
		
		String domain[] = {"naver.com", "gmail.com", "daum.net", "sunmoon.ac.kr", "직접 입력"};
		emailComboBox = new JComboBox<>(domain);
		emailComboBox.setBackground(Color.WHITE);
		
		JPanel emailPanel = new JPanel();
		emailPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		emailPanel.add(emailLocalField);
		emailPanel.add(new JLabel("@"));
		emailPanel.add(emailDomainField);
		emailPanel.add(emailComboBox);
		
		// 버튼 패널
		checkButton = new JButton("확인");
		checkButton.setBackground(Color.WHITE);
		checkButton.setFocusPainted(false);
		
		exitButton = new JButton("뒤로가기");
		exitButton.setBackground(Color.WHITE);
		exitButton.setFocusPainted(false);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(checkButton);
		buttonPanel.add(new JLabel("          "));
		buttonPanel.add(exitButton);
		
		// 좌측 필드 패널
		JPanel fieldsPanel2 = new JPanel();
		fieldsPanel2.setLayout(new GridLayout(5, 1, 0, 0));
		fieldsPanel2.add(birthPanel);
		fieldsPanel2.add(phoneNumberPanel);
		fieldsPanel2.add(emailPanel);
		fieldsPanel2.add(new JLabel(""));
		fieldsPanel2.add(buttonPanel);
		
		// 우측 라벨 + 필드 패널
		JPanel inputPanel2 = new JPanel();
		inputPanel2.setLayout(new BorderLayout(5, 5));
		inputPanel2.add(labelsPanel2, BorderLayout.WEST);	// 두 패널을 패널에 나란히 추가
		inputPanel2.add(fieldsPanel2, BorderLayout.EAST);
		
		// 메인 패널
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(inputPanel1, BorderLayout.WEST);
		mainPanel.add(inputPanel2, BorderLayout.EAST);
		mainPanel.setBorder(new EmptyBorder(0, 20, 20, 20));
		
		ct.add(topLabel, BorderLayout.NORTH);
		ct.add(mainPanel, BorderLayout.CENTER);
		
		// ActionListener 등록
		duplicationButton.addActionListener(this);
		emailComboBox.addActionListener(this);
		checkButton.addActionListener(this);
		exitButton.addActionListener(this);
		
		// 익명 객체 사용
		IDField.getDocument().addDocumentListener(new DocumentListener() {	// 중복 검사 이후 아이디 변경 확인
			@Override
			public void insertUpdate(DocumentEvent e) {
				IDChanged();
			}
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				IDChanged();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				IDChanged();
			}
			
			public void IDChanged() {
				if(duplicationCheckFlag) {	// 중복 검사 성공 상태라면
					duplicationCheckFlag = false;
					duplicationButton.setEnabled(true);	// 버튼 비활성화
					duplicationButton.setText("중복검사");
				}
			}
		});
		
		PWCheckField.getDocument().addDocumentListener(new DocumentListener() {	// 비밀번호 일치 여부를 확인
			@Override
			public void insertUpdate(DocumentEvent e) {
				PWMatch();
			}
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				PWMatch();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				PWMatch();
			}
			
			public void PWMatch() {
				if(new String(PWField.getPassword()).equals(new String(PWCheckField.getPassword()))) {	// 비밀번호가 일치하지 않는다면
					PWStateLabel.setText("일치합니다.");		// 라벨 변경
					PWStateLabel.setForeground(Color.GREEN.darker());
				}
				else {
					PWStateLabel.setText("일치하지 않습니다.");
					PWStateLabel.setForeground(Color.RED);
				}
			}
		});
		
		setTitle("회원가입");
		pack();
		setResizable(false);	// 크기 조정
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	// 종료 시 처리
		setLocationRelativeTo(null);	// 모니터 중앙 표시
		setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		
		// ID 중복 검사 버튼 선택 시
		if(obj == duplicationButton) {			
			String ID = IDField.getText().trim();	// ID를 가져옴
			
			if(ID.equals("")) {
				JOptionPane.showMessageDialog(null, "아이디를 다시 확인해주세요.", "Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			boolean result = SDAO.getInstance().checkIdDuplicate(ID); // 중복 검사를 위해 ID를 인수로 DAO 객체에서 조회 결과 반환
			
			if(!result) {	// 중복되는 아이디가 없을 시(결과가 false)
				JOptionPane.showMessageDialog(null, "중복되지 않은 아이디입니다.", "Information", JOptionPane.PLAIN_MESSAGE);
				duplicationButton.setEnabled(false);	// 버튼 비활성화
				duplicationButton.setText("완료");
				duplicationCheckFlag = true;	// 플래그 변수 변경
			}
			else {
				JOptionPane.showMessageDialog(null, "이미 존재하는 아이디입니다.", "Warning", JOptionPane.WARNING_MESSAGE);
			}
		}
		// 이메일 콤보박스 선택 시
		else if(obj == emailComboBox) {
			String selectedDomain = (String)emailComboBox.getSelectedItem();	// 콤보박스에서 선택한 아이템을 가져와 String으로 형 변환
			
			if(selectedDomain.equals("직접 입력")) {
				emailDomainField.setEditable(true);	// 편집 가능 상태로 변경
				emailDomainField.setText("");		// 문자열 초기화
			}
			else {
				emailDomainField.setEditable(false);		// 편집 불가능 상태로 변경
				emailDomainField.setText(selectedDomain);	// 선택한 도메인으로 설정
			}
		}
		// 확인 버튼 선택 시
		else if(obj == checkButton) {
			// 아이디 중복 검사가 완료되지 않은 상태라면 안내 팝업 출력
			if(!duplicationCheckFlag) {
				JOptionPane.showMessageDialog(null, "중복 검사를 완료해주세요.", "Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			// 사용자가 입력한 정보를 공백을 제거하여 저장
			String ID = IDField.getText().trim();
			String PW = new String(PWField.getPassword());
			String name = nameField.getText().trim();
			String organization = organizationField.getText().trim();
			LocalDate birthDate = null;
			// parseInt 메소드 예외 처리
			try {
				birthDate = LocalDate.of(Integer.parseInt(birthYearField.getText().trim()), Integer.parseInt(birthMonthField.getText().trim()), Integer.parseInt(birthDateField.getText().trim()));
			}
			catch(NumberFormatException exception) {
				JOptionPane.showMessageDialog(null, "생년월일을 다시 확인해주세요.", "Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			String phoneNumber = phoneNumberField1.getText().trim() + phoneNumberField2.getText().trim() + phoneNumberField3.getText().trim();
			String email = emailLocalField.getText().trim() + "@" + emailDomainField.getText().trim();
			
			// 공백 검사 후 팝업 출력
			if(ID.equals("")) {
				JOptionPane.showMessageDialog(null, "아이디를 다시 확인해주세요.", "Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}
			else if(PW.equals("")) {
				JOptionPane.showMessageDialog(null, "비밀번호를 다시 확인해주세요.", "Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}
			else if(name.equals("")) {
				JOptionPane.showMessageDialog(null, "이름을 다시 확인해주세요.", "Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}
			else if(organization.equals("")) {
				JOptionPane.showMessageDialog(null, "소속을 다시 확인해주세요.", "Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}
			else if(phoneNumber.length() != 11) {	// 정상적으로 입력된 전화번호는 11자리임
				JOptionPane.showMessageDialog(null, "전화번호를 다시 확인해주세요.", "Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}
			else if(email.charAt(0) == '@') {
				JOptionPane.showMessageDialog(null, "이메일을 다시 확인해주세요.", "Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			// 로그인 시도 User 객체 생성
			User loginUser = new User();
			loginUser.setID(ID);
			loginUser.setPW(PW);
			loginUser.setName(name);
			loginUser.setOrganization(organization);
			loginUser.setBirthDate(birthDate);
			loginUser.setPhoneNumber(phoneNumber);
			loginUser.setEmail(email);
			
			boolean result = SDAO.getInstance().registerUser(loginUser);	// 회원가입을 위해 User 객체를 인수로 DAO 객체에서 성공 여부 반환
			
			if(result) {
				// 회원가입 성공 시 현재 GUI를 닫고 로그인 GUI로 이동
				JOptionPane.showMessageDialog(null, "회원가입 성공", "Information", JOptionPane.PLAIN_MESSAGE);
				dispose();
				loginFrame.setVisible(true);
			}
			else {
				// 알수 없는 이유로 회원가입 실패 시 재시도 요청
				JOptionPane.showMessageDialog(null, "알 수 없는 이유로 회원가입에 실패했습니다. 다시 시도해주세요.", "Warning", JOptionPane.WARNING_MESSAGE);
			}
		}
		// 뒤로가기 버튼 클릭 시
		else if(obj == exitButton) {
			dispose();
			loginFrame.setVisible(true);
		}
	}
}