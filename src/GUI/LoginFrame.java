package GUI;

import DB.User;
import DB.SDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;

public class LoginFrame extends JFrame implements ActionListener {
	JTextField IDField;
	JPasswordField PWField;
	JButton loginButton, registerButton;
	
	public LoginFrame() {
		Container ct = getContentPane();
		ct.setLayout(new BorderLayout(0, 10));
		
		JLabel topLabel = new JLabel("일정 관리 프로그램", JLabel.CENTER);
		topLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 25));
		topLabel.setBorder(new EmptyBorder(30, 0, 20, 0));
		
		JLabel IDLabel = new JLabel("ID", JLabel.RIGHT);
		JLabel PWLabel = new JLabel("PW", JLabel.RIGHT);
		
		JPanel labelsPanel = new JPanel();
		labelsPanel.setLayout(new GridLayout(2, 1, 0, 5));
		labelsPanel.add(IDLabel);	// 두 레이블을 2*1 판넬에 추가
		labelsPanel.add(PWLabel);
		
		IDField = new JTextField(15);
		PWField = new JPasswordField(15);
		
		JPanel fieldsPanel = new JPanel();
		fieldsPanel.setLayout(new GridLayout(2, 1, 0, 5));
		fieldsPanel.add(IDField);	// 두 필드를 2*1 판넬에 추가
		fieldsPanel.add(PWField);
		fieldsPanel.setBorder(new EmptyBorder(0, 0, 0, 10));	// 로그인 버튼과의 여백
		
		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new BorderLayout(10, 5));
		
		inputPanel.add(labelsPanel, BorderLayout.WEST);	// 두 판넬을 판넬에 나란히 추가
		inputPanel.add(fieldsPanel, BorderLayout.EAST);
		
		loginButton = new JButton("로그인");
		loginButton.setPreferredSize(new Dimension(80, 40));	// 버튼 크기
		loginButton.setBackground(Color.WHITE);	// 버튼 배경색
		loginButton.setFocusPainted(false);	// 버튼 선택 시 텍스트 주변 네모박스
		
		registerButton = new JButton("회원가입");
		registerButton.setPreferredSize(new Dimension(130, 40));
		registerButton.setBackground(Color.WHITE);
		registerButton.setFocusPainted(false);
		
		JPanel mainPanel = new JPanel();
		
		mainPanel.setLayout(new FlowLayout());
		mainPanel.add(inputPanel);
		mainPanel.add(loginButton);
		mainPanel.setBorder(new EmptyBorder(0, 20, 0, 20));	// 프레임과의 여백
		
		JPanel bottomPanel = new JPanel();
		bottomPanel.setBorder(new EmptyBorder(0, 0, 20, 0));	// 회원가입 버튼과 프레임과의 여백
		bottomPanel.add(registerButton);
		
		ct.add(topLabel, BorderLayout.NORTH);
		ct.add(mainPanel, BorderLayout.CENTER);
		ct.add(bottomPanel, BorderLayout.SOUTH);
		
		PWField.addActionListener(this);
		loginButton.addActionListener(this);
		registerButton.addActionListener(this);
		
		setTitle("로그인");
		pack();	// 크기 자동 맞춤
		setResizable(false);	// 크기 조정
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	// 종료 시 처리
		setLocationRelativeTo(null);	// 모니터 중앙 표시
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		
		// 로그인 버튼 클릭
		if(obj == loginButton || obj == PWField) {
			// ID PW를 가져옴
			String ID = IDField.getText().trim();
			String PW = new String(PWField.getPassword());
			
			// 유효성 검사
			if(ID.equals("") || PW.equals("")) {
				JOptionPane.showMessageDialog(null, "아이디 또는 비밀번호를 다시 확인해주세요.", "Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			User user = SDAO.getInstance().loginGetUser(ID, PW);	// 아이디와 비밀번호를 인수로 DAO 객체에서 User 객체 반환
			
			// 로그인 성공 시(반환값이 null이 아닐 시) 안내 메시지 출력 후 메인 화면으로 이동
			if(user != null) {
				JOptionPane.showMessageDialog(null, "로그인 성공", "Information", JOptionPane.PLAIN_MESSAGE);
				dispose();
				new MainFrame(user);	// 로그인에 성공한 User 객체를 전달
			}
			// 로그인 실패 시 안내 메시지 출력 후 재입력 요청
			else {
				JOptionPane.showMessageDialog(null, "로그인 실패", "Information", JOptionPane.WARNING_MESSAGE);
			}
		}
		// 회원가입 버튼 클릭
		else if(obj == registerButton) {
			setVisible(false);
			new RegisterFrame(this);
		}
	}
}