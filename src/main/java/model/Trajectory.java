package model;

import interfaces.CatalogPanel;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Trajectory {
    private List<Point> points;
    private String name;
    private FileData file;
    private static int size = 1;

    public Trajectory(FileData fileData){
        file = fileData;
        points = new ArrayList<>();
        readTrajectoryFile(fileData.getData());
        if (!setTrajectoryName()) {
            name = new StringBuilder("Траектория ").append(size).toString();
            size++;
        }
    }

    public String getTrajectoryName(){
        return name;
    }

    public boolean setTrajectoryName(){
        String name = JOptionPane.showInputDialog(null, "Введите имя траектории");
        for (Trajectory trajectory : CatalogPanel.getTrajectories()) {
            if (trajectory.getTrajectoryName().equals(name)){
                JOptionPane.showConfirmDialog(null, "Данное имя уже существует");
            }
            return false;
        }
        if (name != null && name != "") {
            this.name = name;
            return true;
        }
        return false;
    }

    public List<Point> getPoints(){
        return points;
    }

    public String getFilePath(){
        return file.getPath();
    }

    public List<String> getFileData(){
        return file.getData();
    }

    public String getFileFormat(){
        return file.getFormat();
    }

    public File getFile() { return file.getFile(); }

    public String getFileName() { return file.getName(); }

    public void setFilePath(String path) {
        file.setPath(path);
    }

    public void setFileData(List<String> data) {
        file.setData(data);
    }

    public void readTrajectoryFile(List<String> data){
        points.clear();
        for (String string : data){
            String[] parts = string.split("  ");
            points.add(new Point(Double.parseDouble(parts[0]), Double.parseDouble(parts[1]), Double.parseDouble(parts[2]), Double.parseDouble(parts[3]), Double.parseDouble(parts[4]), Double.parseDouble(parts[5]), Double.parseDouble(parts[6])));
        }
    }

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder("Name: ");
        stringBuilder.append(name);
        stringBuilder.append("\nFile path: ");
        stringBuilder.append(getFilePath());
        stringBuilder.append('\n');
        for (Point point : points){
            stringBuilder.append(point.toString());
            stringBuilder.append('\n');
        }
        return stringBuilder.toString();
    }

    public void renameTrajectoryFile(String oldPath, String newPath) {
        for (Trajectory trajectory: CatalogPanel.getTrajectories()) {
            if (trajectory.getFilePath().equals(oldPath)) {
                file.setPath(newPath);
            }
        }
    }
}
