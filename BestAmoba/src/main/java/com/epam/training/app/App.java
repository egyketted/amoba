package com.epam.training.app;

import com.epam.training.communication.Communicator;
import com.epam.training.communication.HttpCommunicator;
import com.epam.training.domain.Coordinate;
import com.epam.training.strategy.Strategy;

public class App {
    public static void main(String[] args) throws InterruptedException {
        Communicator communicator = new HttpCommunicator(args[0]);
        Strategy strategy = new Strategy() {

            @Override
            public Coordinate getNext(Coordinate lastMove) {
                return new Coordinate((int) (Math.random() * 1000), (int) (Math.random() * 1000));
            }
        };
        while (!communicator.register()) {
            Thread.sleep(200);
        }
        boolean gameEnded = false;
        while (!gameEnded) {
            while (!communicator.isMyTurn()) {
                Thread.sleep(50);
            }
            gameEnded = !communicator.makeMove(strategy.getNext(communicator.getLastEnemyMove()));
        }
    }
}
