package GUI;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;

public class GroupMainPanel extends JPanel implements ActionListener {
	JComboBox<String> groupComboBox;	// 그룹 선택을 위한 콤보박스
	JTextArea memoArea;			// 메모 출력
	JTextField memoField;		// 메모 입력
	JTable groupMemberTable;	// 그룹원 테이블
	DefaultTableModel tableModel;	// 테이블에 삽입할 기본 모델
	JButton inputButton, updateTaskButton;	// 업무 갱신, 입력 버튼
	
	public GroupMainPanel() {
		setLayout(new BorderLayout());
		
		// 테스트 데이터
		// DB에서 그룹명을 가져와 표시해야 함
		String testName[] = {"그룹1", "그룹2", "그룹3"};
		groupComboBox = new JComboBox<>(testName);
		groupComboBox.setBackground(Color.WHITE);
		groupComboBox.setFont(new Font("Malgun Gothic", Font.BOLD, 20));
		
		JLabel groupNameLabel = new JLabel("   그룹명: 그룹1");
		groupNameLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 20));
		
		JLabel groupInviteCodeLabel = new JLabel("/ 초대 코드: 12345");
		groupInviteCodeLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 20));
		
		updateTaskButton = new JButton("업무 갱신");
		updateTaskButton.setBackground(Color.WHITE);
		updateTaskButton.setFocusPainted(false);
		
		// 상단 좌측 패널
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		leftPanel.add(groupComboBox);
		leftPanel.add(groupNameLabel);
		leftPanel.add(groupInviteCodeLabel);
		
		// 상단 우측 패널
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		rightPanel.add(updateTaskButton);
		
		// 상단 패널
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		topPanel.add(leftPanel, BorderLayout.WEST);
		topPanel.add(rightPanel, BorderLayout.EAST);
		topPanel.setBorder(new EmptyBorder(20, 20, 10, 20));
		
		memoArea = new JTextArea(20, 30);
		
		memoField = new JTextField(30);
		inputButton = new JButton("입력");
		inputButton.setBackground(Color.WHITE);
		inputButton.setFocusPainted(false);
		
		// 입력 패널
		JPanel inputPanel = new JPanel();
		inputPanel.add(memoField);
		inputPanel.add(inputButton);
		inputPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
		
		// 좌측 패널
		JPanel memoPanel = new JPanel();
		memoPanel.setLayout(new BorderLayout());
		memoPanel.add(memoArea, BorderLayout.CENTER);
		memoPanel.add(inputPanel, BorderLayout.SOUTH);
		memoPanel.setBorder(new EmptyBorder(0, 20, 20, 10));
		
		// 테스트 테이블 모델
		tableModel = new DefaultTableModel();
		tableModel.addColumn("이름");
		tableModel.addColumn("업무");
		tableModel.addRow(new Object[] {"홍길동", "프로그래밍"});
		tableModel.addRow(new Object[] {"강감찬", "데이터베이스"});
		tableModel.addRow(new Object[] {"유관순", "없음"});
		tableModel.addRow(new Object[] {"이순신", "프로젝트 매니저"});
		tableModel.addRow(new Object[] {"서희", "보고서 및 발표"});
		
		groupMemberTable = new JTable(tableModel);
		groupMemberTable.setRowHeight(20);	// 행 높이 설정
		groupMemberTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);	// 테이블 자동 크기 조절 끄기
		groupMemberTable.getColumnModel().getColumn(0).setPreferredWidth(70);	// 첫 번째 열 너비 설정
		groupMemberTable.getColumnModel().getColumn(1).setPreferredWidth(350);	// 두 번째 열 너비 설정
		JScrollPane scrollPane = new JScrollPane(groupMemberTable);
		//scrollPane.setPreferredSize(new Dimension(200, 300));
		
		// 우측 패널
		JPanel groupMemberPanel = new JPanel();
		groupMemberPanel.add(scrollPane);
		groupMemberPanel.setBorder(new EmptyBorder(0, 10, 20, 20));
		
		// 최종 패널 추가
		add(topPanel, BorderLayout.NORTH);
		add(memoPanel, BorderLayout.WEST);
		add(groupMemberPanel, BorderLayout.CENTER);
	}
	
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		
		// 그룹 콤보박스 선택 시 
		if(obj == groupComboBox) {
			// DB에서 그룹원 정보를 가져오는 이벤트 필요
		}
		// 메모 입력 시
		else if(obj == memoField || obj == inputButton) {
			// 사용자의 입력을 가져와 DB에 전달, TextArea 갱신
		}
		// 진행 업무 갱신 시
		else if(obj == updateTaskButton) {
			// JOptionPanel로 간단한 입력을 받아 저장
		}
		
		// 특정 행 더블 클릭 시 그룹원 상세 정보 출력
		// 별도의 JDialog에서 표시
	}
}