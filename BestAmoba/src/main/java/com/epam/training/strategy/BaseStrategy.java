package com.epam.training.strategy;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.epam.training.domain.BattleArena;
import com.epam.training.domain.Coordinate;
import com.epam.training.domain.Direction;
import com.epam.training.domain.DirectionWeightParameter;
import com.epam.training.domain.Field;
import com.epam.training.domain.FieldType;

public class BaseStrategy implements Strategy {

    private static final double ENEMY_MARK_WEIGHT_MULTIPLIER = 1.2;
    private static final double MARK_COUNT_SCALE = 3;
    private static final double NEXT_COORDINATE_IS_FREE_MULTIPLIER = 1.25;
    private static final double OPPOSIT_DIRECTION_SAME_MARK_MULTIPLIER = 1.1; // checked from both directions, counted twice!
    private static final int PANIC_WEIGHT = 1000000;
    private BattleArena arena;

    public BaseStrategy(BattleArena arena) {
        this.arena = arena;
    }

    @Override
    public Coordinate getNext(Coordinate lastMove) {

        if (lastMove == null) {
            Coordinate nextCoordinate = new Coordinate((int) Math.random() * 1000, (int) Math.random() * 1000);
            arena.add(nextCoordinate, new Field(0, FieldType.OWN));
            System.out.println(nextCoordinate);
            return nextCoordinate;
        }
        arena.add(lastMove, new Field(0, FieldType.ENEMY));

        BattleArena freeMap = setWeights(arena.getEffectiveMap());
        System.out.println("freemap: " + freeMap);

        Coordinate nextCoordinate = getMaxWeightCoordinate(freeMap);
        arena.add(nextCoordinate, new Field(0, FieldType.OWN));
        System.out.println(nextCoordinate);
        return nextCoordinate;
    }

    private BattleArena setWeights(BattleArena effectiveMap) {
        BattleArena freeMap = effectiveMap.getFreeMap();
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
            if (weights.get(direction.getOposite()).getType().isEnemy(weights.get(direction).getType())) {
                weights.get(direction).setWeight(0);
            } else {
                weights.get(direction).setWeight(weights.get(direction).getWeight() * OPPOSIT_DIRECTION_SAME_MARK_MULTIPLIER);
            }
        }

        for (Direction direction : weights.keySet()) {
            if (weights.get(direction).getMarkCount() < 4 && weights.get(direction).getType() == FieldType.ENEMY) {
                if (weights.get(direction.getOposite()).getType() == FieldType.OWN) {
                    weights.get(direction).setWeight(0);
                }
            } else if (weights.get(direction).getMarkCount() >= 4 && weights.get(direction).getType() == FieldType.ENEMY) {
                weights.get(direction).setWeight(PANIC_WEIGHT);
            }
        }

        for (Direction direction : weights.keySet()) {
            weight += weights.get(direction).getWeight();
        }
        return weight;
    }

    private DirectionWeightParameter checkDirection(Direction direction, BattleArena effectiveMap, Coordinate coordinate) {
        int markCount = 0;
        double weight = 0;

        Coordinate nextCoordinate = coordinate.getNext(direction);
        FieldType markIsOurs = effectiveMap.getFieldOnCoordinate(nextCoordinate) == null ? FieldType.EMPTY
                : effectiveMap.getFieldOnCoordinate(nextCoordinate).getType();

        while (effectiveMap.isOccupied(nextCoordinate) && effectiveMap.getFieldOnCoordinate(nextCoordinate).getType() == markIsOurs) {
            markCount++;
            nextCoordinate = nextCoordinate.getNext(direction);
        }
        weight = Math.pow(MARK_COUNT_SCALE, markCount) * (effectiveMap.isOccupied(nextCoordinate) ? 1 : NEXT_COORDINATE_IS_FREE_MULTIPLIER);
        weight *= markIsOurs == FieldType.ENEMY ? ENEMY_MARK_WEIGHT_MULTIPLIER : 1;
        return new DirectionWeightParameter(weight, markIsOurs, markCount);

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
