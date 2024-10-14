package interfaces;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class GraphsPanel extends JPanel {
    private JLabel graphLable;
    private JLabel lable;
    private JCheckBox x;
    private JCheckBox y;
    private JCheckBox z;
    private JCheckBox vX;
    private JCheckBox vY;
    private JCheckBox vZ;
    private JLabel coordLable;
    private JLabel speedLable;
    private JPanel panel;
    private JPanel buttonsPanel;

    public GraphsPanel(){
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(200, 300));

        graphLable = new JLabel("Здесь будет график", SwingConstants.CENTER);
        lable = new JLabel("График", SwingConstants.CENTER);
        x = new JCheckBox("X,м");
        y = new JCheckBox("Y,м");
        z = new JCheckBox("Z,м        ");
        vX = new JCheckBox("Vx,м/с");
        vY = new JCheckBox("Vy,м/с");
        vZ = new JCheckBox("Vz,м/с");
        coordLable = new JLabel("Координаты: ");
        speedLable = new JLabel("Проекции скорости:  ");
        panel = new JPanel(new BorderLayout());
        buttonsPanel = new JPanel();

        addActionToButtons();
        buttonsPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        lable.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        graphLable.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        buttonsPanel.add(coordLable);
        buttonsPanel.add(x);
        buttonsPanel.add(y);
        buttonsPanel.add(z);
        buttonsPanel.add(speedLable);
        buttonsPanel.add(vX);
        buttonsPanel.add(vY);
        buttonsPanel.add(vZ);
        panel.add(lable, BorderLayout.NORTH);
        panel.add(buttonsPanel, BorderLayout.CENTER);
        add(panel, BorderLayout.NORTH);
        add(graphLable, BorderLayout.CENTER);
    }

    private void addActionToButtons() {
        x.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (x.isSelected()){
                    vX.setSelected(false);
                    vY.setSelected(false);
                    vZ.setSelected(false);
                }
            }
        });
        y.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (y.isSelected()){
                    vX.setSelected(false);
                    vY.setSelected(false);
                    vZ.setSelected(false);
                }
            }
        });
        z.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (z.isSelected()){
                    vX.setSelected(false);
                    vY.setSelected(false);
                    vZ.setSelected(false);
                }
            }
        });
        vX.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (vX.isSelected()){
                    x.setSelected(false);
                    y.setSelected(false);
                    z.setSelected(false);
                }
            }
        });
        vY.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (vY.isSelected()){
                    x.setSelected(false);
                    y.setSelected(false);
                    z.setSelected(false);
                }
            }
        });
        vZ.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (vZ.isSelected()){
                    x.setSelected(false);
                    y.setSelected(false);
                    z.setSelected(false);
                }
            }
        });
    }
}
