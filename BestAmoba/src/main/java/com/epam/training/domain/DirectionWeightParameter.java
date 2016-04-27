package com.epam.training.domain;

public class DirectionWeightParameter {

    private double weight;
    private FieldType type;
    private int markCount;

    public DirectionWeightParameter(double weight, FieldType type, int markCount) {
        super();
        this.weight = weight;
        this.type = type;
        this.markCount = markCount;
    }

    public int getMarkCount() {
        return markCount;
    }

    public void setMarkCount(int markCount) {
        this.markCount = markCount;
    }

    public DirectionWeightParameter() {
    }

    public double getWeight() {
        return weight;
    }

    public FieldType getType() {
        return type;
    }

    public void setType(FieldType type) {
        this.type = type;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

}
