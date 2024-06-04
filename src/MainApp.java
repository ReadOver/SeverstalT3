import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class MainApp {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Supplier Data");

        JPanel panel1 = new JPanel();
        JButton button = new JButton("Open Supplier Data");
        panel1.add(button);

        JPanel panel2 = new JPanel();
        String[] suppliers = {"Supplier 1", "Supplier 2", "Supplier 3"};
        JComboBox<String> comboBox = new JComboBox<>(suppliers);
        panel2.add(comboBox);

        String[] columns = {"Column 1", "Column 2", "Column 3", "Column 4"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        JTable table = new JTable(tableModel);

        comboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    String item = (String) e.getItem();
                    tableModel.setRowCount(0); // clear table rows
                    if (item.equals("Supplier 1")) {
                        tableModel.addRow(new Object[]{"1", "1", "1", "1"});
                    } else if (item.equals("Supplier 2")) {
                        tableModel.addRow(new Object[]{"2", "2", "2", "2"});
                    } else if (item.equals("Supplier 3")) {
                        tableModel.addRow(new Object[]{"3", "3", "3", "3"});
                    }
                }
            }
        });

        panel2.add(table);

        button.addActionListener(e -> {
            frame.getContentPane().remove(panel1);
            frame.getContentPane().add(panel2);
            frame.pack();
        });

        frame.getContentPane().add(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setVisible(true);
    }
}