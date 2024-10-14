package interfaces;

import model.Trajectory;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class FilePanel extends JPanel {
    private JLabel lable;
    private static JLabel fileName;
    private static JTextArea text;
    private JPanel panel;
    private JScrollPane scroll;

    public FilePanel(){
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(200, 400));

        panel = new JPanel(new BorderLayout());
        lable = new JLabel("", SwingConstants.CENTER);
        fileName = new JLabel("", SwingConstants.CENTER);
        text = new JTextArea();
        scroll = new JScrollPane(text);

        text.setFont(text.getFont().deriveFont(8f));
        text.setEditable(false);
        addListenerToText();
        lable.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        fileName.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        scroll.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        panel.add(fileName, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        add(lable, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
    }

    private void addListenerToText() {
        text.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
                SettingsPanel.setFlag(true);
            }

            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
                SettingsPanel.setFlag(true);
            }

            @Override
            public void changedUpdate(DocumentEvent documentEvent) {
                SettingsPanel.setFlag(true);
            }
        });
    }

    public static void setTextAtPanel(Trajectory trajectory) {
        fileName.setText(trajectory.getFilePath());
        text.setText("");
        for (String string : trajectory.getFileData()) {
            text.append(string);
            text.append("\n");
        }
    }

    public static String getTextAtPanel() {
        return text.getText();
    }

    public static void setFileName(String path) {
        fileName.setText(path);
    }

    public static void loadDataFromTable(List<String> data){
        StringBuilder stringBuilder = new StringBuilder();
        for (String string : data){
            stringBuilder.append(string);
            stringBuilder.append('\n');
        }
        text.setText(stringBuilder.toString());
    }

    public static void clearText() {
        fileName.setText("");
        text.setText("");
        SettingsPanel.setFlag(false);
    }
}
