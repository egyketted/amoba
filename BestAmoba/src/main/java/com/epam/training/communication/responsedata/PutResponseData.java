package com.epam.training.communication.responsedata;

public class PutResponseData {

    private int statusCode;
    private String message;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int status) {
        this.statusCode = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "PutResponseData [statusCode=" + statusCode + ", message=" + message + "]";
    }

}
