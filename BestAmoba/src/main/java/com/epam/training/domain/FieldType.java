package com.epam.training.domain;

public enum FieldType {
    OWN, ENEMY, EMPTY;

    public boolean isEnemy(FieldType field) {
        if (this == EMPTY) {
            return false;
        } else {
            return field != EMPTY && field != this;
        }
    }

    public FieldType getEnemyType() {
        if (this == EMPTY) {
            return EMPTY;
        } else if (this == OWN) {
            return ENEMY;
        }
        return OWN;
    }

}
