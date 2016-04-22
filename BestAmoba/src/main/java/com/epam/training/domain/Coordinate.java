package com.epam.training.domain;

import java.util.ArrayList;
import java.util.List;

public class Coordinate {

    private int x;
    private int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double getDistance(Coordinate otherCoordinate) {
        return Math.sqrt(Math.pow(x - otherCoordinate.getX(), 2) + Math.pow(y - otherCoordinate.getY(), 2));
    }

    public List<Coordinate> getNeighbours() {
        List<Coordinate> neighbours = new ArrayList<Coordinate>();

        neighbours.add(new Coordinate(x, y + 1));
        neighbours.add(new Coordinate(x, y - 1));
        neighbours.add(new Coordinate(x + 1, y));
        neighbours.add(new Coordinate(x - 1, y));
        neighbours.add(new Coordinate(x + 1, y + 1));
        neighbours.add(new Coordinate(x + 1, y - 1));
        neighbours.add(new Coordinate(x - 1, y + 1));
        neighbours.add(new Coordinate(x - 1, y - 1));

        return neighbours;
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + x;
        result = prime * result + y;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Coordinate other = (Coordinate) obj;
        if (x != other.x) {
            return false;
        }
        if (y != other.y) {
            return false;
        }
        return true;
    }

}
