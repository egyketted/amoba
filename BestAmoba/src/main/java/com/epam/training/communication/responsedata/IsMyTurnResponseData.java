package com.epam.training.communication.responsedata;

public class IsMyTurnResponseData {

    private boolean isMyTrun;
    private LastMoveData lastMove;

    public boolean isMyTrun() {
        return isMyTrun;
    }

    public void setMyTrun(boolean isMyTrun) {
        this.isMyTrun = isMyTrun;
    }

    public LastMoveData getLastMove() {
        return lastMove;
    }

    public void setLastMove(LastMoveData lastMove) {
        this.lastMove = lastMove;
    }

}
