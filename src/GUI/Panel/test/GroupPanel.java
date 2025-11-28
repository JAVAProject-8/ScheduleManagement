package GUI.Panel.test;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

// TODO : DB에서 그룹 이름과 그룹원별 일정을 가져와 그룹 이름은 JComboBox에 추가하고, 그룹원 일정은 JComboBox으로 그룹을 선택에 해당 그룹원의 일정을 테이블에 요일과 시간에 맞게 배치하고 색상으로 구분한다.

public class GroupPanel extends JPanel {

    private JComboBox<String> groupBox;
    private JTable table;
    private DefaultTableModel model;

    public GroupPanel() {
        setLayout(new BorderLayout());

        // 상단 콤보박스
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        groupBox = new JComboBox<>();

        groupBox.addItem("1조");
        groupBox.addItem("2조");
        groupBox.addItem("3조");

        groupBox.addActionListener(e -> loadGroupData());

        top.add(new JLabel("그룹: "));
        top.add(groupBox);

        add(top, BorderLayout.NORTH);

        // 시간표 테이블 (기본 구조만 생성)
        String[] cols = { "시간", "요일", "담당자" };
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);

        // 사용자별 색상 구분 렌더러
        table.setDefaultRenderer(Object.class, new MemberColorRenderer());

        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void loadGroupData() {
        // TODO: 콤보박스 선택에 따른 시간표 데이터 로딩 
        model.setRowCount(0);
        model.addRow(new Object[] { "09:00", "월", "홍길동" });
        model.addRow(new Object[] { "10:00", "화", "김철수" });
    }

    // 팀원 이름에 따라 셀 색상 적용
    class MemberColorRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, col, col);

            String member = table.getValueAt(row, 2).toString();

            switch (member) {
                case "홍길동":
                    c.setBackground(new Color(255, 230, 230));
                    break;
                case "김철수":
                    c.setBackground(new Color(230, 230, 255));
                    break;
                default:
                    c.setBackground(Color.WHITE);
            }
            return c;
        }
    }
}
