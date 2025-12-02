package GUI;

import javax.swing.*;

public class ScheduleInformation {
	// 메인 메소드
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());	// Java 기본 Look And Feel 설정
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		new LoginFrame();
		//new MainFrame();
	}
}