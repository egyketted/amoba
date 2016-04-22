package com.epam.training.communication;

import java.io.IOException;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;

import com.epam.training.communication.responsedata.IsMyTurnResponseData;
import com.epam.training.communication.responsedata.PutRequestData;
import com.epam.training.communication.responsedata.PutResponseData;
import com.epam.training.communication.responsedata.RegisterRequestAnswer;
import com.epam.training.communication.responsedata.UuidData;
import com.epam.training.domain.Coordinate;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HttpCommunicator implements Communicator {

    private static final int SERVER_PORT = 8080;
    private static final String BASE_ENDPOINT = "/xoxo";
    private static final String REGISTER_ENDPOINT = BASE_ENDPOINT + "/reg";
    private static final String IS_MY_TURN_ENDPOINT = BASE_ENDPOINT + "/ismyturn";
    private static final String MAKE_A_MOVE_ENDPOINT = BASE_ENDPOINT + "/put";

    private final HttpHost host;
    private final HttpClient client;

    private String uuid;
    private ObjectMapper mapper = new ObjectMapper();
    private Coordinate lastMove;

    public HttpCommunicator(String serverAddress) {
        this.host = new HttpHost(serverAddress, SERVER_PORT);
        this.client = HttpClientBuilder.create().build();
    }

    @Override
    public boolean register() {
        HttpResponse response = executeGetRequest(REGISTER_ENDPOINT);
        if (response == null) {
            return false;
        }
        try {
            uuid = mapper.readValue(response.getEntity().getContent(), RegisterRequestAnswer.class).getUuid();
        } catch (UnsupportedOperationException | IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean isMyTurn() {
        HttpResponse response = executePostRequest(IS_MY_TURN_ENDPOINT, new UuidData(uuid));
        if (response == null) {
            return false;
        }
        try {
            IsMyTurnResponseData data = mapper.readValue(response.getEntity().getContent(), IsMyTurnResponseData.class);
            lastMove = new Coordinate(data.getLastMove().getX(), data.getLastMove().getY());
            System.out.println("isMyTurnResponse: " + data);
            return data.getIsMyTurn();
        } catch (UnsupportedOperationException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Coordinate getLastEnemyMove() {
        return lastMove;
    }

    @Override
    public boolean makeMove(Coordinate coordinate) {
        HttpResponse response = executePostRequest(MAKE_A_MOVE_ENDPOINT, new PutRequestData(uuid, coordinate.getX(), coordinate.getY()));
        PutResponseData data = null;
        try {
            data = mapper.readValue(response.getEntity().getContent(), PutResponseData.class);
            System.out.println("makeMoveResponse: " + data);
        } catch (UnsupportedOperationException | IOException e) {
            e.printStackTrace();
        }
        return data != null && data.getStatusCode() == 200;
    }

    private HttpResponse executeGetRequest(String endpoint) {
        HttpGet reqest = new HttpGet(endpoint);
        HttpResponse response = null;
        try {
            response = client.execute(host, reqest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    private HttpResponse executePostRequest(String endpoint, Object data) {
        HttpPost request = new HttpPost(endpoint);
        request.setHeader(new BasicHeader("Content-Type", "Application/json"));
        HttpResponse response = null;
        try {
            request.setEntity(new StringEntity(mapper.writeValueAsString(data)));
            response = client.execute(host, request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

}
