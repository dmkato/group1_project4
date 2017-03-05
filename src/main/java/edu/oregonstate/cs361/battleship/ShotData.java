package edu.oregonstate.cs361.battleship;

/**
 * Created by volkmanj on 3/4/2017.
 */
public class ShotData {
    public Coordinate loc;
    public String type;

    public ShotData(Coordinate location, String ship_type){
        loc = location;
        type = ship_type;
    }
}
