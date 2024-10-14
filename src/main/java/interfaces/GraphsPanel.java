package interfaces;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleInsets;

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
    private XYLineAndShapeRenderer renderer;

    private static XYSeries s1;
    private static XYSeries s2;
    private static XYSeries s3;
    private static XYSeries s4;
    private static XYSeries s5;
    private static XYSeries s6;

    public GraphsPanel(){
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(200, 300));
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

        addListenerToButtons();
        buttonsPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        lable.setBorder(BorderFactory.createLineBorder(Color.GRAY));

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
        add(createGraph(), BorderLayout.CENTER);
    }

    private void addListenerToButtons() {
        x.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (x.isSelected()){
                    renderer.setSeriesLinesVisible(0, true);
                    renderer.setSeriesLinesVisible(3, false);
                    renderer.setSeriesLinesVisible(4, false);
                    renderer.setSeriesLinesVisible(5, false);
                    vX.setSelected(false);
                    vY.setSelected(false);
                    vZ.setSelected(false);
                } else {
                    renderer.setSeriesLinesVisible(0, false);
                }
            }
        });
        y.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (y.isSelected()){
                    renderer.setSeriesLinesVisible(1, true);
                    renderer.setSeriesLinesVisible(3, false);
                    renderer.setSeriesLinesVisible(4, false);
                    renderer.setSeriesLinesVisible(5, false);
                    vX.setSelected(false);
                    vY.setSelected(false);
                    vZ.setSelected(false);
                } else {
                    renderer.setSeriesLinesVisible(1, false);
                }
            }
        });
        z.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (z.isSelected()){
                    renderer.setSeriesLinesVisible(2, true);
                    renderer.setSeriesLinesVisible(3, false);
                    renderer.setSeriesLinesVisible(4, false);
                    renderer.setSeriesLinesVisible(5, false);
                    vX.setSelected(false);
                    vY.setSelected(false);
                    vZ.setSelected(false);
                } else {
                    renderer.setSeriesLinesVisible(2, false);
                }
            }
        });
        vX.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (vX.isSelected()){
                    renderer.setSeriesLinesVisible(3, true);
                    renderer.setSeriesLinesVisible(0, false);
                    renderer.setSeriesLinesVisible(1, false);
                    renderer.setSeriesLinesVisible(2, false);
                    x.setSelected(false);
                    y.setSelected(false);
                    z.setSelected(false);
                } else {
                    renderer.setSeriesLinesVisible(3, false);
                }
            }
        });
        vY.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (vY.isSelected()){
                    renderer.setSeriesLinesVisible(4, true);
                    renderer.setSeriesLinesVisible(0, false);
                    renderer.setSeriesLinesVisible(1, false);
                    renderer.setSeriesLinesVisible(2, false);
                    x.setSelected(false);
                    y.setSelected(false);
                    z.setSelected(false);
                } else {
                    renderer.setSeriesLinesVisible(4, false);
                }
            }
        });
        vZ.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (vZ.isSelected()){
                    renderer.setSeriesLinesVisible(5, true);
                    renderer.setSeriesLinesVisible(0, false);
                    renderer.setSeriesLinesVisible(1, false);
                    renderer.setSeriesLinesVisible(2, false);
                    x.setSelected(false);
                    y.setSelected(false);
                    z.setSelected(false);
                } else {
                    renderer.setSeriesLinesVisible(5, false);
                }
            }
        });
    }

    private ChartPanel createGraph(){
        JFreeChart chart = createChart(creatDataset());
        chart.setPadding(new RectangleInsets(4, 8, 2, 2));
        ChartPanel panel = new ChartPanel(chart);
        panel.setFillZoomRectangle(true);
        panel.setMouseWheelEnabled(true);
        return panel;
    }

    private XYDataset creatDataset() {
        s1 = new XYSeries("x");
        s2 = new XYSeries("y");
        s3 = new XYSeries("z");
        s4 = new XYSeries("vx");
        s5 = new XYSeries("vy");
        s6 = new XYSeries("vz");


        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(s1);
        dataset.addSeries(s2);
        dataset.addSeries(s3);
        dataset.addSeries(s4);
        dataset.addSeries(s5);
        dataset.addSeries(s6);

        return dataset;
    }

    private JFreeChart createChart(XYDataset dataset) {
        JFreeChart chart = ChartFactory.createXYLineChart("", "", "", dataset);
        chart.setBackgroundPaint(Color.WHITE);

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setDomainGridlinePaint(Color.GRAY);
        plot.setRangeGridlinePaint(Color.GRAY);
        plot.setBackgroundPaint(Color.WHITE);
        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);

        renderer = new XYLineAndShapeRenderer();
        renderer.setBaseShapesFilled(true);
        renderer.setBaseLinesVisible(true);
        renderer.setDrawSeriesLineAsPath(true);
        renderer.setSeriesShapesVisible(0, false);
        renderer.setSeriesShapesVisible(1, false);
        renderer.setSeriesShapesVisible(2, false);
        renderer.setSeriesShapesVisible(3, false);
        renderer.setSeriesShapesVisible(4, false);
        renderer.setSeriesShapesVisible(5, false);
        renderer.setSeriesPaint(3, Color.RED);
        renderer.setSeriesPaint(4, Color.BLUE);
        renderer.setSeriesPaint(5, Color.GREEN);
        if (!x.isSelected()) {
            renderer.setSeriesLinesVisible(0, false);
        }
        if (!y.isSelected()) {
            renderer.setSeriesLinesVisible(1, false);
        }
        if (!z.isSelected()) {
            renderer.setSeriesLinesVisible(2, false);
        }
        if (!vX.isSelected()) {
            renderer.setSeriesLinesVisible(3, false);
        }
        if (!vY.isSelected()) {
            renderer.setSeriesLinesVisible(4, false);
        }
        if (!vZ.isSelected()) {
            renderer.setSeriesLinesVisible(5, false);
        }
        plot.setRenderer(renderer);
        chart.removeLegend();
        return chart;
    }

    public static void setCoord(){
        s1.clear();
        s2.clear();
        s3.clear();
        s4.clear();
        s5.clear();
        s6.clear();
        Double[][] data = TablePanel.getTableData();
        for (int i=0; i< data.length; i++) {
            s1.add(data[i][0], data[i][1]);
            s2.add(data[i][0], data[i][2]);
            s3.add(data[i][0], data[i][3]);
            s4.add(data[i][0], data[i][4]);
            s5.add(data[i][0], data[i][5]);
            s6.add(data[i][0], data[i][6]);
        }
    }

    public static void clearGraph() {
        s1.clear();
        s2.clear();
        s3.clear();
        s4.clear();
        s5.clear();
        s6.clear();
    }
}
