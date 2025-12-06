package GUI.Dialog;

import DB.User;
import DB.Member;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;

import java.time.format.DateTimeFormatter;

public class GroupMemberInfoDialog extends JDialog {
	// 그룹원 세부정보
	JTextField nameField, organizationField, phoneNumberField, emailField;	// 이름, 소속, 전화번호, 이메일
	JLabel positionField, birthDateField;	// 직책, 생일
	JButton checkButton;	// 확인(종료) 버튼
	
	User user = null;
	Member member = null;
	
	public GroupMemberInfoDialog(JFrame frame, String title, User _u, Member _m) {
		super(frame, title, true);
		user = _u;
		member = _m;
		
		setLayout(new BorderLayout());
		
		JPanel labelsPanel = new JPanel();
		labelsPanel.setLayout(new GridLayout(6, 1, 0, 5));
		labelsPanel.add(new JLabel("이름", JLabel.RIGHT));
		labelsPanel.add(new JLabel("직책", JLabel.RIGHT));
		labelsPanel.add(new JLabel("소속", JLabel.RIGHT));
		labelsPanel.add(new JLabel("생년월일", JLabel.RIGHT));
		labelsPanel.add(new JLabel("전화번호", JLabel.RIGHT));
		labelsPanel.add(new JLabel("이메일", JLabel.RIGHT));
		labelsPanel.setBorder(new EmptyBorder(10, 20, 10, 10));
		
		nameField = new JTextField(15);
		nameField.setEditable(false);
		nameField.setBackground(Color.WHITE);
		
		positionField = new JLabel();
		positionField.setOpaque(true);
		positionField.setBackground(Color.WHITE);
		
		organizationField = new JTextField(15);
		organizationField.setEditable(false);
		organizationField.setBackground(Color.WHITE);
		
		birthDateField = new JLabel();
		birthDateField.setOpaque(true);
		birthDateField.setBackground(Color.WHITE);
		
		phoneNumberField = new JTextField(15);
		phoneNumberField.setEditable(false);
		phoneNumberField.setBackground(Color.WHITE);
		
		emailField = new JTextField(15);
		emailField.setEditable(false);
		emailField.setBackground(Color.WHITE);
		
		JPanel fieldsPanel = new JPanel();
		fieldsPanel.setLayout(new GridLayout(6, 1, 0, 5));
		fieldsPanel.add(nameField);
		fieldsPanel.add(positionField);
		fieldsPanel.add(organizationField);
		fieldsPanel.add(birthDateField);
		fieldsPanel.add(phoneNumberField);
		fieldsPanel.add(emailField);
		fieldsPanel.setBorder(new EmptyBorder(10, 10, 10, 20));
		
		checkButton = new JButton("확인");
		checkButton.setBackground(Color.WHITE);
		checkButton.setFocusPainted(false);
		checkButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		JPanel bottomPanel = new JPanel();
		bottomPanel.add(checkButton);
		
		add(labelsPanel, BorderLayout.WEST);
		add(fieldsPanel, BorderLayout.EAST);
		add(bottomPanel, BorderLayout.SOUTH);
		
		setUserInfo();
		
		pack();	// 크기 자동 맞춤
		setResizable(false);	// 크기 조정 불가
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);	// 종료 시 처리
		setLocationRelativeTo(null);	// 모니터 중앙 표시
		setVisible(true);
	}
	
	public void setUserInfo() {
		nameField.setText(user.getName());
		String position = member.getPosition();
		if(position.equals("Y")) {
			position = "팀장";
		}
		else if(position.equals("N")) {
			position = "팀원";
		}
		positionField.setText(position);	// 이 부분은 Member 테이블에서 가져와야 함
		organizationField.setText(user.getOrganization());
		birthDateField.setText(user.getBirthDate().format(DateTimeFormatter.ofPattern("YYYY년 MM월 dd일")));
		phoneNumberField.setText(user.getPhoneNumber().substring(0, 3) + "-" + user.getPhoneNumber().substring(3, 7) + "-" + user.getPhoneNumber().substring(7));
		emailField.setText(user.getEmail());
	}
}