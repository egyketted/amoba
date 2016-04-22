package com.epam.training.domain;

public class DirectionWeightParameter {

    private double weight;
    private int enemyMarkCount;
    private boolean lastMarkIsOurs;

    public DirectionWeightParameter() {
    }

    public DirectionWeightParameter(double weight, int enemyMarkCount, boolean lastMarkIsOurs) {
        this.weight = weight;
        this.enemyMarkCount = enemyMarkCount;
        this.lastMarkIsOurs = lastMarkIsOurs;
    }

    public double getWeight() {
        return weight;
    }

    public int getEnemyMarkCount() {
        return enemyMarkCount;
    }

    public boolean isLastMarkIsOurs() {
        return lastMarkIsOurs;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setEnemyMarkCount(int enemyMarkCount) {
        this.enemyMarkCount = enemyMarkCount;
    }

    public void setLastMarkIsOurs(boolean lastMarkIsOurs) {
        this.lastMarkIsOurs = lastMarkIsOurs;
    }

}
