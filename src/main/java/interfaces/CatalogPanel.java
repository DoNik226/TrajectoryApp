package interfaces;

import model.FileData;
import model.Trajectory;
import org.apache.commons.io.FilenameUtils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class CatalogPanel extends JPanel {
    private static DefaultListModel<String> listModel;
    private static JList<String> list;
    private JLabel lable;
    private JScrollPane scroll;
    private JPopupMenu popupMenu;
    private JMenuItem closeButton;
    private static List<Trajectory> trajectories = new ArrayList<>();

    public CatalogPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(200, 235));

        listModel = new DefaultListModel<>();
        list = new JList<>(listModel);
        popupMenu = new JPopupMenu();
        closeButton = new JMenuItem("Закрыть файл с выбранной траекторией");
        popupMenu.add(closeButton);
        scroll = new JScrollPane(list);
        lable = new JLabel("Каталог", SwingConstants.CENTER);

        addListenersToButtons();
        addMouseListenerToList();
        lable.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        scroll.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        add(lable, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
    }

    private void addListenersToButtons() {
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String selectedValue = list.getSelectedValue();
                int answer = JOptionPane.showConfirmDialog(null, new StringBuilder("Хотите закрыть данный файл: ").append(selectedValue).append('?'), "", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (answer == 0) {
                    if (SettingsPanel.getFlag()) {
                        answer = JOptionPane.showConfirmDialog(null, "Хотите сохранить изменения?", "", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if (answer == JOptionPane.YES_OPTION) {
                            SettingsPanel.saveFile();
                        }
                    }
                    if (SettingsPanel.getChosenTrajectory().getTrajectoryName().equals(selectedValue)) {
                        deleteTrajectory(SettingsPanel.getChosenTrajectory().getTrajectoryName());
                        FilePanel.clearText();
                        TablePanel.clearTable();
                        SettingsPanel.setFile(null);
                        SettingsPanel.setChosenTrajectory(null);
                        SettingsPanel.setFlag(false);
                    }
                    listModel.removeElement(selectedValue);
                }
            }
        });
    }

    private void addMouseListenerToList() {
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String selectedValue = list.getSelectedValue();
                if (selectedValue != null) {
                    if (e.getClickCount() == 2) {
                        for (Trajectory trajectory : getTrajectories()) {
                            if (trajectory.getTrajectoryName().equals(selectedValue)) {
                                if (SettingsPanel.getFile() != null && SettingsPanel.getFlag()) {
                                    int answer = JOptionPane.showConfirmDialog(null, "Хотите сохранить изменения?", "", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                                    if (answer == JOptionPane.YES_OPTION) {
                                        SettingsPanel.saveFile();
                                    }
                                }
                                SettingsPanel.setFile(new File(trajectory.getFilePath()));
                                SettingsPanel.openFile(SettingsPanel.getFile());
                                break;
                            }
                        }
                    } else if (SwingUtilities.isRightMouseButton(e)) {
                        popupMenu.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            }
        });
    }

    public static void addElementAtList(String element) {
        listModel.addElement(element);
    }

    public static boolean checkListModel(String element) {
        return listModel.contains(FilenameUtils.getBaseName(element));
    }

    public static void setListSelectedValue(String value) {
        list.setSelectedValue(value, false);
    }

    public static Trajectory addTrajectory(File file){
        FileData fileData = new FileData(file);
        if (fileData.fileIsCorrect()) {
            Trajectory trajectory = new Trajectory(fileData);
            trajectories.add(trajectory);
            return trajectory;
        } else {
            return null;
        }
    }

    public static void deleteTrajectory(String name){
        for (Trajectory trajectory : trajectories){
            if (trajectory.getTrajectoryName().equals(name)){
                trajectories.remove(trajectory);
                break;
            }
        }
    }

    public static List<Trajectory> getTrajectories(){
        return trajectories;
    }

    public static void printTrajectories(){
        for(Trajectory trajectory : trajectories){
            System.out.println(trajectory.toString());
        }
    }
}
