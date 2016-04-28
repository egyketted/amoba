package com.epam.training.domain;

public class DirectionWeightParameter {

    private double weight;
    private FieldType type;
    private int markCount;
    private FieldType closerType;

    public DirectionWeightParameter(double weight, FieldType type, int markCount, FieldType closerType) {
        super();
        this.weight = weight;
        this.type = type;
        this.markCount = markCount;
        this.closerType = closerType;
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

    public FieldType getCloserType() {
        return closerType;
    }

    public void setCloserType(FieldType closerType) {
        this.closerType = closerType;
    }

}
