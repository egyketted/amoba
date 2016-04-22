package com.epam.training.strategy;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.epam.training.domain.BattleArena;
import com.epam.training.domain.Coordinate;
import com.epam.training.domain.Direction;
import com.epam.training.domain.DirectionWeightParameter;
import com.epam.training.domain.Field;

public class BaseStrategy implements Strategy {

    private static final int PANIC_WEIGHT = 1000000;
    private BattleArena arena;

    public BaseStrategy(BattleArena arena) {
        this.arena = arena;
    }

    @Override
    public Coordinate getNext(Coordinate lastMove) {
        if (!arena.isEmpty()) {
            arena.add(lastMove, new Field(0, true));

        } else {
            Coordinate nextCoordinate = new Coordinate(0, 0);
            arena.add(nextCoordinate, new Field(0, false));
            return nextCoordinate;
        }
        BattleArena freeMap = setWeights(arena.getEffectiveMap());

        Coordinate nextCoordinate = getMaxWeightCoordinate(freeMap);
        arena.add(nextCoordinate, new Field(0, false));
        return nextCoordinate;
    }

    private BattleArena setWeights(BattleArena effectiveMap) {
        BattleArena freeMap = effectiveMap.getFreeMap();
        System.out.println(freeMap);
        for (Entry<Coordinate, Field> entry : freeMap) {
            entry.getValue().setWeight(calculateWeight(effectiveMap, entry.getKey()));
        }
        return freeMap;
    }

    private double calculateWeight(BattleArena effectiveMap, Coordinate coordinate) {
        Map<Direction, DirectionWeightParameter> weights = new HashMap<>();
        double weight = 0;
        for (Direction direction : Direction.values()) {
            weights.put(direction, checkDirection(direction, effectiveMap, coordinate));
        }
        for (Direction direction : weights.keySet()) {
            if (weights.get(direction).getEnemyMarkCount() < 4 && !weights.get(direction).isLastMarkIsOurs()) {
                if (weights.get(direction.getOposite()).isLastMarkIsOurs()) {
                    weights.get(direction).setWeight(0);
                } else if (weights.get(direction).getEnemyMarkCount() >= 4) {
                    weights.get(direction).setWeight(PANIC_WEIGHT);
                }
            }
        }
        for (Direction direction : weights.keySet()) {
            weight += Math.min(weights.get(direction).getWeight(), 0);
        }
        return weight;
    }

    private DirectionWeightParameter checkDirection(Direction direction, BattleArena effectiveMap, Coordinate coordinate) {
        int enemyMarkCount = 0;
        double weight = 0;
        boolean lastMarkIsOurs = false;

        for (int i = 0; i < 5; i++) {
            Coordinate nextCoordinate = coordinate.getNext(direction);
            if (i == 0) {
                lastMarkIsOurs = effectiveMap.isOccupied(nextCoordinate) && !effectiveMap.getFieldOnCoordinate(nextCoordinate).isEnemy();
            }
            if (effectiveMap.isOccupied(nextCoordinate)) {
                Field field = effectiveMap.getFieldOnCoordinate(nextCoordinate);
                if (field.isEnemy()) {
                    enemyMarkCount++;
                    weight += 0.5;
                } else {
                    weight -= 0.5;
                }
            }
        }
        return new DirectionWeightParameter(weight, enemyMarkCount, lastMarkIsOurs);
    }

    private Coordinate getMaxWeightCoordinate(BattleArena map) {
        double maxValue = -10000000;
        Entry<Coordinate, Field> maxEntry = null;
        for (Entry<Coordinate, Field> entry : map) {
            double value = entry.getValue().getWeight();
            if (value > maxValue) {
                maxValue = value;
                maxEntry = entry;
            }
        }
        return maxEntry.getKey();
    }
}
