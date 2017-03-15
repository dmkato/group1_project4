package edu.oregonstate.cs361.battleship;

/**
 * Created by volkmanj on 3/4/2017.
 */
public class StealthShip extends Ship {
    private boolean isHit;  //lol that's clever

    public StealthShip(String n, int l, Coordinate s, Coordinate e) {
        type = "CIA";
        name = n;
        length = l;
        start = s;
        end = e;
    }
}
