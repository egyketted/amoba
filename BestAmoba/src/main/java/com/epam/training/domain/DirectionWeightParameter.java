package com.epam.training.domain;

public class DirectionWeightParameter {

    private double weight;
    private boolean markIsOurs;
    private int markCount;

    public int getMarkCount() {
        return markCount;
    }

    public void setMarkCount(int markCount) {
        this.markCount = markCount;
    }

    public DirectionWeightParameter() {
    }

    public DirectionWeightParameter(double weight, boolean lastMarkIsOurs, int markCount) {
        super();
        this.weight = weight;

        this.markIsOurs = lastMarkIsOurs;
        this.markCount = markCount;
    }

    public double getWeight() {
        return weight;
    }

    public boolean isLastMarkIsOurs() {
        return markIsOurs;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setLastMarkIsOurs(boolean lastMarkIsOurs) {
        this.markIsOurs = lastMarkIsOurs;
    }

}
