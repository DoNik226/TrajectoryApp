package interfaces;

import model.Point;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

public class TablePanel extends JPanel {
    private JLabel lable;
    private static String[] columNames;
    private static Double[][] data;
    private JScrollPane scroll;
    private static DefaultTableModel tableModel;
    private static String newValue;
    private static String oldValue;
    private static JTable table;

    public TablePanel(){
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(200, 240));

        lable = new JLabel("Таблица", SwingConstants.CENTER);
        columNames = new String[]{"T, с", "X, м", "Y, м", "Z, м", "Vx, м/с", "Vy, м/с", "Vz, м/с"};
        tableModel = new DefaultTableModel(null, columNames);
        setTable();
        addListenerToTable();
        scroll = new JScrollPane(table);

        lable.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        scroll.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        add(lable, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
    }

    public static void setTableModel(List<Point> points) {
        data = new Double[points.size()][7];
        int i=0;
        tableModel.setRowCount(0);
        for (Point point : points) {
            data[i] = new Double[]{point.getTime(), point.getXCoordinate(), point.getYCoordinate(), point.getZCoordinate(), point.getXVelocity(), point.getYVelocity(), point.getZVelocity()};
            tableModel.addRow(data[i]);
            i++;
        }
        tableModel.fireTableDataChanged();
    }

    public static void clearTable() {
        tableModel.setRowCount(0);
        tableModel.fireTableDataChanged();
    }

    private void updateFilePanel() {
        List<String> data = new ArrayList<>();
        for (int i=0; i<table.getRowCount(); i++) {
            StringBuilder s = new StringBuilder();
            for (int j=0; j<table.getColumnCount(); j++) {
                s.append(table.getValueAt(i, j));
                s.append("  ");
            }
            data.add(s.toString());
        }
        FilePanel.loadDataFromTable(data);
    }

    private void addListenerToTable() {
        tableModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent tableModelEvent) {
                int row = table.getSelectedRow();
                int col = table.getSelectedColumn();
                if (tableModel.getRowCount() != 0 && table.getSelectedColumn() != -1 && table.getSelectedRow() != -1) {
                    String datas = table.getValueAt(row, col).toString();
                    Pattern pattern = Pattern.compile("-?([0]|[1-9][0-9]*)(\\.[0-9]+)?");
                    Matcher matcher = pattern.matcher(datas);
                    if (!matcher.matches()) {
                        table.setValueAt(oldValue, row, col);
                    }
                }
                updateFilePanel();
            }
        });
    }

    private void setTable() {
        table = new JTable(tableModel) {
            @Override
            public Component prepareEditor(TableCellEditor editor, int row, int column) {
                oldValue = tableModel.getValueAt(row, column).toString();
                JTextField component = (JTextField) super.prepareEditor(editor, row, column);
                component.getDocument().addDocumentListener(new DocumentListener() {
                    @Override
                    public void insertUpdate(DocumentEvent documentEvent) {
                        newValue = component.getText();
                        if (newValue != null && !isValidValue(newValue)) {
                            component.setBackground(Color.RED);
                        } else {
                            component.setBackground(Color.WHITE);
                        }
                    }

                    @Override
                    public void removeUpdate(DocumentEvent documentEvent) {
                        newValue = component.getText();
                        if (newValue != null && !isValidValue(newValue)) {
                            component.setBackground(Color.RED);
                        } else {
                            component.setBackground(Color.WHITE);
                        }
                    }

                    @Override
                    public void changedUpdate(DocumentEvent documentEvent) {
                        newValue = component.getText();
                        if (newValue != null && !isValidValue(newValue)) {
                            component.setBackground(Color.RED);
                        } else {
                            component.setBackground(Color.WHITE);
                        }
                    }
                });
                return component;
            }
            private boolean isValidValue(String value) {
                Pattern pattern = Pattern.compile(("-?([0]|[1-9][0-9]*)(\\.[0-9]+)?"));
                Matcher matcher = pattern.matcher(value);
                return matcher.matches();
            }
        };
    }
}
