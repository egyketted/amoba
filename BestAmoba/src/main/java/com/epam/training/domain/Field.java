package com.epam.training.domain;

public class Field {

    private double weight;
    private boolean isEnemy;

    public Field() {

    }

    public Field(double weight, boolean isEnemy) {
        this.weight = weight;
        this.isEnemy = isEnemy;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public boolean isEnemy() {
        return isEnemy;
    }

    public void setEnemy(boolean isEnemy) {
        this.isEnemy = isEnemy;
    }

    @Override
    public String toString() {
        return String.valueOf(weight);
    }

}
