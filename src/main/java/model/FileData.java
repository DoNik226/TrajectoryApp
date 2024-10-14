package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

import javax.swing.*;

public class FileData {
    private String path;
    private List<String> data;
    private String format;
    private File file;
    private List<Integer> incorrectStrings;

    public FileData(File file) {
        incorrectStrings = new ArrayList<>();
        this.file = file;
        path = file.getPath();
        format = FilenameUtils.getExtension(path);
        data = new ArrayList<>();
        readFile(path);
    }

    private void readFile(String filePath) {
        incorrectStrings.clear();
        try (FileReader fileReader = new FileReader(filePath)) {
            StringBuilder s = new StringBuilder();
            int i;
            char c;
            int stringNumber = 1;
            while ((i = fileReader.read()) != -1) {
                c = (char) i;
                if (c == '\n'){
                    checkFile(s.toString(), stringNumber);
                    data.add(String.valueOf(s));
                    s.setLength(0);
                    stringNumber++;
                } else {
                    s.append(c);
                }
            }
            //incorrectStrings.remove(incorrectStrings.size()-1);
            if (!incorrectStrings.isEmpty()) {
                s = new StringBuilder("В данном файле некорректные данные в следующих строках: ");
                for (Integer number : incorrectStrings) {
                    s.append(number);
                    s.append(", ");
                }
                JOptionPane.showMessageDialog(null, s.substring(0, s.length()-2));
            }
        } catch (FileNotFoundException e){
            System.out.println("Данный файл не найден, попробуйте еще раз\n");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkFile(String string, int number) {
        if (string.isEmpty()) {
            incorrectStrings.add(number);
        } else {
            String[] stringsData = string.split("  ");
            for (String data : stringsData) {
                try {
                    Double.parseDouble(data);
                } catch (NumberFormatException e) {
                    incorrectStrings.add(number);
                    break;
                }
            }
        }
    }

    public String getPath(){
        return path;
    }

    public String getName() { return FilenameUtils.getBaseName(path); }

    public String getFormat(){
        return format;
    }

    public List<String> getData(){
        return data;
    }

    public File getFile() { return file; }

    public void setPath(String path) { this.path = path; }

    public void setData(List<String> data) {
        this.data = data;
    }

    public boolean fileIsCorrect() {
        return incorrectStrings.isEmpty();
    }
}
