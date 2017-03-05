package edu.oregonstate.cs361.battleship;

/**
 * Created by michaelhilton on 1/8/17.
 */
public class Coordinate {
    private int Across;
    private int Down;

    public Coordinate(int across, int down) {
        Across = across;
        Down = down;
    }

    public int getDown() {
        return Down;
    }

    public void setDown(int down) {
        Down = down;
    }

    public int getAcross() {
        return Across;
    }

    public void setAcross(int across) {
        Across = across;
    }
}
