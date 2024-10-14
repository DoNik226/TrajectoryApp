package interfaces;

import model.Point;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
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
    private JPopupMenu popupMenu;
    private JMenuItem insertLineBelow;
    private JMenuItem insertLineAbove;
    private JMenuItem removeLine;
    private JMenuItem showStatistics;

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

        popupMenu = new JPopupMenu();
        insertLineBelow = new JMenuItem("Вставить строку ниже");
        insertLineAbove = new JMenuItem("Вставить строку выше");
        removeLine = new JMenuItem("Удалить строку");
        showStatistics = new JMenuItem("Отобразить статистику");
        addListenersToButtons();
        popupMenu.add(insertLineAbove);
        popupMenu.add(insertLineBelow);
        popupMenu.add(removeLine);
        popupMenu.add(showStatistics);


        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)){
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

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
        GraphsPanel.setCoord();
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
                    sortTable();
                    GraphsPanel.setCoord();
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

    private void sortTable() {
        if (tableModel.getRowCount() != 0) {
            data = new Double[tableModel.getRowCount()][tableModel.getColumnCount()];
            for (int i=0; i<tableModel.getRowCount(); i++) {
                for (int j=0; j<tableModel.getColumnCount(); j++) {
                    data[i][j] = Double.parseDouble(tableModel.getValueAt(i, j).toString());
                }
            }
            tableModel.setRowCount(0);
            Arrays.sort(data, new Comparator<Double[]>() {
                @Override
                public int compare(Double[] firstString, Double[] secondString) {
                    return Double.compare(firstString[0], secondString[0]);
                }
            });
            for (int i = 0; i< data.length; i++) {
                tableModel.addRow(data[i]);
            }
        }
    }

    public static Double[][] getTableData() {
        return data;
    }

    public void setData(Double[][] data) {
        this.data = data;
    }

    private void addListenersToButtons() {
        insertLineAbove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow == -1 || table.getSelectedRowCount()>1) {
                    if (selectedRow == -1) {
                        JOptionPane.showMessageDialog(null, "Укажите, куда нужно вставить строку");
                    } else {
                        JOptionPane.showMessageDialog(null, "Выделено больше одной строки, укажите место вставки корректно");
                    }
                } else {
                    if (selectedRow != 0) {
                        Double time = ((Double) table.getValueAt(selectedRow, 0) + (Double) table.getValueAt(selectedRow - 1, 0)) / 2;
                        tableModel.insertRow(selectedRow, new Double[]{time, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0});
                    } else {
                        tableModel.insertRow(selectedRow, new Double[]{(Double) table.getValueAt(selectedRow, 0), 0.0, 0.0, 0.0, 0.0, 0.0, 0.0});
                    }
                }
            }
        });
        insertLineBelow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow == -1 || table.getSelectedRowCount()>1) {
                    if (selectedRow == -1) {
                        JOptionPane.showMessageDialog(null, "Укажите, куда нужно вставить строку");
                    } else {
                        JOptionPane.showMessageDialog(null, "Выделено больше одной строки, укажите место вставки корректно");
                    }
                } else {
                    if (selectedRow != table.getRowCount() - 1) {
                        Double time = ((Double) table.getValueAt(selectedRow, 0) + (Double) table.getValueAt(selectedRow + 1, 0)) / 2;
                        tableModel.insertRow(selectedRow+1, new Double[]{time, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0});
                    } else {
                        tableModel.insertRow(selectedRow+1, new Double[]{(Double) table.getValueAt(selectedRow, 0), 0.0, 0.0, 0.0, 0.0, 0.0, 0.0});
                    }
                }
            }
        });
        removeLine.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow == -1 || table.getSelectedRowCount()>1) {
                    if (selectedRow == -1) {
                        JOptionPane.showMessageDialog(null, "Укажите, какую строку необходимо удалить");
                    } else {
                        JOptionPane.showMessageDialog(null, "Выделено больше одной строки, укажите, какую строку хотите удалить ");
                    }
                } else {
                    tableModel.removeRow(selectedRow);
                }
            }
        });
        showStatistics.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String result = calculateData();
                JOptionPane.showMessageDialog(null, result);
            }
        });
    }

    public String calculateData() {
        double[] averageValue = new double[6];
        double[] dispersion = new double[6];
        double[] momentSecondOrder = new double[6];
        double[] momentThirdOrder = new double[6];


        for (int i=0; i<data.length; i++) {
            averageValue[0] += data[i][1];
            averageValue[1] += data[i][2];
            averageValue[2] += data[i][3];
            averageValue[3] += data[i][4];
            averageValue[4] += data[i][5];
            averageValue[5] += data[i][6];
        }
        averageValue[0] /= data.length;
        averageValue[1] /= data.length;
        averageValue[2] /= data.length;
        averageValue[3] /= data.length;
        averageValue[4] /= data.length;
        averageValue[5] /= data.length;

        for (int i=0; i<data.length; i++) {
            dispersion[0] += Math.pow(data[i][1] - averageValue[0], 2);
            dispersion[1] += Math.pow(data[i][2] - averageValue[1], 2);
            dispersion[2] += Math.pow(data[i][3] - averageValue[2], 2);
            dispersion[3] += Math.pow(data[i][4] - averageValue[3], 2);
            dispersion[4] += Math.pow(data[i][5] - averageValue[4], 2);
            dispersion[5] += Math.pow(data[i][6] - averageValue[5], 2);
            momentThirdOrder[0] += Math.pow(data[i][1] - averageValue[0], 3);
            momentThirdOrder[1] += Math.pow(data[i][2] - averageValue[1], 3);
            momentThirdOrder[2] += Math.pow(data[i][3] - averageValue[2], 3);
            momentThirdOrder[3] += Math.pow(data[i][4] - averageValue[3], 3);
            momentThirdOrder[4] += Math.pow(data[i][5] - averageValue[4], 3);
            momentThirdOrder[5] += Math.pow(data[i][6] - averageValue[5], 3);
        }
        for (int i=0; i<6; i++) {
            momentSecondOrder[i] = dispersion[i]/data.length;
            momentThirdOrder[i] /= data.length;
            dispersion[i] /= data.length - 1;
        }

        StringBuilder s = new StringBuilder("Статистические данные по 6 параметрам:\n\n");
        s.append("Среднее значение x: ");
        s.append(averageValue[0]);
        s.append("\n");
        s.append("Дисперсия x: ");
        s.append(dispersion[0]);
        s.append("\n");
        s.append("Момент второго порядка x: ");
        s.append(momentSecondOrder[0]);
        s.append("\n");
        s.append("Момент третьего порядка x: ");
        s.append(momentThirdOrder[0]);
        s.append("\n\n");
        s.append("Среднее значение y: ");
        s.append(averageValue[1]);
        s.append("\n");
        s.append("Дисперсия y: ");
        s.append(dispersion[1]);
        s.append("\n");
        s.append("Момент второго порядка y: ");
        s.append(momentSecondOrder[1]);
        s.append("\n");
        s.append("Момент третьего порядка y: ");
        s.append(momentThirdOrder[1]);
        s.append("\n\n");
        s.append("Среднее значение z: ");
        s.append(averageValue[2]);
        s.append("\n");
        s.append("Дисперсия z: ");
        s.append(dispersion[2]);
        s.append("\n");
        s.append("Момент второго порядка z: ");
        s.append(momentSecondOrder[2]);
        s.append("\n");
        s.append("Момент третьего порядка z: ");
        s.append(momentThirdOrder[2]);
        s.append("\n\n");
        s.append("Среднее значение vX: ");
        s.append(averageValue[3]);
        s.append("\n");
        s.append("Дисперсия vX: ");
        s.append(dispersion[3]);
        s.append("\n");
        s.append("Момент второго порядка vX: ");
        s.append(momentSecondOrder[3]);
        s.append("\n");
        s.append("Момент третьего порядка vX: ");
        s.append(momentThirdOrder[3]);
        s.append("\n\n");
        s.append("Среднее значение vY: ");
        s.append(averageValue[4]);
        s.append("\n");
        s.append("Дисперсия vY: ");
        s.append(dispersion[4]);
        s.append("\n");
        s.append("Момент второго порядка vY: ");
        s.append(momentSecondOrder[4]);
        s.append("\n");
        s.append("Момент третьего порядка vY: ");
        s.append(momentThirdOrder[4]);
        s.append("\n\n");
        s.append("Среднее значение vZ: ");
        s.append(averageValue[5]);
        s.append("\n");
        s.append("Дисперсия vZ: ");
        s.append(dispersion[5]);
        s.append("\n");
        s.append("Момент второго порядка vZ: ");
        s.append(momentSecondOrder[5]);
        s.append("\n");
        s.append("Момент третьего порядка vZ: ");
        s.append(momentThirdOrder[5]);
        s.append("\n");
        return s.toString();
    }
}
