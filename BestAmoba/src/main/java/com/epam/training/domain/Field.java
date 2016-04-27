package com.epam.training.domain;

public class Field {

    private double weight;
    private FieldType type;

    public Field() {

    }

    public Field(double weight, FieldType type) {
        super();
        this.weight = weight;
        this.type = type;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public FieldType getType() {
        return type;
    }

    public boolean isEnemy() {
        return type == FieldType.ENEMY;
    }

    public void setType(FieldType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return String.valueOf(weight);
    }

}
