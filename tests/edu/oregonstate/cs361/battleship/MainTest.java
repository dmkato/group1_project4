package edu.oregonstate.cs361.battleship;

import com.google.gson.Gson;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import spark.Spark;
import spark.utils.IOUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static spark.Spark.awaitInitialization;


/**
 * Created by michaelhilton on 1/26/17.
 */
class MainTest {

    @BeforeAll
    public static void beforeClass() {
        Main.main(null);
        awaitInitialization();
    }

    @AfterAll
    public static void afterClass() {
        Spark.stop();
    }

    @Test
    public void testGetModel() {
        String newModel = "{\"aircraftCarrier\":{\"name\":\"AircraftCarrier\",\"length\":5,\"start\":{\"Across\":0,\"Down\":0},\"end\":{\"Across\":0,\"Down\":0},\"type\":\"default\"},\"battleship\":{\"isHit\":false,\"name\":\"Battleship\",\"length\":4,\"start\":{\"Across\":0,\"Down\":0},\"end\":{\"Across\":0,\"Down\":0},\"type\":\"CIA\"},\"submarine\":{\"isHit\":false,\"name\":\"Submarine\",\"length\":2,\"start\":{\"Across\":0,\"Down\":0},\"end\":{\"Across\":0,\"Down\":0},\"type\":\"CIA\"},\"clipper\":{\"isHit\":false,\"name\":\"Clipper\",\"length\":3,\"start\":{\"Across\":0,\"Down\":0},\"end\":{\"Across\":0,\"Down\":0},\"type\":\"civ\"},\"dhingy\":{\"isHit\":false,\"name\":\"Dhingy\",\"length\":1,\"start\":{\"Across\":0,\"Down\":0},\"end\":{\"Across\":0,\"Down\":0},\"type\":\"civ\"},\"fisher\":{\"isHit\":false,\"name\":\"Fisher\",\"length\":2,\"start\":{\"Across\":0,\"Down\":0},\"end\":{\"Across\":0,\"Down\":0},\"type\":\"civ\"},\"computer_aircraftCarrier\":{\"name\":\"Computer_AircraftCarrier\",\"length\":5,\"start\":{\"Across\":2,\"Down\":2},\"end\":{\"Across\":2,\"Down\":7},\"type\":\"default\"},\"computer_battleship\":{\"isHit\":false,\"name\":\"Computer_Battleship\",\"length\":4,\"start\":{\"Across\":2,\"Down\":8},\"end\":{\"Across\":6,\"Down\":8},\"type\":\"CIA\"},\"computer_submarine\":{\"isHit\":false,\"name\":\"Computer_Submarine\",\"length\":2,\"start\":{\"Across\":9,\"Down\":6},\"end\":{\"Across\":9,\"Down\":8},\"type\":\"CIA\"},\"computer_clipper\":{\"isHit\":false,\"name\":\"Computer_Clipper\",\"length\":3,\"start\":{\"Across\":1,\"Down\":1},\"end\":{\"Across\":1,\"Down\":3},\"type\":\"civ\"},\"computer_dhingy\":{\"isHit\":false,\"name\":\"Computer_Dhingy\",\"length\":1,\"start\":{\"Across\":10,\"Down\":10},\"end\":{\"Across\":10,\"Down\":10},\"type\":\"civ\"},\"computer_fisher\":{\"isHit\":false,\"name\":\"Computer_Fisher\",\"length\":2,\"start\":{\"Across\":7,\"Down\":1},\"end\":{\"Across\":7,\"Down\":2},\"type\":\"civ\"},\"playerHits\":[],\"playerMisses\":[],\"computerHits\":[],\"computerMisses\":[],\"scanResult\":false}";
        TestResponse res = request("GET", "/model");
        assertEquals(200, res.status);
        assertEquals(newModel,res.body);
    }

    @Test
    public void testPlaceShip() {
        BattleshipModel test = new BattleshipModel();
        Gson gson = new Gson();
        String model = gson.toJson(test);

        TestResponse res = request_post("POST", "/placeShip/aircraftCarrier/1/1/horizontal",model);
        assertEquals(200, res.status);
        assertEquals("{\"aircraftCarrier\":{\"name\":\"AircraftCarrier\",\"length\":5,\"start\":{\"Across\":0,\"Down\":0},\"end\":{\"Across\":0,\"Down\":0},\"type\":\"default\"},\"battleship\":{\"isHit\":false,\"name\":\"Battleship\",\"length\":4,\"start\":{\"Across\":0,\"Down\":0},\"end\":{\"Across\":0,\"Down\":0},\"type\":\"CIA\"},\"submarine\":{\"isHit\":false,\"name\":\"Submarine\",\"length\":2,\"start\":{\"Across\":0,\"Down\":0},\"end\":{\"Across\":0,\"Down\":0},\"type\":\"CIA\"},\"clipper\":{\"isHit\":false,\"name\":\"Clipper\",\"length\":3,\"start\":{\"Across\":0,\"Down\":0},\"end\":{\"Across\":0,\"Down\":0},\"type\":\"civ\"},\"dhingy\":{\"isHit\":false,\"name\":\"Dhingy\",\"length\":1,\"start\":{\"Across\":0,\"Down\":0},\"end\":{\"Across\":0,\"Down\":0},\"type\":\"civ\"},\"fisher\":{\"isHit\":false,\"name\":\"Fisher\",\"length\":2,\"start\":{\"Across\":0,\"Down\":0},\"end\":{\"Across\":0,\"Down\":0},\"type\":\"civ\"},\"computer_aircraftCarrier\":{\"name\":\"Computer_AircraftCarrier\",\"length\":5,\"start\":{\"Across\":2,\"Down\":2},\"end\":{\"Across\":2,\"Down\":7},\"type\":\"default\"},\"computer_battleship\":{\"isHit\":false,\"name\":\"Computer_Battleship\",\"length\":4,\"start\":{\"Across\":2,\"Down\":8},\"end\":{\"Across\":6,\"Down\":8},\"type\":\"CIA\"},\"computer_submarine\":{\"isHit\":false,\"name\":\"Computer_Submarine\",\"length\":2,\"start\":{\"Across\":9,\"Down\":6},\"end\":{\"Across\":9,\"Down\":8},\"type\":\"CIA\"},\"computer_clipper\":{\"isHit\":false,\"name\":\"Computer_Clipper\",\"length\":3,\"start\":{\"Across\":1,\"Down\":1},\"end\":{\"Across\":1,\"Down\":3},\"type\":\"civ\"},\"computer_dhingy\":{\"isHit\":false,\"name\":\"Computer_Dhingy\",\"length\":1,\"start\":{\"Across\":10,\"Down\":10},\"end\":{\"Across\":10,\"Down\":10},\"type\":\"civ\"},\"computer_fisher\":{\"isHit\":false,\"name\":\"Computer_Fisher\",\"length\":2,\"start\":{\"Across\":7,\"Down\":1},\"end\":{\"Across\":7,\"Down\":2},\"type\":\"civ\"},\"playerHits\":[],\"playerMisses\":[],\"computerHits\":[],\"computerMisses\":[],\"scanResult\":false}",res.body);
    }

    @Test
    public void testScan() {
        BattleshipModel test = new BattleshipModel();
        Gson gson = new Gson();
        String model = gson.toJson(test);

        TestResponse res = request_post("POST", "/scan/6/6",model);
        assertEquals(200, res.status);
        assertEquals("{\"aircraftCarrier\":{\"name\":\"AircraftCarrier\",\"length\":5,\"start\":{\"Across\":0,\"Down\":0},\"end\":{\"Across\":0,\"Down\":0},\"type\":\"default\"},\"battleship\":{\"isHit\":false,\"name\":\"Battleship\",\"length\":4,\"start\":{\"Across\":0,\"Down\":0},\"end\":{\"Across\":0,\"Down\":0},\"type\":\"CIA\"},\"submarine\":{\"isHit\":false,\"name\":\"Submarine\",\"length\":2,\"start\":{\"Across\":0,\"Down\":0},\"end\":{\"Across\":0,\"Down\":0},\"type\":\"CIA\"},\"clipper\":{\"isHit\":false,\"name\":\"Clipper\",\"length\":3,\"start\":{\"Across\":0,\"Down\":0},\"end\":{\"Across\":0,\"Down\":0},\"type\":\"civ\"},\"dhingy\":{\"isHit\":false,\"name\":\"Dhingy\",\"length\":1,\"start\":{\"Across\":0,\"Down\":0},\"end\":{\"Across\":0,\"Down\":0},\"type\":\"civ\"},\"fisher\":{\"isHit\":false,\"name\":\"Fisher\",\"length\":2,\"start\":{\"Across\":0,\"Down\":0},\"end\":{\"Across\":0,\"Down\":0},\"type\":\"civ\"},\"computer_aircraftCarrier\":{\"name\":\"Computer_AircraftCarrier\",\"length\":5,\"start\":{\"Across\":2,\"Down\":2},\"end\":{\"Across\":2,\"Down\":7},\"type\":\"default\"},\"computer_battleship\":{\"isHit\":false,\"name\":\"Computer_Battleship\",\"length\":4,\"start\":{\"Across\":2,\"Down\":8},\"end\":{\"Across\":6,\"Down\":8},\"type\":\"CIA\"},\"computer_submarine\":{\"isHit\":false,\"name\":\"Computer_Submarine\",\"length\":2,\"start\":{\"Across\":9,\"Down\":6},\"end\":{\"Across\":9,\"Down\":8},\"type\":\"CIA\"},\"computer_clipper\":{\"isHit\":false,\"name\":\"Computer_Clipper\",\"length\":3,\"start\":{\"Across\":1,\"Down\":1},\"end\":{\"Across\":1,\"Down\":3},\"type\":\"civ\"},\"computer_dhingy\":{\"isHit\":false,\"name\":\"Computer_Dhingy\",\"length\":1,\"start\":{\"Across\":10,\"Down\":10},\"end\":{\"Across\":10,\"Down\":10},\"type\":\"civ\"},\"computer_fisher\":{\"isHit\":false,\"name\":\"Computer_Fisher\",\"length\":2,\"start\":{\"Across\":7,\"Down\":1},\"end\":{\"Across\":7,\"Down\":2},\"type\":\"civ\"},\"playerHits\":[],\"playerMisses\":[],\"computerHits\":[],\"computerMisses\":[],\"scanResult\":false}",res.body);
    }


    @Test
    public void testValidFire(){
        BattleshipModel test = new BattleshipModel();
        Gson gson = new Gson();
        String model = gson.toJson(test);

        TestResponse res = request_post( "POST", "/fire/4/1", model);
        assertEquals( 200, res.status);
    }


    private TestResponse request_post(String method, String path, String body) {
        try {
            URL url = new URL("http://localhost:4567" + path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setDoOutput(true);
            if(body != null) {
                connection.setDoInput(true);
                byte[] outputInBytes = body.getBytes("UTF-8");
                OutputStream os = connection.getOutputStream();
                os.write(outputInBytes);
            }
            connection.connect();
            return new TestResponse(connection.getResponseCode(), body);
        } catch (IOException e) {
            e.printStackTrace();
            fail("Sending request failed: " + e.getMessage());
            return null;
        }
    }

    private TestResponse request(String method, String path) {
        try {
            URL url = new URL("http://localhost:4567" + path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setDoOutput(true);
            connection.connect();
            String body = IOUtils.toString(connection.getInputStream());
            return new TestResponse(connection.getResponseCode(), body);
        } catch (IOException e) {
            e.printStackTrace();
            fail("Sending request failed: " + e.getMessage());
            return null;
        }
    }

    private static class TestResponse {

        public final String body;
        public final int status;

        public TestResponse(int status, String body) {
            this.status = status;
            this.body = body;
        }

        public Map<String,String> json() {
            return new Gson().fromJson(body, HashMap.class);
        }
    }


}
