package com.epam.training.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class BattleArena implements Iterable<Entry<Coordinate, Field>> {

    private static final int EFFECTIVE_MAP_SIZE_LIMIT = 4;
    private static final int DISTANCE_LIMIT = 10;

    private Map<Coordinate, Field> arena;

    public BattleArena() {
        this.arena = new HashMap<Coordinate, Field>();
    }

    public BattleArena(Map<Coordinate, Field> arena) {
        this.arena = new HashMap<Coordinate, Field>(arena);
    }

    public boolean isEnemyCoordinate(Coordinate coordinate) {
        Field field = arena.get(coordinate);
        return field.isEnemy();
    }

    public double getWeight(Coordinate coordinate) {
        Field field = arena.get(coordinate);
        return field.getWeight();
    }

    public void setWeight(Coordinate coordinate, double weight) {
        Field field = arena.get(coordinate);
        field.setWeight(weight);
    }

    public Field getFieldOnCoordinate(Coordinate coordinate) {
        return arena.get(coordinate);
    }

    public BattleArena getFreeMap() {
        Map<Coordinate, Field> freeMap = new HashMap<Coordinate, Field>();
        Set<Entry<Coordinate, Field>> entrySet = arena.entrySet();

        for (Entry<Coordinate, Field> entry : entrySet) {
            List<Coordinate> neighbours = entry.getKey().getNeighbours();
            for (Coordinate coordinate : neighbours) {
                if (!arena.containsKey(coordinate)) {
                    freeMap.put(coordinate, new Field());
                }
            }
        }
        return new BattleArena(freeMap);
    }

    public BattleArena getEffectiveMap() {
        Map<Coordinate, Field> effectiveMap = new HashMap<Coordinate, Field>();
        if (arena.size() < EFFECTIVE_MAP_SIZE_LIMIT) {
            effectiveMap = new HashMap<Coordinate, Field>(arena);
        } else {
            Set<Entry<Coordinate, Field>> entrySet = arena.entrySet();
            for (Entry<Coordinate, Field> entry : entrySet) {
                if (isCoordinateNearby(entry)) {
                    effectiveMap.put(entry.getKey(), entry.getValue());
                }
            }
        }
        return new BattleArena(effectiveMap);
    }

    private boolean isCoordinateNearby(Entry<Coordinate, Field> check) {
        Set<Entry<Coordinate, Field>> entrySet = arena.entrySet();
        List<Double> distances = new ArrayList<Double>();

        if (check.getValue().isEnemy()) {
            for (Entry<Coordinate, Field> entry : entrySet) {
                if (entry.getValue().isEnemy()) {
                    distances.add(check.getKey().getDistance(entry.getKey()));
                }
            }
        } else {
            for (Entry<Coordinate, Field> entry : entrySet) {
                if (!entry.getValue().isEnemy()) {
                    distances.add(check.getKey().getDistance(entry.getKey()));
                }
            }
        }
        Collections.sort(distances);
        return distances.get(0) < DISTANCE_LIMIT;
    }

    /*
    private boolean isCoordinateInSquare(Entry<Coordinate, Field> check) {
        if (check.getValue().isEnemy()) {
            for (Entry<Coordinate, Field> entry : entrySet) {
                if (entry.getValue().isEnemy()) {
                    distances.add(check.getKey().getDistance(entry.getKey()));
                }
            }
        } else {
            for (Entry<Coordinate, Field> entry : entrySet) {
                if (!entry.getValue().isEnemy()) {
                    distances.add(check.getKey().getDistance(entry.getKey()));
                }
            }
        }
    
    }
    */
    public Iterator<Entry<Coordinate, Field>> iterator() {
        return arena.entrySet().iterator();
    }

}
