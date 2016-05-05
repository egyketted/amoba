package com.epam.training.domain;

public enum Direction {
    UP(0, 1), UP_RIGHT(1, 1), RIGHT(1, 0), DOWN_RIGHT(1, -1), DOWN(0, -1), DOWN_LEFT(-1, -1), LEFT(-1, 0), UP_LEFT(-1, 1);

    private int xDifference;
    private int yDifference;

    private Direction oposite;

    private Direction(int xDifference, int yDifference) {

        this.yDifference = yDifference;
        this.xDifference = xDifference;
    }

    public int getXDifference() {
        return xDifference;
    }

    public int getYDifference() {
        return yDifference;
    }

    public void setOpposite(Direction direction) {
        this.oposite = direction;

    }

    public Direction getOposite() {
        return oposite;
    }

}
