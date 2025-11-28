package GUI.Panel.test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;

// TODO : 메인 기능은 구현되었지만 캘린터 디자인 수정 및 사용자가 입력한 일정을 DB에 산입한다.

public class CalendarPanel extends JPanel {

    private JLabel monthLabel;
    private JPanel datePanel;

    private LocalDate today = LocalDate.now();
    private LocalDate currentMonth = LocalDate.of(today.getYear(), today.getMonth(), 1);

    public CalendarPanel() {
        setLayout(new BorderLayout());

        // 상단 월 표시
        monthLabel = new JLabel(currentMonth.getYear() + "년 " + currentMonth.getMonthValue() + "월",
                SwingConstants.CENTER);
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

            btn.addActionListener((ActionEvent e) -> openScheduleDialog(day));
            datePanel.add(btn);
        }

        datePanel.revalidate();
        datePanel.repaint();
    }

    private void openScheduleDialog(int day) {
        JTextField titleField = new JTextField();
        JTextArea memoArea = new JTextArea(5, 20);

        Object[] msg = {
                "일정 제목:", titleField,
                "메모:", new JScrollPane(memoArea)
        };

        int result = JOptionPane.showConfirmDialog(
                this, msg, day + "일 일정 등록", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            // TODO: 일정 DB/파일 저장 처리
            System.out.println(day + "일 일정 저장 완료: " + titleField.getText());
        }
    }
}