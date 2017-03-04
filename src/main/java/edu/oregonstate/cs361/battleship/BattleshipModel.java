package edu.oregonstate.cs361.battleship;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by michaelhilton on 1/4/17.
 */

public class BattleshipModel {

    private Ship aircraftCarrier = new Ship("AircraftCarrier",5, new Coordinate(0,0),new Coordinate(0,0));
    private Ship battleship = new Ship("Battleship",4, new Coordinate(0,0),new Coordinate(0,0));
    private Ship submarine = new Ship("Submarine",2, new Coordinate(0,0),new Coordinate(0,0));
    private Ship clipper = new Ship("Clipper", 3, new Coordinate(0, 0), new Coordinate(0, 0));
    private Ship dhingy = new Ship("Dhingy", 1, new Coordinate(0, 0), new Coordinate(0, 0));
    private Ship fisher = new Ship("Fisher", 2, new Coordinate(0, 0), new Coordinate(0, 0));


    private Ship computer_aircraftCarrier = new Ship("Computer_AircraftCarrier",5, new Coordinate(2,2),new Coordinate(2,7));
    private Ship computer_battleship = new Ship("Computer_Battleship",4, new Coordinate(2,8),new Coordinate(6,8));
    private Ship computer_submarine = new Ship("Computer_Submarine",2, new Coordinate(9,6),new Coordinate(9,8));
    private Ship computer_clipper = new Ship("Computer_Clipper", 3, new Coordinate(1, 1), new Coordinate(1, 3));
    private Ship computer_dhingy = new Ship("Computer_Dhingy", 1, new Coordinate(10, 10), new Coordinate(10, 10));
    private Ship computer_fisher = new Ship("Computer_Fisher", 2, new Coordinate(7, 1), new Coordinate(7, 2));


    ArrayList<Coordinate> playerHits;
    private ArrayList<Coordinate> playerMisses;
    ArrayList<Coordinate> computerHits;
    private ArrayList<Coordinate> computerMisses;
    ArrayList<Coordinate> computerHitsCivShip;
    ArrayList<Coordinate> computerHitsCIAShip;
    ArrayList<Coordinate> playerHitsCivShip;
    ArrayList<Coordinate> playerHitsCIAShip;

    boolean scanResult = false;



    public BattleshipModel() {
        playerHits = new ArrayList<>();
        playerMisses= new ArrayList<>();
        computerHits = new ArrayList<>();
        computerMisses= new ArrayList<>();
        computerHitsCivShip = new ArrayList<>();
        computerHitsCIAShip = new ArrayList<>();
        playerHitsCivShip = new ArrayList<>();
        playerHitsCIAShip = new ArrayList<>();
    }


    public Ship getShip(String shipName) {
        if (shipName.equalsIgnoreCase("aircraftcarrier")) {
            return aircraftCarrier;
        } if(shipName.equalsIgnoreCase("battleship")) {
            return battleship;
        } if(shipName.equalsIgnoreCase("dhingy")) {
            return dhingy;
        } if(shipName.equalsIgnoreCase("clipper")) {
            return clipper;
        } if(shipName.equalsIgnoreCase("fisher")){
            return fisher;
        } if(shipName.equalsIgnoreCase("submarine")) {
            return submarine;
        } else {
            return null;
        }
    }

    public BattleshipModel placeShip(String shipName, String row, String col, String orientation) {
        int rowint = Integer.parseInt(row);
        int colInt = Integer.parseInt(col);
        if(orientation.equals("horizontal")){
            if (shipName.equalsIgnoreCase("aircraftcarrier")) {
                this.getShip(shipName).setLocation(new Coordinate(rowint,colInt),new Coordinate(rowint,colInt+4));
            } if(shipName.equalsIgnoreCase("battleship")) {
                this.getShip(shipName).setLocation(new Coordinate(rowint,colInt),new Coordinate(rowint,colInt+3));
            } if(shipName.equalsIgnoreCase("dhingy")) {
                this.getShip(shipName).setLocation(new Coordinate(rowint,colInt),new Coordinate(rowint,colInt));
            } if(shipName.equalsIgnoreCase("clipper")) {
                this.getShip(shipName).setLocation(new Coordinate(rowint, colInt), new Coordinate(rowint, colInt + 2));
            } if(shipName.equalsIgnoreCase("fisher")){
                this.getShip(shipName).setLocation(new Coordinate(rowint, colInt), new Coordinate(rowint, colInt + 1));
            }if(shipName.equalsIgnoreCase("submarine")) {
                this.getShip(shipName).setLocation(new Coordinate(rowint, colInt), new Coordinate(rowint, colInt + 1));
            }
        }else{
            //vertical
                if (shipName.equalsIgnoreCase("aircraftcarrier")) {
                    this.getShip(shipName).setLocation(new Coordinate(rowint,colInt),new Coordinate(rowint+4,colInt));
                } if(shipName.equalsIgnoreCase("battleship")) {
                    this.getShip(shipName).setLocation(new Coordinate(rowint,colInt),new Coordinate(rowint+3,colInt));
                } if(shipName.equalsIgnoreCase("dhingy")) {
                    this.getShip(shipName).setLocation(new Coordinate(rowint,colInt),new Coordinate(rowint,colInt));
                } if(shipName.equalsIgnoreCase("clipper")) {
                this.getShip(shipName).setLocation(new Coordinate(rowint, colInt), new Coordinate(rowint + 2, colInt));
                } if(shipName.equalsIgnoreCase("fisher")){
                    this.getShip(shipName).setLocation(new Coordinate(rowint, colInt), new Coordinate(rowint + 1, colInt));
                }if(shipName.equalsIgnoreCase("submarine")) {
                    this.getShip(shipName).setLocation(new Coordinate(rowint, colInt), new Coordinate(rowint + 1, colInt));
                }
        }
        return this;
    }

    public void shootAtComputer(int row, int col) {
        Coordinate coor = new Coordinate(row, col);
        if (computer_aircraftCarrier.covers(coor)) {
            computerHits.add(coor);
        } else if (computer_battleship.covers(coor)) {
            computerHits.add(coor);
            // computerHitsCIAShip.add(coor);
        } else if (computer_clipper.covers(coor)) {
            computerHits.add(coor);
            // computerHitsCivShip.add(coor);
        } else if (computer_dhingy.covers(coor)) {
            computerHits.add(coor);
            // computerHitsCivShip.add(coor);
        } else if (computer_fisher.covers(coor)) {
            computerHits.add(coor);
            // computerHitsCivShip.add(coor);
        } else if (computer_submarine.covers(coor)){
            computerHits.add(coor);
            // computerHitsCIAShip.add(coor);
        } else {
            computerMisses.add(coor);
        }
    }

    public void shootAtPlayer() {
        int max = 10;
        int min = 1;
        Random random = new Random();
        int randRow = random.nextInt(max - min + 1) + min;
        int randCol = random.nextInt(max - min + 1) + min;

        Coordinate coor = new Coordinate(randRow,randCol);
        playerShot(coor);
    }

    void playerShot(Coordinate coor) {
        if(playerMisses.contains(coor)){
            System.out.println("Dupe");
            this.shootAtPlayer();
        }

        if(aircraftCarrier.covers(coor)){
            playerHits.add(coor);
        }else if (battleship.covers(coor)){
            playerHitsCIAShip.add(coor);
        }else if (dhingy.covers(coor)){
            playerHitsCivShip.add(coor);
        }else if (clipper.covers(coor)) {
            playerHitsCivShip.add(coor);
        }else if (fisher.covers(coor)){
            playerHitsCivShip.add(coor);
        }else if (submarine.covers(coor)){
            playerHitsCIAShip.add(coor);
        }else {
            playerMisses.add(coor);
        }
    }

    public void scan(int rowInt, int colInt) {
        Coordinate coor = new Coordinate(rowInt, colInt);
        scanResult = false;
        if (computer_aircraftCarrier.scan(coor)) {
            scanResult = true;
        } else if (computer_battleship.scan(coor)){
            scanResult = true;
	    } else if (computer_clipper.scan(coor)) {
            scanResult = true;
        } else if (computer_dhingy.scan(coor)) {
            scanResult = true;
        } else if (computer_fisher.scan(coor)) {
            scanResult = true;
        } else {
            scanResult = false;
        }
    }

    public boolean getScanResult() {
        return scanResult;
    }
}
