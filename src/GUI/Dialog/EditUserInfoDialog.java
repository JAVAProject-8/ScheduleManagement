package GUI.Dialog;

import DB.SDAO;
import DB.User;
import GUI.Frame.MainFrame;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;
import java.util.StringTokenizer;

public class EditUserInfoDialog extends JDialog implements ActionListener {
	JTextField IDField, nameField, organizationField; // 아이디, 이름, 소속
	JTextField birthYearField, birthMonthField, birthDateField;	// 생년월일
	JTextField phoneNumberField1, phoneNumberField2, phoneNumberField3;	// 전화번호
	JTextField emailLocalField, emailDomainField;	// 이메일 로컬, 도메인
	JComboBox<String> emailComboBox;		// 도메인 콤보 박스
	JPasswordField PWField, PWCheckField;	// 비밀번호, 비밀번호 확인
	JLabel PWStateLabel;	// 비밀번호 일치 여부 확인 라벨
	JButton checkButton;	// 확인 버튼
	
	MainFrame mainFrame = null;
	User user = null;
	StringTokenizer st = null;
	
	// 생성자(자동 완성을 위해 현재 로그인 한 User 객체를 전달받아야 함)
	public EditUserInfoDialog(MainFrame frame, String title, User _u) {
		super(frame, title, true);
		user = _u;
		mainFrame = frame;
		
		setLayout(new BorderLayout());
		
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
		JPanel IDPanel = new JPanel();
		IDPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		IDPanel.add(IDField);
		
		// 비밀번호 입력 패널
		PWField = new JPasswordField(15);
		JPanel PWPanel = new JPanel();
		PWPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		PWPanel.add(PWField);
		
		// 비밀번호 확인 패널
		PWCheckField = new JPasswordField(15);
		PWStateLabel = new JLabel("", JLabel.LEFT);
		PWStateLabel.setPreferredSize(new Dimension(105, 20));
		
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
		inputPanel1.setBorder(new EmptyBorder(20, 20, 20, 20));
		
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
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(checkButton);
		
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
		inputPanel2.setBorder(new EmptyBorder(20, 20, 20, 20));
		
		add(inputPanel1, BorderLayout.WEST);
		add(inputPanel2, BorderLayout.EAST);
		
		setDefaultValue();	// 기본값 설정
		
		// ActionListener 등록
		emailComboBox.addActionListener(this);
		checkButton.addActionListener(this);
		
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
		
		pack();	// 크기 자동 맞춤
		setResizable(false);	// 크기 조정
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);	// 종료 시 처리
		setLocationRelativeTo(null);	// 모니터 중앙 표시
		setVisible(true);
	}
	
	// ActionListener 메소드 구현
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		
		// 이메일 콤보박스 선택 시
		if(obj == emailComboBox) {
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
			// 필요 정보를 저장
			String PW = new String(PWField.getPassword());
			String organization = organizationField.getText().trim();
			String phoneNumber = phoneNumberField1.getText().trim() + phoneNumberField2.getText().trim() + phoneNumberField3.getText().trim();
			String email = emailLocalField.getText().trim() + "@" + emailDomainField.getText().trim();
			
			// 공백 검사 후 팝업 출력
			if(PW.equals("")) {
				JOptionPane.showMessageDialog(null, "비밀번호를 다시 확인해주세요.", "Warning", JOptionPane.WARNING_MESSAGE);
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
			
			User editUser = new User();
			editUser.setID(user.getID());
			editUser.setPW(PW);
			editUser.setName(user.getName());
			editUser.setOrganization(organization);
			editUser.setBirthDate(user.getBirthDate());
			editUser.setPhoneNumber(phoneNumber);
			editUser.setEmail(email);
			
			boolean result = SDAO.getInstance().updateUserInfo(editUser);	// 정보 수정을 위해 User 객체를 인수로 DAO 객체에서 성공 여부 반환
			
			// 회원 정보 수정 성공 시
			if(result) {
				JOptionPane.showMessageDialog(null, "사용자 정보 수정 성공", "Information", JOptionPane.PLAIN_MESSAGE);
				dispose();	// 프레임 종료
				mainFrame.user = editUser;	// MainFrame에 위치한 User 객체를 업데이트
			}
			// 회원 정보 수정 실패 시
			else {
				// 알수 없는 이유로 정보 수정 실패 시 재시도 요청
				JOptionPane.showMessageDialog(null, "알 수 없는 이유로 사용자 정보 수정에 실패했습니다. 다시 시도해주세요.", "Warning", JOptionPane.WARNING_MESSAGE);
			}
		}
	}
	
	// 기본값 설정 메소드
	private void setDefaultValue() {
		// 수정 불가능 항목 기본 정보 설정
		IDField.setEditable(false);
		IDField.setBackground(Color.WHITE);
		IDField.setText(user.getID());
		
		nameField.setEditable(false);
		nameField.setBackground(Color.WHITE);
		nameField.setText(user.getName());
		
		birthYearField.setEditable(false);
		birthYearField.setBackground(Color.WHITE);
		birthYearField.setText(Integer.toString(user.getBirthDate().getYear()));
		
		birthMonthField.setEditable(false);
		birthMonthField.setBackground(Color.WHITE);
		birthMonthField.setText(Integer.toString(user.getBirthDate().getMonthValue()));
		
		birthDateField.setEditable(false);
		birthDateField.setBackground(Color.WHITE);
		birthDateField.setText(Integer.toString(user.getBirthDate().getDayOfMonth()));
		
		// 수정 가능 항목 기본 정보 설정
		organizationField.setText(user.getOrganization());
		phoneNumberField1.setText(user.getPhoneNumber().substring(0, 3));
		phoneNumberField2.setText(user.getPhoneNumber().substring(3, 7));
		phoneNumberField3.setText(user.getPhoneNumber().substring(7));
		
		st = new StringTokenizer(user.getEmail(), "@");
		emailLocalField.setText(st.nextToken());
		emailDomainField.setText(st.nextToken());
	}
}
