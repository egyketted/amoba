package com.epam.training.strategy;

import com.epam.training.domain.Coordinate;

public class RandomStrategy implements Strategy {

    @Override
    public Coordinate getNext(Coordinate lastMove) {
        return new Coordinate((int) (Math.random() * 1000), (int) (Math.random() * 1000));
    }

}
