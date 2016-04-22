package com.epam.training.strategy;

import java.util.Map.Entry;

import com.epam.training.domain.BattleArena;
import com.epam.training.domain.Coordinate;
import com.epam.training.domain.Field;

public class BaseStrategy implements Strategy {

    private BattleArena arena;

    public BaseStrategy(BattleArena arena) {
        super();
        this.arena = arena;
    }

    public Coordinate getNext(Coordinate lastMove) {
        BattleArena effectiveMap = arena.getEffectiveMap();

        setWeights(effectiveMap);

        Coordinate nextCoordinate = getMaxWeightCoordinate(effectiveMap);
        return nextCoordinate;
    }

    private void setWeights(BattleArena effectiveMap) {

    }

    private Coordinate getMaxWeightCoordinate(BattleArena effectiveMap) {
        double maxValue = 0;
        Entry<Coordinate, Field> maxEntry = null;
        for (Entry<Coordinate, Field> entry : effectiveMap) {
            double value = entry.getValue().getWeight();
            if (value > maxValue) {
                maxValue = value;
                maxEntry = entry;
            }
        }
        return maxEntry.getKey();
    }
}
