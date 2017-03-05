package edu.oregonstate.cs361.battleship;

/**
 * Created by guita on 3/1/2017.
 */
public class CivilianShip extends Ship {
    private boolean isHit;

    public CivilianShip(String n, int l,Coordinate s, Coordinate e) {
        type = "civ";
        name = n;
        length = l;
        start = s;
        end = e;
    }

}
