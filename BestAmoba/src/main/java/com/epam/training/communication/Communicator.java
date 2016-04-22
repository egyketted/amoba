package com.epam.training.communication;

import com.epam.training.domain.Coordinate;

public interface Communicator {

    public boolean register();

    public boolean isMyTurn();

    public Coordinate getLastEnemyMove();

    public boolean makeMove(Coordinate coordinate);

}
