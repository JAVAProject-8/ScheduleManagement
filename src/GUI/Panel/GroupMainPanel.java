package GUI.Panel;

import GUI.Dialog.*;

import DB.User;
import DB.Group;
import DB.Member;
import DB.Memo;
import DB.SDAO;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class GroupMainPanel extends JPanel implements ActionListener {
	JComboBox<String> groupComboBox;	// 그룹 선택을 위한 콤보박스
	DefaultComboBoxModel<String> comboBoxModel;
	JTextArea memoArea;			// 메모 출력
	JTextField memoField;		// 메모 입력
	JButton inputButton, updateTaskButton, checkButton, addButton;	// 메모 입력, 업무 입력, 일정 조회, 일정 추가 버튼
	JLabel groupInviteCodeLabel;	// 초대 코드 라벨
	JTable groupMemberTable;	// 그룹원 테이블
	DefaultTableModel tableModel;	// 테이블에 삽입할 기본 모델
	
	JPanel topPanel, memoPanel, groupMemberPanel;
	
	ArrayList<Group> groups = null;
	ArrayList<Member> members = null;
	ArrayList<Memo> memos = null;
	
	User user = null;
	Group selectedGroup = null;
	
	public GroupMainPanel(User _u) {
		setLayout(new BorderLayout());
		user = _u;
		
		initComponent();	// 기본 컴포넌트 배치
		refreshGroupList();	// 그룹 목록 새로고침
	}
	
	// 컴포넌트 배치 메소드
	private void initComponent() {
		comboBoxModel = new DefaultComboBoxModel<>();		// 모델 생성
		groupComboBox = new JComboBox<>(comboBoxModel);		// 생성한 모델로 콤보박스 생성
		groupComboBox.setBackground(Color.WHITE);
		groupComboBox.setFont(new Font("Malgun Gothic", Font.BOLD, 20));
		
		groupInviteCodeLabel = new JLabel("    초대 코드: ");
		groupInviteCodeLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 20));
		
		updateTaskButton = new JButton("업무 입력");
		updateTaskButton.setBackground(Color.WHITE);
		updateTaskButton.setFocusPainted(false);
		
		checkButton = new JButton("일정 조회");
		checkButton.setBackground(Color.WHITE);
		checkButton.setFocusPainted(false);
		
		addButton = new JButton("일정 추가");
		addButton.setBackground(Color.WHITE);
		addButton.setFocusPainted(false);
		
		// 상단 좌측 패널
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		leftPanel.add(groupComboBox);
		leftPanel.add(groupInviteCodeLabel);
		
		// 상단 우측 패널
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		rightPanel.add(checkButton);
		rightPanel.add(addButton);
		rightPanel.add(updateTaskButton);
		
		// 상단 패널
		topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		topPanel.add(leftPanel, BorderLayout.WEST);
		topPanel.add(rightPanel, BorderLayout.EAST);
		topPanel.setBorder(new EmptyBorder(20, 20, 10, 20));
		
		memoArea = new JTextArea(20, 30);
		memoArea.setEditable(false);
		memoArea.setLineWrap(true);	// 자동 줄바꿈 처리
		
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
		memoPanel = new JPanel();
		memoPanel.setLayout(new BorderLayout());
		memoPanel.add(new JScrollPane(memoArea), BorderLayout.CENTER);
		memoPanel.add(inputPanel, BorderLayout.SOUTH);
		memoPanel.setBorder(new EmptyBorder(0, 20, 20, 10));
		
		// 테스트 테이블 모델
		tableModel = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int r, int c) {	// 셀 수정 비활성화 처리
				return false;
			}
		};
		tableModel.addColumn("이름");
		tableModel.addColumn("업무");
		
		// 테이블 설정
		groupMemberTable = new JTable(tableModel);
		groupMemberTable.setRowHeight(20);	// 행 높이 설정
		groupMemberTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);	// 테이블 자동 크기 조절 끄기
		groupMemberTable.getColumnModel().getColumn(0).setPreferredWidth(60);	// 첫 번째 열 너비 설정
		groupMemberTable.getColumnModel().getColumn(1).setPreferredWidth(280);	// 두 번째 열 너비 설정
		groupMemberTable.getTableHeader().setReorderingAllowed(false);	// 열 순서 변경 금지 처리
		groupMemberTable.getTableHeader().setResizingAllowed(false);	// 열 너비 변경 금지 처리
		
		groupMemberTable.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int selectedIndex = groupMemberTable.getSelectedRow();	// 선택한 행의 인덱스를 가져옴
				
				int selectedModelIndex = groupMemberTable.convertRowIndexToModel(selectedIndex);	// 테이블 모델의 인덱스로 변환
				Member selectedMember = members.get(selectedModelIndex);
				User selectedUser = SDAO.getInstance().getUserInfo(selectedMember.getUserId());
				
				// 특정 행 더블 클릭 시 그룹원 상세 정보 출력
				Window mainFrame = SwingUtilities.getWindowAncestor(groupMemberTable);
				new GroupMemberInfoDialog((JFrame)mainFrame, "상세 정보", selectedUser, selectedMember);
			}
			
			@Override
			public void mousePressed(MouseEvent e) {}
			
			@Override
			public void mouseReleased(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseExited(MouseEvent e) {}
		});
		
		// 우측 패널
		groupMemberPanel = new JPanel();
		groupMemberPanel.setLayout(new BorderLayout());
		groupMemberPanel.add(new JScrollPane(groupMemberTable), BorderLayout.CENTER);
		groupMemberPanel.setBorder(new EmptyBorder(0, 10, 20, 20));
		
		groupComboBox.addActionListener(this);
		memoField.addActionListener(this);
		inputButton.addActionListener(this);
		checkButton.addActionListener(this);
		addButton.addActionListener(this);
		updateTaskButton.addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		
		// 그룹 콤보박스 선택 시 
		if(obj == groupComboBox) {
			int selectedIndex = groupComboBox.getSelectedIndex();	// 인덱스를 가져옴
			
			if(selectedIndex != -1) {
				selectedGroup = groups.get(selectedIndex);	// 선택 그룹을 변경
				
				setGroupInfo();	// 그룹 정보 갱신
				setGroupMemo();	// 그룹 메모 갱신
				setGroupTask(); // 그룹 업무 갱신
			}
		}
		// 메모 입력 시
		else if(obj == memoField || obj == inputButton) {
			String memoText = memoField.getText().trim();
			if(memoText.equals("")) {
				return;
			}
			
			// 메모 객체 생성
			Memo memo = new Memo(selectedGroup.getGroupId(), user.getID(), memoText, LocalDateTime.now());
			
			// 사용자의 입력을 가져와 DB에 전달
			boolean result = SDAO.getInstance().insertMemo(memo);
			
			// 전달 성공 시 TextArea 갱신
			if(result) {
				JOptionPane.showMessageDialog(null, "메모 입력 성공", "Information", JOptionPane.PLAIN_MESSAGE);
				setGroupMemo();
			}
			// 전달 실패 시
			else {
				JOptionPane.showMessageDialog(null, "메모 입력 실패. 다시 시도해 주세요.", "Warning", JOptionPane.WARNING_MESSAGE);
			}
			
			memoField.setText("");	// 입력 필드 초기화
		}
		// 그룹 일정 조회 시
		else if(obj == checkButton) {
			new CheckScheduleDialog(null, "그룹 일정 조회", user, selectedGroup);
		}
		// 그룹 일정 추가 시
		else if(obj == addButton) {
			// 팀장 권한을 가지고 있는지 검사
			String currentID = user.getID();
			boolean isPermission = false;
			
			// 팀장 권한 탐색
			for(int i = 0; i < members.size(); i++) {
				if(members.get(i).getUserId().equals(currentID)) {
					if(members.get(i).getPosition().equals("Y")) {
						isPermission = true;
					}
					break;
				}
			}
			
			if(isPermission) {
				new ScheduleDialog(null, "그룹 일정 추가", user, selectedGroup);
			}
			else {
				JOptionPane.showMessageDialog(null, "권한이 없습니다.", "Warning", JOptionPane.WARNING_MESSAGE);
			}
		}
		// 진행 업무 입력 시
		else if(obj == updateTaskButton) {
			// JOptionPanel로 간단한 입력을 받아 저장
			String task = JOptionPane.showInputDialog(null, "현재 업무를 입력해주세요.", "업무 입력", JOptionPane.QUESTION_MESSAGE).trim();
			if(task == null || task.equals("")) {
				JOptionPane.showMessageDialog(null, "입력 내용을 확인해주세요.", "Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			boolean result = SDAO.getInstance().updateTask(user.getID(), selectedGroup.getGroupId(), task);
			
			if(result) {
				JOptionPane.showMessageDialog(null, "업무 입력 성공", "Information", JOptionPane.PLAIN_MESSAGE);
				setGroupTask();
			}
			else {
				JOptionPane.showMessageDialog(null, "업무 입력 실패. 다시 시도해 주세요.", "Warning", JOptionPane.WARNING_MESSAGE);
			}
		}
	}
	
	public void refreshGroupList() {
		groups = SDAO.getInstance().getMyGroups(user.getID());	// 사용자 아이디를 인수로 DAO 객체에서 그룹 ArrayList 반환
		
		this.removeAll();					// 모든 컴포넌트를 제거
		comboBoxModel.removeAllElements();	// 모든 원소를 제거
		
		if(groups.size() == 0) {	// 사용자가 가입되어있는 그룹이 없는 경우
			JLabel impormationLabel = new JLabel("초대 코드를 입력하여 그룹에 가입해주세요.", JLabel.CENTER);
			impormationLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 30));
			add(impormationLabel, BorderLayout.CENTER);
			selectedGroup = null;
			
			this.revalidate();	// 배치 정보 재계산
			this.repaint();		// 재배치
			
			return;
		}
		
		// 패널 추가
		add(topPanel, BorderLayout.NORTH);
		add(memoPanel, BorderLayout.WEST);
		add(groupMemberPanel, BorderLayout.CENTER);
		
		groupComboBox.removeActionListener(this);	// 리스너 임시 삭제
		
		for(int i = 0; i < groups.size(); i++) {
			comboBoxModel.addElement(groups.get(i).getGroupName());	// 그룹 이름을 Vector에 추가
		}
		
		groupComboBox.setSelectedIndex(0);
		selectedGroup = groups.get(0);
		
		setGroupInfo();	// 그룹 정보 갱신
		setGroupMemo();	// 그룹 메모 갱신
		setGroupTask(); // 그룹 업무 갱신
		
		groupComboBox.addActionListener(this);	// 리스너 재등록
		
		this.revalidate();	// 배치 정보 재계산
		this.repaint();		// 재배치
	}
	
	// 그룹 정보 설정
	public void setGroupInfo() {
		groupInviteCodeLabel.setText("\t초대 코드: " + selectedGroup.getInviteCode());
	}
	
	// 그룹 메모 설정
	public void setGroupMemo() {
		memoArea.setText("");
		
		memos = SDAO.getInstance().getMemosByGroupId(selectedGroup.getGroupId());
		
		// DB에서 전달받은 ArrayList를 작성시각을 기준으로 하여 정렬
		Collections.sort(memos, new Comparator<Memo>() {
			@Override
			public int compare(Memo m1, Memo m2) {
				return m1.getCreatedAt().compareTo(m2.getCreatedAt());
			}
		});
		
		for(int i = 0; i < memos.size(); i++) {
			User otherUser = SDAO.getInstance().getUserInfo(memos.get(i).getWriterId());	// ID를 매개변수로 User 객체를 가져옴
			
			String line = "[" + otherUser.getName() + "] "
					+ memos.get(i).getCreatedAt().format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm")) + "\n"
					+ memos.get(i).getContent();
			
			memoArea.append(line + "\n\n");
		}
	}
	
	// 그룹 업무 설정
	public void setGroupTask() {
		members = SDAO.getInstance().getMembersByGroupId(selectedGroup.getGroupId());
		tableModel.setRowCount(0);
		
		for(int i = 0; i < members.size(); i++) {
			Object[] data = new Object[2];
			
			data[0] = members.get(i).getUserName();
			data[1] = members.get(i).getTask();
			
			tableModel.addRow(data);
		}
	}
}