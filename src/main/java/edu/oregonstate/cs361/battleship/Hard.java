package edu.oregonstate.cs361.battleship;

import java.util.Random;
import java.util.StringJoiner;

/**
 * Created by wongnich on 3/14/17.
 */
public class Hard {

    //check to see if we want to fire randomly or not:
    //check to see if previous coordinates hit a ship, if so don't choose next coordinates randomly:
    //if(!flag): // flag set to false means no ship was hit

    //hardmode:
    //should fire at nearby locations if a player ship is hit
    //should then fire in a contiguous line (smart firing)
        /*
        else:
            check if prev coord is a hit (is coor in computerHits?):
                fire at left, right, top, or bottom position (relative to coords)
            if (ship sink):
                set flag to false -> so it will continue firing randomly
         */


    private String[] shipName = {"aircraftCarrier", "battleship", "submarine", "clipper", "dhingy", "fisher"};
    private String[] direction = {"up", "down"};
    private int[][] board = new int[10][10];    //simulate game board 0's - free, 1's not free , all values are 0 by default
    private boolean flag = false;

    BattleshipModel model = new BattleshipModel();

    //function follows the noCollision. It checks the opposite direction of what was tested in noCollision
    public boolean checkDecrement(int Row, int Col, String direction, int length, int start){    //checks decrement for noCollision function
        for(int x=start; x>=0; x--){
            //if(Row-length <0 || Col-length<0)   return false;   //we already checked bounds
            if(direction=="up"){
                if(board[x][Col]==1)   return false;   //there is no way to place the ship (down->up, or up->down)
            }
            else if(direction=="down"){
                if(board[Row][x]==1)    return false;   //collision occurred
            }
        }
        //otherwise conditions passed, we can place ship, first fill the simulated board
        for(int i=start; i>=0; i--){
            if(direction=="up") board[start][Col]=1;
            else if(direction=="down")  board[Row][start]=1;
        }

        return true;
    }
    //function checks for collisions with random placement
    public boolean noCollision(int Row, int Col, String direction, String shipName){
        int length=0;
        int start;
        //get length of ship
        if(shipName=="aircraftCarrier") length = 5;
        else if(shipName=="battleship") length = 4;
        else if(shipName=="submarine")  length = 2;
        else if(shipName=="clipper")    length = 3;
        else if(shipName=="dhingy")     length = 1;
        else if(shipName=="fisher")     length = 2;

        //if the ship will never collide with current layout, then return true, otherwise return false
        //set the board accordingly if there is no collision
        if(direction=="up") start = Row;    //will only increment/decrement the Rows
        else    start = Col;                //will only increment/decrement the Cols

        for(int x=start; x<length; x++){
            if(Row+length >=10 || Col+length >= 10 || Row-length <0 || Col-length <0)    return false;   //this means out of bounds
            if(direction=="up"){    //will be placing vertical, only rows are changing
                if(board[x][Col]==1)   return checkDecrement(Row, Col, direction, length, start);   //if any spots in line of path are 1, then checkDecrement
            }
            else if(direction=="down"){ //will be placing horizontally, only cols are changing
                if(board[Row][x]==1)    return checkDecrement(Row, Col, direction, length, start);
            }
        }

        //conditions passed, we can place ships
        for(int i=start; i<length; i++){
            if(direction=="up")    board[start][Col] = 1;      //set position to "filled"
            else if(direction=="down") board[Row][start] = 1;
        }

        return true;
    }

    //places ships randomly
    public void place() {
        Random random = new Random();
        String x, y;
        int max = 10;
        int min = 1;
        int randIterator = random.nextInt(2) + 1;   //random num 1 or 2
        int randRow = random.nextInt(max - min + 1) + min, randCol = random.nextInt(max - min + 1) + min;

        for(int i = 0; i < 6; i++){
            //randomize start place of ship without collision
            while(!noCollision(randRow, randCol, direction[randIterator], shipName[i])) {
                randRow = random.nextInt(max - min + 1) + min;
                randCol = random.nextInt(max - min + 1) + min;
                randIterator = random.nextInt(2) + 1;
            }
            //convert our randomized coordinates to a string for placeShip function
            x = String.valueOf(randRow);
            y = String.valueOf(randCol);

            //place the ship, should be able to place without collision
            model.placeShip(shipName[i], x, y, direction[randIterator]);
        }

    }

    //function that takes in previous coordinates fired, and decides where to fire based on input
    public /*Coordinate*/ void fire(/*Coordinate prev*/){
        int max = 10;
        int min = 1;
        Random random = new Random();
        int randRow=0, randCol=0;

        //check if shot was hit or miss:
        //if(coords == hit) flag = true
        //else  flag = false

        if(!flag){  //if coordinates were a miss, select random coordinates
            randRow = random.nextInt(max - min + 1) + min;
            randCol = random.nextInt(max - min + 1) + min;
        }
        else {  //otherwise select coordinates nearby previous coordinates
            //simply go: maxCols = cols+1, maxRows = rows+1
            //           minCols = cols-1, minRows = rows-1
            randRow = random.nextInt(max - min + 1) + min;
            randCol = random.nextInt(max - min + 1) + min;
        }
        Coordinate coor = new Coordinate(randRow, randCol);


        // Check for duplicates
        for (ShotData s: model.computerHits) {
            if(s.loc.getAcross() == coor.getAcross() && s.loc.getDown() == coor.getDown()){
                model.shootAtPlayer();
                model.playerShot(coor);
                return;
            }
        }
        for (ShotData s: model.computerMisses) {
            if(s.loc.getAcross() == coor.getAcross() && s.loc.getDown() == coor.getDown()){
                model.shootAtPlayer();
                model.playerShot(coor);
                return;
            }
        }

        model.playerShot(coor);

    }
}
