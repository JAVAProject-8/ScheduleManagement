package GUI;

import DB.User;
import DB.SDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;
import java.util.Random;

public class MainFrame extends JFrame implements ActionListener {
	User user;
	// JTabbedPane에 추가될 패널을 필드로 선언 필요
	
	public MainFrame() {
	//public MainFrame(User _u) {
		Container ct = getContentPane();
		//user = _u;	// 현재 로그인 한 사용자를 식별하기 위한 객체 저장
		
		// 메뉴 바 설정
		JMenuBar menuBar = new JMenuBar();
		//menuBar.setBackground(Color.WHITE);
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
		ct.add(tabbedPane);
		
		tabbedPane.addTab("메인", new JPanel());
		tabbedPane.addTab("시간표", new JPanel());
		tabbedPane.addTab("캘린더", new JPanel());
		tabbedPane.addTab("그룹 메인", new GroupMainPanel());
		tabbedPane.addTab("그룹 시간표", new JPanel());
		
		// ActionListener 등록
		addScheduleMenuItem.addActionListener(this);
		checkScheduleMenuItem.addActionListener(this);
		createGroupMenuItem.addActionListener(this);
		joinGroupMenuItem.addActionListener(this);
		leaveGrupMenuItem.addActionListener(this);
		editUserInfoMenuItem.addActionListener(this);
		exitMenuItem.addActionListener(this);
		
		setTitle("일정 관리 프로그램");
		//pack();
		setSize(500, 500);
		setResizable(false);	// 크기 조정
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	// 종료 시 처리
		setLocationRelativeTo(null);	// 모니터 중앙 표시
		setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		
		if(command.equals("일정 추가")) {
			new AddScheduleDialog(this, "일정 추가");
			// 일정 추가는 메뉴 외에 JTable 에서 MouseEvent로도 호출될 수 있음. 이 경우 생성자에 시각 시간을 전달
			// 또는 캘린더에서 호출될 수 있음. 이 경우 생성자에 날짜를 전달
		}
		else if(command.equals("전체 일정 조회")) {
			new CheckScheduleDialog(this, "전체 일정 조회");
		}
		else if(command.equals("그룹 생성")) {
			String groupId = JOptionPane.showInputDialog(null, "그룹 아이디를 입력해주세요", "그룹 생성", JOptionPane.QUESTION_MESSAGE);
			if(groupId == null) {
				return;
			}
			String groupName = JOptionPane.showInputDialog(null, "그룹명을 입력해주세요", "그룹 생성", JOptionPane.QUESTION_MESSAGE);
			if(groupName == null) {
				return;
			}
			
			// 초대 코드를 생성
			Random random = new Random();
			String characterSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
			String inviteCode = "";
			for(int i = 0; i < 10; i++) {
				inviteCode += characterSet.charAt(random.nextInt(characterSet.length()));
			}
			//JOptionPane.showMessageDialog(null, inviteCode);
			
			// 그룹 아이디와 그룹 명, 초대 코드를 DB에 전달
			// 그룹 아이디가 중복될 경우 생성 불가, 재입력 필요
		}
		else if(command.equals("그룹 가입")) {
			String inviteCode = JOptionPane.showInputDialog(null, "초대코드를 입력해주세요", "그룹 가입", JOptionPane.QUESTION_MESSAGE);
			if(inviteCode == null) {
				return;
			}
			
			// DAO에게 입력받은 초대 코드와 사용자 아이디를 전달
			// 그룹이 존재할 시 데이터베이스에 추가 후 성공 반환, 부재 시 실패 반환
			
		}
		else if(command.equals("그룹 탈퇴")) {
			// DB에서 현재 로그인 한 사용자가 속해 있는 그룹명을 배열 형태로 받아옴
			
			String[] testArray = {"abc", "def", "ghi"};	// 테스트용 임시 문자열 배열
			String leaveGroupName = (String)JOptionPane.showInputDialog(null, "탈퇴할 그룹을 선택해주세요", "그룹 탈퇴", JOptionPane.QUESTION_MESSAGE, null, testArray, null);
			
			// DAO에게 선택한 그룹의 ID와 사용자 아이디를 전달
			// 그룹명과 ID를 동시에 받아오려면 그룹 객체에 필드를 선언해서 리스트로..
			// DB의 그룹원 테이블에서 해당 사용자를 삭제 처리
		}
		else if(command.equals("사용자 정보 관리")) {
			new EditUserInfoDialog(this, "사용자 정보 관리");
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