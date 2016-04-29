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

    private static final double UNCLOSED_THREE_OR_MORE_MARKS_MULTIPLIER = 1.5;
    private static final int WIN_TRESHOLD = 4;
    private static final int PANIC_TRESHOLD = 4;
    private static final int WIN_WEIGHT = 10000000;
    private static final int PANIC_WEIGHT = 1000000;
    private static final double ENEMY_MARK_WEIGHT_MULTIPLIER = 1.0;
    private static final double NEXT_COORDINATE_IS_FREE_MULTIPLIER = 1.25;
    private static final double OPPOSIT_DIRECTION_SAME_MARK_MULTIPLIER = 1.1; // checked from both directions, counted twice!
    private static final double MARKS_CLOSED_BY_ENEMY_MULTIPLIER = 0.95;
    private static final double CLOSED_THREE_OR_LESS_MULTIPLIER = 0;
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

        BattleArena freeMap = setWeights(arena);
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
            } else if (weights.get(direction.getOposite()).getType() != FieldType.EMPTY) {
                weights.get(direction).setWeight(weights.get(direction).getWeight() * OPPOSIT_DIRECTION_SAME_MARK_MULTIPLIER);
            }
        }

        for (Direction direction : weights.keySet()) {
            if (checkIfThereAreFewerThenPanicEnemyMarksInTheDirection(weights, direction)) {
                //if markcount is lower then PANIC_TRESHOLD and it is closed on the other side of the evaluated field, set it to 0 because it is not important
                if (checkIfThereIsOwnMarkInTheOppositeDirection(weights, direction)) {
                    weights.get(direction).setWeight(0);
                }
            } else if (checkIfThereArePanicNumberOfEnemyMarksInTheDirection(weights, direction)) {
                weights.get(direction).setWeight(PANIC_WEIGHT + weights.get(direction).getMarkCount());
            }
        }
        int halfClosedThreesAroundTheField = 0;
        for (Direction direction : weights.keySet()) {
            if (chekIfThereAreAWinningNumberOfOwnMarksInTheDirection(weights, direction)) {
                weights.get(direction).setWeight(WIN_WEIGHT);
            }
            if (chekIfThereAreWinningNumberOfSameTypeMarksAroundTheField(weights, direction)
                    || chekIfThereAreWinningNumberMinusOneOfSameTypeMarksAroundTheFieldUnclosed(weights, direction)) {
                weight += PANIC_WEIGHT;
            }
            if (chekIfThereIsHlafClosedThreeOrMoreInTheDirection(weights, direction)) {
                halfClosedThreesAroundTheField++;
            }
        }

        if (halfClosedThreesAroundTheField >= 2) {
            weight = WIN_WEIGHT;
        }

        for (Direction direction : weights.keySet()) {
            weight += weights.get(direction).getWeight();
        }
        return weight;
    }

    private boolean chekIfThereIsHlafClosedThreeOrMoreInTheDirection(Map<Direction, DirectionWeightParameter> weights, Direction direction) {
        return weights.get(direction).getMarkCount() >= 3 && weights.get(direction).getCloserType() == FieldType.ENEMY;
    }

    //if there are lower then PANIC_TRESHOLD marks in the given direction and they are enemy marks
    private boolean checkIfThereAreFewerThenPanicEnemyMarksInTheDirection(Map<Direction, DirectionWeightParameter> weights, Direction direction) {
        return weights.get(direction).getMarkCount() < PANIC_TRESHOLD && weights.get(direction).getType() == FieldType.ENEMY;
    }

    private boolean checkIfThereIsOwnMarkInTheOppositeDirection(Map<Direction, DirectionWeightParameter> weights, Direction direction) {
        return weights.get(direction.getOposite()).getType() == FieldType.OWN;
    }

    //if there are greater or equals PANIC_TRESHOLD marks in the given direction and they are enemy, this means a sure loose on the next turn if not taken care of
    private boolean checkIfThereArePanicNumberOfEnemyMarksInTheDirection(Map<Direction, DirectionWeightParameter> weights, Direction direction) {
        return weights.get(direction).getMarkCount() >= PANIC_TRESHOLD && weights.get(direction).getType() == FieldType.ENEMY;
    }

    //if there are WIN_TRESHOLD count of marks in the direction and they are ours, meaning we can win in this turn
    private boolean chekIfThereAreAWinningNumberOfOwnMarksInTheDirection(Map<Direction, DirectionWeightParameter> weights, Direction direction) {
        return weights.get(direction).getMarkCount() >= WIN_TRESHOLD && weights.get(direction).getType() == FieldType.OWN;
    }

    //if there are WIN_TRESHOLD enemy marks on the oposite sides of the evaluated fields
    private boolean chekIfThereAreWinningNumberOfSameTypeMarksAroundTheField(Map<Direction, DirectionWeightParameter> weights, Direction direction) {
        return weights.get(direction).getType() != FieldType.EMPTY
                && weights.get(direction).getMarkCount() + weights.get(direction.getOposite()).getMarkCount() >= WIN_TRESHOLD
                && weights.get(direction).getType() == weights.get(direction.getOposite()).getType();
    }

    private boolean chekIfThereAreWinningNumberMinusOneOfSameTypeMarksAroundTheFieldUnclosed(Map<Direction, DirectionWeightParameter> weights,
            Direction direction) {
        return weights.get(direction).getType() != FieldType.EMPTY
                && weights.get(direction).getMarkCount() + weights.get(direction.getOposite()).getMarkCount() >= WIN_TRESHOLD - 1
                && weights.get(direction).getType() == weights.get(direction.getOposite()).getType()
                && weights.get(direction).getCloserType() == FieldType.EMPTY
                && weights.get(direction.getOposite()).getCloserType() == FieldType.EMPTY;
    }

    private DirectionWeightParameter checkDirection(Direction direction, BattleArena effectiveMap, Coordinate coordinate) {
        int markCount = 0;
        double weight = 0;

        Coordinate nextCoordinate = coordinate.getNext(direction);
        FieldType markType = effectiveMap.getFieldOnCoordinate(nextCoordinate) == null ? FieldType.EMPTY
                : effectiveMap.getFieldOnCoordinate(nextCoordinate).getType();

        while (effectiveMap.isOccupied(nextCoordinate) && effectiveMap.getFieldOnCoordinate(nextCoordinate).getType() == markType) {
            markCount++;
            nextCoordinate = nextCoordinate.getNext(direction);
        }
        weight = Math.pow(3, markCount) * getFieldWeightMultiplier(effectiveMap, nextCoordinate, markType);
        weight *= markType == FieldType.ENEMY ? ENEMY_MARK_WEIGHT_MULTIPLIER : 1;
        FieldType closerType = getCloserType(effectiveMap, nextCoordinate);
        weight *= calculateClosureMultiplier(markCount, markType, closerType);
        return new DirectionWeightParameter(weight, markType, markCount, closerType);

    }

    private double calculateClosureMultiplier(int markCount, FieldType markType, FieldType closerType) {
        return decideIfUnclosedThreeOrMore(markCount, closerType) ? UNCLOSED_THREE_OR_MORE_MARKS_MULTIPLIER
                : decideIfClosedThreeOrLess(markCount, markType, closerType) ? CLOSED_THREE_OR_LESS_MULTIPLIER : 1;
    }

    private boolean decideIfClosedThreeOrLess(int markCount, FieldType markType, FieldType closerType) {
        return closerType.isEnemy(markType) && markCount <= 3;
    }

    private boolean decideIfUnclosedThreeOrMore(int markCount, FieldType closerType) {
        return closerType == FieldType.EMPTY && markCount >= 3;
    }

    private FieldType getCloserType(BattleArena effectiveMap, Coordinate nextCoordinate) {
        return effectiveMap.isOccupied(nextCoordinate)
                ? effectiveMap.getFieldOnCoordinate(nextCoordinate).getType()
                        : FieldType.EMPTY;
    }

    private double getFieldWeightMultiplier(BattleArena effectiveMap, Coordinate nextCoordinate, FieldType markType) {
        return effectiveMap.isOccupied(nextCoordinate)
                ? effectiveMap.getFieldOnCoordinate(nextCoordinate).getType().isEnemy(markType) ? MARKS_CLOSED_BY_ENEMY_MULTIPLIER : 1
                        : NEXT_COORDINATE_IS_FREE_MULTIPLIER;
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
