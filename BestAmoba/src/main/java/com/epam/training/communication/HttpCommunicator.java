package com.epam.training.communication;

import java.io.IOException;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;

import com.epam.training.communication.responsedata.IsMyTurnResponseData;
import com.epam.training.communication.responsedata.PutRequestData;
import com.epam.training.communication.responsedata.PutResponseData;
import com.epam.training.communication.responsedata.RegisterRequestAnswer;
import com.epam.training.communication.responsedata.RegisterRequestData;
import com.epam.training.communication.responsedata.UuidData;
import com.epam.training.domain.Coordinate;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HttpCommunicator implements Communicator {

    private static final int SERVER_PORT = 8080;
    private static final String BASE_ENDPOINT = "/xoxo";
    private static final String REGISTER_ENDPOINT = BASE_ENDPOINT + "/reg";
    private static final String IS_MY_TURN_ENDPOINT = BASE_ENDPOINT + "/ismyturn";
    private static final String MAKE_A_MOVE_ENDPOINT = BASE_ENDPOINT + "/put";

    private static final String OUR_NAME = "Best Team";

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
        HttpResponse response = executePostRequest(REGISTER_ENDPOINT, new RegisterRequestData(OUR_NAME));
        if (response == null) {
            return false;
        }
        try {
            RegisterRequestAnswer readValue = mapper.readValue(response.getEntity().getContent(), RegisterRequestAnswer.class);
            uuid = readValue.getUuid();
            System.out.println(readValue.getGid().toString());
            System.out.println(readValue.getType().toString());
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
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 400) {
            System.out.println("Bad fcking request!");
            throw new BadRequestException();
        } else if (statusCode == 100) {
            System.out.println("Game ended!");
            throw new GameEndedException();
        }
        try {
            IsMyTurnResponseData data = mapper.readValue(response.getEntity().getContent(), IsMyTurnResponseData.class);
            if (data.isFirst()) {
                lastMove = null;
            } else {
                lastMove = new Coordinate(data.getLastMove().getX(), data.getLastMove().getY());
            }
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
