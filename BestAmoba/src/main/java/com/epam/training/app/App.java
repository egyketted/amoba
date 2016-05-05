package com.epam.training.app;

import com.epam.training.communication.Communicator;
import com.epam.training.communication.HttpCommunicator;
import com.epam.training.domain.BattleArena;
import com.epam.training.domain.Direction;
import com.epam.training.strategy.BaseStrategy;
import com.epam.training.strategy.Strategy;

public class App {

    public static void main(String[] args) throws InterruptedException {
        Communicator communicator = new HttpCommunicator(args[0]);
        Strategy strategy = new BaseStrategy(new BattleArena());

        initDirection();
        register(communicator);
        play(communicator, strategy);
    }

    private static void play(Communicator communicator, Strategy strategy) throws InterruptedException {
        boolean gameEnded = false;
        while (!gameEnded) {
            while (!communicator.isMyTurn()) {
                Thread.sleep(10);
            }
            gameEnded = !communicator.makeMove(strategy.getNext(communicator.getLastEnemyMove()));
        }
    }

    private static void register(Communicator communicator) throws InterruptedException {
        while (!communicator.register()) {
            Thread.sleep(200);
        }
    }

    public static void initDirection() {
        Direction.DOWN.setOpposite(Direction.UP);
        Direction.UP.setOpposite(Direction.DOWN);
        Direction.DOWN_LEFT.setOpposite(Direction.UP_RIGHT);
        Direction.DOWN_RIGHT.setOpposite(Direction.UP_LEFT);
        Direction.UP_LEFT.setOpposite(Direction.DOWN_RIGHT);
        Direction.UP_RIGHT.setOpposite(Direction.DOWN_LEFT);
        Direction.LEFT.setOpposite(Direction.RIGHT);
        Direction.RIGHT.setOpposite(Direction.LEFT);
    }
}
