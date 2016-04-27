package com.epam.training.communication.responsedata;

public class IsMyTurnResponseData {

    private int statusCode;
    private String message;
    private boolean isMyTurn;
    private LastMoveData lastMove;
    private boolean first;

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

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

}
