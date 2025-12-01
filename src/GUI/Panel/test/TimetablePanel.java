package GUI.Panel.test;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

// TODO : DB에서 사용자를 일정 가져오고 테이블에 요일과 시간에 맞게 배치하고 색상으로 구분한다.

public class TimetablePanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    // 요일
    private final String[] DAYS = { "시간", "월", "화", "수", "목", "금" };
    // 시간
    private final String[] TIMES = {
            "09:00", "",
            "10:00", "",
            "11:00", "",
            "12:00", "",
            "13:00", "",
            "14:00", "",
            "15:00", "",
            "16:00", "",
            "17:00", "",
            "18:00", ""
    };
    public TimetablePanel() {
        setLayout(new BorderLayout());

        model = new DefaultTableModel(DAYS, 0);
        for (String t : TIMES) {
            // 시간과 공백 
            model.addRow(new Object[] { t, "", "", "", "", "" });
        }

        // 테이블 생성
        table = new JTable(model);
        // 수평선 표시
        table.setShowHorizontalLines(true);
        // 수직선 표시
        table.setShowVerticalLines(true);
        // 그리드선 색상을 희색으로 변경
        table.setGridColor(Color.GRAY);
        // 셀 사이 간격 설정
        table.setIntercellSpacing(new java.awt.Dimension(1, 1));
        // 드래그 앤 드롭 지금
        table.setDragEnabled(false);
        // 패널에 추가
        add(new JScrollPane(table), BorderLayout.CENTER);
    }
}
