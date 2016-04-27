package com.epam.training.domain;

public enum FieldType {
    OWN, ENEMY, EMPTY;

    public boolean isEnemy(FieldType field) {
        return field != EMPTY && field != this;
    }

}
