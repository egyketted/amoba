package com.epam.training.communication.responsedata;

public class IsMyTurnResponseData {

    private boolean isMyTurn;
    private LastMoveData lastMove;

    public boolean getIsMyTurn() {
        return isMyTurn;
    }

    public void setisMyTurn(boolean isMyTurn) {
        this.isMyTurn = isMyTurn;
    }

    public LastMoveData getLastMove() {
        return lastMove;
    }

    public void setLastMove(LastMoveData lastMove) {
        this.lastMove = lastMove;
    }

    @Override
    public String toString() {
        return "IsMyTurnResponseData [isMyTurn=" + isMyTurn + ", lastMove=" + lastMove + "]";
    }

}
