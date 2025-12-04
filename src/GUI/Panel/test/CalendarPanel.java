package GUI.Panel.test;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;

// TODO : 메인 기능은 구현되었지만 캘린터 디자인 수정 및 사용자가 입력한 일정을 DB에 산입한다.
// TODO : 일 버튼을 클릭하면 테이블이 표시해야함
public class CalendarPanel extends JPanel {
    private JLabel monthLabel;
    private JPanel datePanel;

    private LocalDate today = LocalDate.now();
    private LocalDate currentMonth = LocalDate.of(today.getYear(), today.getMonth(), 1);

    public CalendarPanel() {
        setLayout(new BorderLayout());

        // 상단 월 표시
        monthLabel = new JLabel(
            currentMonth.getYear() + "년 " + currentMonth.getMonthValue() + "월",
            SwingConstants.CENTER
        );
        add(monthLabel, BorderLayout.NORTH);

        // 날짜 버튼 영역
        datePanel = new JPanel(new GridLayout(0, 7));
        add(datePanel, BorderLayout.CENTER);

        buildCalendar();
    }

    private void buildCalendar() {
        datePanel.removeAll();

        int lastDay = currentMonth.lengthOfMonth();

        for (int d = 1; d <= lastDay; d++) {
            int day = d;
            JButton btn = new JButton(String.valueOf(day));
            btn.setBackground(Color.WHITE);
            btn.setFocusPainted(false);

            btn.addActionListener((ActionEvent e) -> openScheduleDialog(day));
            datePanel.add(btn);
        }

        datePanel.revalidate();
        datePanel.repaint();
    }

    private void openScheduleDialog(int day) {
        DefaultTableModel tableModel;
        JTable scheduleTable; // 일정 테이블

        tableModel = new DefaultTableModel();
        tableModel.addColumn("구분");
        tableModel.addColumn("내용");
        tableModel.addColumn("시작");
        tableModel.addColumn("종료");

        // 테스트 모델
		// DB에서 받은 일정 ArrayList를 바탕으로 테이블 모델을 생성하는 메소드 필요
		for(int i = 1; i <= 12; i++) {
			tableModel.addRow(new Object[] {"수업", String.valueOf(i), "2025-01-01", "2025-12-31"});
		}
		
		// 테이블 설정
		scheduleTable = new JTable(tableModel);
		scheduleTable.setDragEnabled(false);

        // 테이블 열 크기
        scheduleTable.setRowHeight(20);
        // 수평선 표시
        scheduleTable.setShowHorizontalLines(true);
        // 수직선 표시
        scheduleTable.setShowVerticalLines(true);
        // 그리드선 색상을 희색으로 변경
        scheduleTable.setGridColor(Color.GRAY);
        // 셀 사이 간격 설정
        scheduleTable.setIntercellSpacing(new java.awt.Dimension(1, 1));
        // 드래그 앤 드롭 지금
        scheduleTable.setDragEnabled(false);
        // 테이블 이동 불가
        scheduleTable.getTableHeader().setReorderingAllowed(false);
        // 테이블 크기조정 불기
        scheduleTable.getTableHeader().setResizingAllowed(false);

        // 테이블 행 선택
        scheduleTable.addMouseListener((MouseListener) new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = scheduleTable.getSelectedRow();
                if (row >= 0) {
                    String col1 = scheduleTable.getValueAt(row, 0).toString();
                    String col2 = scheduleTable.getValueAt(row, 1).toString();
                    String col3 = scheduleTable.getValueAt(row, 2).toString();
                    String col4 = scheduleTable.getValueAt(row, 3).toString();

                    System.out.println(
                        col1 + ", " +
                        col2 + ", " +
                        col3 + ", " +
                        col4 + ", " +
                        currentMonth.getYear() + ", " +
                        currentMonth.getMonthValue() + ", " +
                        day
                    );
                }
            }
        });

		// 스크롤 패널 등록
		JScrollPane scheduleScroll = new JScrollPane(scheduleTable);
		scheduleScroll.setPreferredSize(new Dimension(0, 250));
		//scheduleScroll.setBorder(new EmptyBorder(20, 20, 20, 20));
		
        String[] butTitle = {
            "추가", "수정", "삭제"
        };

        int result = JOptionPane.showOptionDialog(
            this, scheduleTable, day + "일 일정 등록", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, butTitle, butTitle[0]
        );
            
        if (result == JOptionPane.YES_OPTION) {
            // TODO : AddScheduleDialog 생성자 호출해서 년, 월, 일 입력란에 자동 완성
            System.out.println(day + "일 일정 추가 완료");
        }
        if (result == JOptionPane.NO_OPTION) {
            // TODO : 테이블에서 선택한 일정을 수정
            // TODO : AddScheduleDialog 생성자 호출해서 입력란에 자동 완성
            // 테이블에 MouseListener 추가
        
            System.out.println(day + "일 일정 수정 완료");
        }
        if (result == JOptionPane.CANCEL_OPTION) {
            System.out.println(day + "일 일정 취소 완료");
        }
    }

    public void mouseClickedT(JTable table) {
        int row = table.getSelectedRow();
        // int col = table.getSelectedColumn();
        for (int i = 0; i < table.getColumnCount(); i++) {
            System.out.print(table.getModel().getValueAt(row, i )+"\t"); 
        } 
    }
}