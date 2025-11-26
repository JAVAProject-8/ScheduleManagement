package GUI;

import DB.SDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;

public class MainFrame extends JFrame {
	
	public MainFrame() {
		Container ct = getContentPane();
		
		// 로그인 후 사용자를 식별하기 위한 객체 전달 필요(ID 등)
		
		setTitle("메인");
		pack();
		setResizable(false);	// 크기 조정
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	// 종료 시 처리
		setLocationRelativeTo(null);	// 모니터 중앙 표시
		setVisible(true);
	}
}
