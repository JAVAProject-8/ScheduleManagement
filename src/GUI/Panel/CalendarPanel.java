package GUI.Panel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import DB.SDAO;
import DB.Schedule;
import DB.User;
import GUI.Dialog.ScheduleDialog;

import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class CalendarPanel extends JPanel {
    private JLabel monthLabel;
    private JPanel datePanel;

    private LocalDate today = LocalDate.now();
    private LocalDate currentMonth = LocalDate.of(today.getYear(), today.getMonth(), 1);
    
    private User u = null;
    private Schedule selectedSchedule = null;

    public CalendarPanel(User u) {
        this.u = u;

        setLayout(new BorderLayout());

        // 상단 월 표시
        monthLabel = new JLabel(
            currentMonth.getYear() + "년 " + currentMonth.getMonthValue() + "월",
            SwingConstants.CENTER
        );
        monthLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 20));
        add(monthLabel, BorderLayout.NORTH);

        // 날짜 버튼 영역
        datePanel = new JPanel(new GridLayout(0, 7));
        add(datePanel, BorderLayout.CENTER);

        buildCalendar();
    }

    // 캘린더 생성
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

    // 특정 날짜를 클릭할 때 일정 테이블 표시
    private void openScheduleDialog(int day) {
        selectedSchedule = null;
        ArrayList<Schedule> data = new ArrayList<>();

        DefaultTableModel tableModel;
        JTable scheduleTable; // 일정 테이블
        String[] t = { "구분", "내용", "시작", "종료" };

        tableModel = new DefaultTableModel(t, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 직접 편집 금지
            }
        };

        ArrayList<Schedule> schedules = SDAO.getInstance().getSchedules(u.getID());

        for (Schedule s : schedules) {
            if (
                s.getStartAt().getYear() == currentMonth.getYear() && 
                s.getStartAt().getMonthValue() == currentMonth.getMonthValue() &&
                s.getStartAt().getDayOfMonth() == day) {

                System.out.println(s.getStartAt().toLocalDate() + " -> " + LocalDate.of(currentMonth.getYear(), currentMonth.getMonthValue(), day));
                data.add(s);
                System.out.println(data.size());
            }
        }
        // data = TestDAO.getInstance().getScheduleDate(LocalDate.of(currentMonth.getYear(), currentMonth.getMonthValue(), day), u.getID());
        
        
        for (Schedule s : data) {
            tableModel.addRow(new Object[] { s.getScheduleType(), s.getScheduleDescription(), s.getStartAt().toLocalDate(), s.getEndAt().toLocalDate() });
            System.out.println("일정 -> 테이블에 추가");
        }
        

		// 테이블 설정
		scheduleTable = new JTable(tableModel);
		// scheduleTable.setDragEnabled(false);

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
                    selectedSchedule = data.get(row);
                }
                
            }
        });

		// 스크롤 패널 등록
		JScrollPane scheduleScroll = new JScrollPane(scheduleTable);
		
        // JOptionPane에 버튼 이름
        String[] butTitle = {
            "추가", "수정", "삭제"
        };

        // JOptionPane 설정
        int result = JOptionPane.showOptionDialog(
            this, 
            scheduleScroll, 
            day + "일 일정 등록", 
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            butTitle,
            butTitle[0]
        );

        // 일정 추가 버튼을 클릭할 때
        if (result == JOptionPane.YES_OPTION) {
            new ScheduleDialog(
                null, 
                "일정 추가",
                u,
                LocalDate.of(
                    currentMonth.getYear(), 
                    currentMonth.getMonthValue(), 
                    day
                )
            );
            
            // System.out.println(day + "일 일정 추가 완료");
        }

        // 일정 수정 버튼을 클릭할 때
        if (result == JOptionPane.NO_OPTION) {
            // 수정할 일정을 선택하고, 가져온 일정 리스트 크기가 0 이상이면
            if (selectedSchedule != null && data.size() > 0) {
                ScheduleDialog scheduleDialog = new ScheduleDialog(
                    null, 
                    "일정 수정",
                    u,
                    selectedSchedule
                );
                scheduleDialog.setVisible(true);
                // System.out.println(day + "일 일정 수정 완료");
            }
            
            // 가져온 일정 리스트 크기가 0이면
            else if (data.size() == 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "수정할 일정이 없습니다.",
                        "경고",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 수정할 일정을 선택하지 않았을 일때
            else {
                JOptionPane.showMessageDialog(
                    this,
                    "수정할 일정을 선택하세요.",
                    "경고",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }
        }

        // 일정 삭제를 클릭할 때
        if (result == JOptionPane.CANCEL_OPTION) {
            SDAO.getInstance().deleteSchedule(selectedSchedule.getScheduleId());
            // 삭제할 일정을 선택하고, 가져온 일정 리스트 크기가 0 이상이면
            if (selectedSchedule != null && data.size() > 0) {
                System.out.println(day + "일 일정 삭제 완료");
            }
            // 가져온 일정 리스트 크기가 0이면
            else if (data.size() == 0) {
                JOptionPane.showMessageDialog(
                    this,
                    "삭제할 일정을 없습니다.",
                    "경고",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            // 삭제할 일정을 선택하지 않았을 일때 
            else {
                JOptionPane.showMessageDialog(
                    this,
                    "삭제할 일정을 선택하세요.",
                    "경고",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }
        }
    }
}