package interfaces;

import model.Trajectory;
import org.apache.commons.io.FilenameUtils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import javax.swing.*;

public class SettingsPanel extends JPanel {
    private JPanel menuPanel;
    private JMenuBar menuBar;
    private JMenu menu;
    private JMenuItem openButton;
    private JMenuItem saveButton;
    private JMenuItem saveAsButton;
    private static File file;
    private static boolean textWasChanged;
    private static Trajectory chosenTrajectory;
    private static final String PATH_TO_OPENED_FILES = "openedFiles/openedFiles";
    private static List<String> openedFiles;
    private JList<String> list;
    private static DefaultListModel<String> listModel;
    private JMenu listMenu;

    public SettingsPanel(){
        setLayout(new BorderLayout());
        menuPanel = new JPanel(new BorderLayout());
        menuBar = new JMenuBar();
        menu = new JMenu("Файл");
        openButton = new JMenuItem("Открыть");
        saveButton = new JMenuItem("Сохранить");
        saveAsButton = new JMenuItem("Сохранить как");
        textWasChanged = false;
        openedFiles = new ArrayList<>();

        listModel = new DefaultListModel<>();
        list = new JList<>(listModel);
        addMouseListenerToList();
        listMenu = new JMenu("Открыть недавние");
        listMenu.add(list);

        addActionToButtons();
        addComponentsToPanel();
        readPreviouslyOpenFiles();

        setVisible(true);
        setBackground(new Color(90, 100, 230));
        setSize(1000, 500);
    }

    private void addComponentsToPanel() {
        menu.add(openButton);
        menu.add(listMenu);
        menu.add(saveButton);
        menu.add(saveAsButton);
        menuBar.add(menu);
        menuPanel.add(menuBar, BorderLayout.NORTH);
        add(menuPanel, BorderLayout.WEST);
    }

    public static void setChosenTrajectory(Trajectory trajectory) {
        chosenTrajectory = trajectory;
    }

    public static void setFlag(boolean newFlag) {
        textWasChanged = newFlag;
    }

    public static boolean getFlag() {
        return textWasChanged;
    }

    public static Trajectory getChosenTrajectory() {
        return chosenTrajectory;
    }

    public static List<String> getOpenedFiles() { return openedFiles; }

    private void addMouseListenerToList() {
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String selectedValue = list.getSelectedValue();
                    for (String pathToFile : openedFiles) {
                        if (FilenameUtils.getBaseName(pathToFile).equals(selectedValue)) {
                            if (file != null && textWasChanged) {
                                int answer = JOptionPane.showConfirmDialog(null, "Хотите сохранить изменения?", "", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                                if (answer == JOptionPane.YES_OPTION) {
                                    saveFile();
                                }
                            }
                            file = new File(pathToFile);
                            openFile(file);
                        }
                    }
                }
            }
        });
    }

    private void addActionToButtons() {
        openButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                if (textWasChanged && file != null) {
                    int answer = JOptionPane.showConfirmDialog(null, "Хотите сохранить изменения?", "", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (answer == JOptionPane.YES_OPTION) {
                        saveFile();
                    }
                }
                JFileChooser chooser = new JFileChooser();
                int chooseAnswer = chooser.showSaveDialog(null);
                if (chooseAnswer == JFileChooser.APPROVE_OPTION) {
                    file = new File(chooser.getSelectedFile().getAbsolutePath());
                    openFile(file);
                }
                savePreviouslyOpenFiles();
            }
        });

        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                if (file != null){
                    saveFile();
                } else {
                    JOptionPane.showMessageDialog(null, "Нет открытых файлов");
                }
            }
        });

        saveAsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                if (file != null){
                    for (Trajectory trajectory : CatalogPanel.getTrajectories()) {
                        if (trajectory.getFilePath().equals(file.getPath())) {
                            chosenTrajectory = trajectory;
                        }
                    }
                    String newName = JOptionPane.showInputDialog(null, "Введите новое имя файла");
                    if (newName != null) {
                        String newPath = new StringBuilder(chosenTrajectory.getFilePath().substring(0, chosenTrajectory.getFilePath().lastIndexOf('/')+1)).append(newName).toString();
                        chosenTrajectory.renameTrajectoryFile(file.getName(), newPath);
                        boolean renameFile = file.renameTo(new File(newPath));
                        if (renameFile) {
                            JOptionPane.showMessageDialog(null, "Файл успешно переименован");
                            savePreviouslyOpenFiles();
                        }
                        FilePanel.setFileName(newPath);
                        chosenTrajectory.setFilePath(newName);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Нет открытых файлов");
                }
            }
        });
    }

    public static void saveFile() {
        try (FileWriter fileOut = new FileWriter(file)) {
            String string = FilePanel.getTextAtPanel();
            fileOut.write(string);
            textWasChanged = false;
            List<String> data = Arrays.asList(string.split("\n"));
            for (Trajectory trajectory : CatalogPanel.getTrajectories()) {
                if (trajectory.getFilePath().equals(file.getPath())) {
                    trajectory.setFileData(data);
                    trajectory.readTrajectoryFile(data);
                }
            }
        } catch (IOException e) {
            System.out.println("Возникли проблемы при сохранении файла");
            e.printStackTrace();
        }
    }

    public static void setFile(File newFile) {
        file = newFile;
    }

    public static File getFile() {
        return file;
    }

    private void readPreviouslyOpenFiles() {
        File f = new File(PATH_TO_OPENED_FILES);
        if (!f.exists()) {
            try {
                f.getParentFile().mkdirs();
                f.createNewFile();
            } catch (IOException e) {
                System.out.println("При создании файла возникла ошибка");
                e.printStackTrace();
            }
        }
        try (Scanner scanner = new Scanner(new File(PATH_TO_OPENED_FILES))) {
            String path;
            while (scanner.hasNext()) {
                path = scanner.nextLine();
                if (!path.equals(PATH_TO_OPENED_FILES)) {
                    openedFiles.add(path);
                    listModel.addElement(FilenameUtils.getBaseName(path));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Данный файл не существует");
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    private void savePreviouslyOpenFiles() {
        try (FileWriter fileOut = new FileWriter(PATH_TO_OPENED_FILES)) {
            StringBuilder s = new StringBuilder();
            for (String path : openedFiles) {
                s.append(path);
                s.append("\n");
            }
            fileOut.write(s.toString());
        } catch (FileNotFoundException e) {
            System.out.println("Данный файл не существует");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Возникла ошибка при записи данных в файл");
            e.printStackTrace();
        }
    }

    public static void openFile(File file) {
        int fileExist = 0;
        if (!CatalogPanel.getTrajectories().isEmpty()) {
            for (Trajectory trajectory : CatalogPanel.getTrajectories()) {
                if (trajectory.getFilePath().equals(file.getPath())) {
                    chosenTrajectory = trajectory;
                    fileExist++;
                    JOptionPane.showMessageDialog(null, "Данный файл уже открыт");
                    break;
                }
            }
        }
        if (fileExist == 0) {
            chosenTrajectory = CatalogPanel.addTrajectory(file);
        }
        if (!CatalogPanel.checkListModel(chosenTrajectory.getTrajectoryName())) {
            CatalogPanel.addElementAtList(chosenTrajectory.getTrajectoryName());
            if (!listModel.contains(chosenTrajectory.getFileName())) {
                listModel.addElement(chosenTrajectory.getFileName());
                openedFiles.add(chosenTrajectory.getFilePath());
            }
        }
        if (chosenTrajectory != null) {
            FilePanel.setTextAtPanel(chosenTrajectory);
            TablePanel.setTableModel(chosenTrajectory.getPoints());
        }
        textWasChanged = false;
    }
}
