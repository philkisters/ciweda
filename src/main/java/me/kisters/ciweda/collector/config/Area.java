package me.kisters.ciweda.collector.config;

public class Area {

    private double minLat;
    private double minLong;
    private double maxLat;
    private double maxLong;
    private int yCells;
    private int xCells;



    public double getMinLat() {
        return minLat;
    }

    public void setMinLat(double minLat) {
        this.minLat = minLat;
    }

    public double getMinLong() {
        return minLong;
    }

    public void setMinLong(double minLong) {
        this.minLong = minLong;
    }

    public double getMaxLat() {
        return maxLat;
    }

    public void setMaxLat(double maxLat) {
        this.maxLat = maxLat;
    }

    public double getMaxLong() {
        return maxLong;
    }

    public void setMaxLong(double maxLong) {
        this.maxLong = maxLong;
    }

    public int getYCells() {
        return yCells;
    }

    public void setYCells(int yCells) {
        this.yCells = yCells;
    }

    public int getXCells() {
        return xCells;
    }

    public void setXCells(int xCells) {
        this.xCells = xCells;
    }
}