package model;

public class Point {
    private final double time;
    private final double xVelocity;
    private final double yVelocity;
    private final double zVelocity;
    private final double xCoordinate;
    private final double yCoordinate;
    private final double zCoordinate;

    public Point(double time, double xCoordinate, double yCoordinate, double zCoordinate, double xVelocity, double yVelocity, double zVelocity){
        this.time = time;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.zCoordinate = zCoordinate;
        this.xVelocity = xVelocity;
        this.yVelocity = yVelocity;
        this.zVelocity = zVelocity;
    }

    public double getTime(){
        return time;
    }

    public double getXCoordinate(){
        return xCoordinate;
    }

    public double getYCoordinate(){
        return yCoordinate;
    }

    public double getZCoordinate(){
        return zCoordinate;
    }

    public double getXVelocity(){
        return xVelocity;
    }

    public double getYVelocity(){
        return yVelocity;
    }

    public double getZVelocity(){
        return zVelocity;
    }

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(time);
        stringBuilder.append(' ');
        stringBuilder.append(xCoordinate);
        stringBuilder.append(' ');
        stringBuilder.append(yCoordinate);
        stringBuilder.append(' ');
        stringBuilder.append(zCoordinate);
        stringBuilder.append(' ');
        stringBuilder.append(xVelocity);
        stringBuilder.append(' ');
        stringBuilder.append(yVelocity);
        stringBuilder.append(' ');
        stringBuilder.append(zVelocity);
        return stringBuilder.toString();
    }
}
