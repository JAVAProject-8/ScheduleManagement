package GUI.Frame;

import GUI.Panel.*;
import GUI.Dialog.*;

import DB.User;
import DB.Group;
import DB.SDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class MainFrame extends JFrame implements ActionListener {
	public User user = null;
	
	// JTabbedPane에 추가될 패널을 필드로 선언 필요
	MainPanel mainPanel = null;
	TimetablePanel timeTablePanel = null;
	CalendarPanel calendarPanel = null;
	GroupMainPanel groupMainPanel = null;
	GroupPanel groupPanel = null;
	
	public MainFrame(User _u) {
		Container ct = getContentPane();
		user = _u;	// 현재 로그인 한 사용자를 식별하기 위한 객체 저장
		
		// 메뉴 바 설정
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		// 메뉴 설정
		JMenu scheduleMenu = new JMenu("일정 관리");
		JMenu groupMenu = new JMenu("그룹 관리");
		JMenu settingMenu = new JMenu("설정");
		menuBar.add(scheduleMenu);
		menuBar.add(groupMenu);
		menuBar.add(settingMenu);
		
		// 일정 관리 메뉴 아이템 설정
		JMenuItem addScheduleMenuItem = new JMenuItem("일정 추가");
		JMenuItem checkScheduleMenuItem = new JMenuItem("전체 일정 조회");
		scheduleMenu.add(addScheduleMenuItem);
		scheduleMenu.addSeparator();
		scheduleMenu.add(checkScheduleMenuItem);
		
		// 그룹 관리 메뉴 아이템 설정
		JMenuItem createGroupMenuItem = new JMenuItem("그룹 생성");
		JMenuItem joinGroupMenuItem = new JMenuItem("그룹 가입");
		JMenuItem leaveGrupMenuItem = new JMenuItem("그룹 탈퇴");
		groupMenu.add(createGroupMenuItem);
		groupMenu.addSeparator();
		groupMenu.add(joinGroupMenuItem);
		groupMenu.addSeparator();
		groupMenu.add(leaveGrupMenuItem);
		
		// 설정 메뉴 아이템 설정
		JMenuItem editUserInfoMenuItem = new JMenuItem("사용자 정보 관리");
		JMenuItem exitMenuItem = new JMenuItem("프로그램 종료");
		settingMenu.add(editUserInfoMenuItem);
		settingMenu.addSeparator();
		settingMenu.add(exitMenuItem);
		
		// 그룹 홀더(JTabbedPane) 설정
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setBackground(Color.WHITE);
		
		// 패널 생성
		mainPanel = new MainPanel(user);
		timeTablePanel = new TimetablePanel(user);
		calendarPanel = new CalendarPanel(user);
		groupMainPanel = new GroupMainPanel(user);
		groupPanel = new GroupPanel(user);
		
		tabbedPane.addTab("메인", mainPanel);
		tabbedPane.addTab("시간표", timeTablePanel);
		tabbedPane.addTab("캘린더", calendarPanel);
		tabbedPane.addTab("그룹 메인", groupMainPanel);
		tabbedPane.addTab("그룹 시간표", groupPanel);
		
		// ActionListener 등록
		addScheduleMenuItem.addActionListener(this);
		checkScheduleMenuItem.addActionListener(this);
		createGroupMenuItem.addActionListener(this);
		joinGroupMenuItem.addActionListener(this);
		leaveGrupMenuItem.addActionListener(this);
		editUserInfoMenuItem.addActionListener(this);
		exitMenuItem.addActionListener(this);
		
		ct.add(tabbedPane);
		
		setTitle("일정 관리 프로그램");
		//pack();
		setSize(800, 600);
		setResizable(false);	// 크기 조정
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	// 종료 시 처리
		setLocationRelativeTo(null);	// 모니터 중앙 표시
		setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		
		if(command.equals("일정 추가")) {
			new ScheduleDialog(this, "일정 추가", user);
		}
		else if(command.equals("전체 일정 조회")) {
			new CheckScheduleDialog(this, "전체 일정 조회", user);
		}
		else if(command.equals("그룹 생성")) {
			String groupId = JOptionPane.showInputDialog(null, "그룹 아이디를 입력해주세요.", "그룹 생성", JOptionPane.QUESTION_MESSAGE);
			if(groupId == null || groupId.equals("")) {
				return;
			}
			String groupName = JOptionPane.showInputDialog(null, "그룹명을 입력해주세요.", "그룹 생성", JOptionPane.QUESTION_MESSAGE);
			if(groupName == null || groupName.equals("")) {
				return;
			}
			
			boolean result = SDAO.getInstance().createGroup(groupId, groupName, user.getID());	// 그룹 아이디와 그룹명, 사용자 아이디를 인수로 DAO 객체에서 그룹 생성 성공 여부 반환
			
			// 그룹 생성 성공
			if(result) {
				JOptionPane.showMessageDialog(null, "그룹 생성 성공", "Information", JOptionPane.PLAIN_MESSAGE);
				groupMainPanel.refreshGroupList();	// 현재 그룹 목록 갱신
			}
			// 그룹 생성 실패
			else {
				JOptionPane.showMessageDialog(null, "그룹 생성 실패", "Warning", JOptionPane.WARNING_MESSAGE);
			}
		}
		else if(command.equals("그룹 가입")) {
			String inviteCode = JOptionPane.showInputDialog(null, "초대코드를 입력해주세요.", "그룹 가입", JOptionPane.QUESTION_MESSAGE);
			if(inviteCode == null || inviteCode.equals("")) {
				return;
			}
			
			int result = SDAO.getInstance().joinGroup(user.getID(), inviteCode);	// 사용자 아이디와 초대코드를 인수로 DAO 객체에서 그룹 가입 성공 여부 반환
			
			// 그룹 가입 성공
			if(result == 1) {
				JOptionPane.showMessageDialog(null, "그룹 가입 성공", "Information", JOptionPane.PLAIN_MESSAGE);
				groupMainPanel.refreshGroupList();	// 현재 그룹 목록 갱신
			}
			// 초대 코드 오류, 그룹 가입 실패
			else if(result == 0) {
				JOptionPane.showMessageDialog(null, "그룹 가입 실패(초대 코드 오류)", "Warning", JOptionPane.WARNING_MESSAGE);
			}
			// 그 외 오류로 그룹 가입 실패
			if(result == -1) {
				JOptionPane.showMessageDialog(null, "그룹 가입 실패(예상치 못한 오류)", "Warning", JOptionPane.WARNING_MESSAGE);
			}
		}
		else if(command.equals("그룹 탈퇴")) {
			ArrayList<Group> groups = SDAO.getInstance().getMyGroups(user.getID());	// 사용자 아이디를 인수로 DAO 객체에서 그룹 ArrayList 반환
			Object[] options = groups.toArray();	// ArrayList를 Object 배열로 변환
			Group selectedGroup = (Group)JOptionPane.showInputDialog(null, "탈퇴할 그룹을 선택해주세요", "그룹 탈퇴", JOptionPane.QUESTION_MESSAGE, null, options, null);
			
			if(selectedGroup != null) {
				boolean result = SDAO.getInstance().leaveGroup(user.getID(), selectedGroup.getGroupId());	// 사용자 아이디와 그룹 아이디를 인수로 DAO 객체에서 그룹 탈퇴 성공 여부 반환
				
				// 그룹 탈퇴 성공 시
				if(result) {
					JOptionPane.showMessageDialog(null, "그룹 탈퇴 성공", "Information", JOptionPane.PLAIN_MESSAGE);
					groupMainPanel.refreshGroupList();	// 현재 그룹 목록 갱신
				}
				// 그룹 탈퇴 실패 시
				else {
					JOptionPane.showMessageDialog(null, "그룹 탈퇴 실패(예상치 못한 오류)", "Warning", JOptionPane.WARNING_MESSAGE);
				}
			}
		}
		else if(command.equals("사용자 정보 관리")) {
			new EditUserInfoDialog(this, "사용자 정보 관리", user);	// 현재 User 객체를 전달
		}
		else if(command.equals("프로그램 종료")) {
			// YES == 0, NO == 1, 팝업 끄기 == -1
			int var = JOptionPane.showConfirmDialog(null, "정말 종료하시겠습니까?", "종료", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
			
			if(var == JOptionPane.YES_OPTION) {
				dispose();	// 종료
			}
		}
	}
}