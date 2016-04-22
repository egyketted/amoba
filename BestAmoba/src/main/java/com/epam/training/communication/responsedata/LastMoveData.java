package com.epam.training.communication.responsedata;

public class LastMoveData {

    private int x;
    private int y;
    private String t;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    @Override
    public String toString() {
        return "LastMoveData [x=" + x + ", y=" + y + ", t=" + t + "]";
    }

}
