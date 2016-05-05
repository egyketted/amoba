package com.epam.training.strategy;

import com.epam.training.domain.Coordinate;

public interface Strategy {

    public Coordinate getNext(Coordinate lastMove);
}
