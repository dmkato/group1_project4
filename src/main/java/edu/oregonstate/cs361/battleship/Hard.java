package edu.oregonstate.cs361.battleship;

import java.util.Random;
import java.util.StringJoiner;

/**
 * Created by wongnich on 3/14/17.
 */
/*
This implements a hard mode for the game battleship:
The computer will randomly place its ships at the start of each game. None of the ships will be placed on top of each other (collision)
The computer will continue firing randomly until it hits a player ship. It will then fire at a nearby location.
    If it misses, it will fire randomly again, if it hits again, it will fire at another nearby location and so on.
 */

public class Hard {

    private String[] shipName = {"aircraftCarrier", "battleship", "submarine", "clipper", "dhingy", "fisher"};
    private String[] direction = {"horizontal", "vertical"};
    private int[][] board = new int[10][10];    //simulate game board 0's - free, 1's not free , all values are 0 by default
    private boolean flag = false;

    BattleshipModel model = new BattleshipModel();
    
    /*bojack: this method is clever, preventing the ships from existing in the same squares. Good job*/
    //function follows the noCollision. It checks the opposite direction of what was tested in noCollision
    public boolean checkDecrement(int Row, int Col, String direction, int length, int start){    //checks decrement for noCollision function
        for(int x=start; x>=length+start; x--){ //want to decrement for length of ship
            //if(Row-length <0 || Col-length<0)   return false;   //we already checked bounds in noCollision
            if(direction=="vertical"){
                if(board[x][Col]==1)   return false;   //there is no way to place the ship (down->up, or up->down)
            }
            else if(direction=="horizontal"){
                if(board[Row][x]==1)    return false;   //collision occurred
            }
        }
        //otherwise conditions passed, we can place ship, first fill the simulated board
        for(int i=start; i>=length+start; i--){
            if(direction=="vertical") board[i][Col]=1;
            else if(direction=="horizontal")  board[Row][i]=1;
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
        if(direction=="vertical") start = Row;    //will only increment/decrement the Rows
        else    start = Col;                //will only increment/decrement the Cols

        for(int x=start; x<length; x++){
            if(Row+length >=10 || Col+length >= 10 || Row-length <0 || Col-length <0)    return false;   //this means out of bounds
            if(direction=="horizontal"){    //will be placing horizontally, only cols are changing
                if(board[x][Col]==1)   return checkDecrement(Row, Col, direction, length, start);   //if any spots in line of path are 1, then checkDecrement
            }
            else if(direction=="vertical"){ //will be placing vertically, only rows are changing
                if(board[Row][x]==1)    return checkDecrement(Row, Col, direction, length, start);
            }
        }

        //conditions passed, we can place ships
        for(int i=start; i<length; i++){
            if(direction=="vertical")    board[i][Col] = 1;      //set position to "filled"
            else if(direction=="horizontal") board[Row][i] = 1;
        }

        return true;
    }

    /*bojack: this function may end up being in a different class.*/
    /*wongnich: I was thinking about that, but the easy mode and hard mode have different ship placement attributes*/
    //places ships randomly
    public void place() {
        Random random = new Random();
        String x, y;
        int max = 10; //bojack: usually these types of variables would be global constants instead of local ints
        int min = 1;  //that way if we wanted to expand the board to 25x25 we would only have to change the constants
                      //you're also using these values elsewhere in the file. that's why I mention it.
        
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

        /*bojack: you might want to make the following piece of code a separate method. that way you can compare coordinate types
        in several functions. Implementing this in another class may be useful for a few other objectives. Either way,
        you can use that for the conditional below.*/
        
        //check if shot was hit or miss:
        //if(coords == hit) flag = true
        //else  flag = false

        /*bojack: your flag is commented out above. this if loop needs a different conditional.*/
        if(!flag){  //if coordinates were a miss, select random coordinates
            randRow = random.nextInt(max - min + 1) + min;
            randCol = random.nextInt(max - min + 1) + min;
        }
        
        /*bojack: you need a way to store shotdata from previous shots fired. that way it doesn't shoot the same place twice.*/
        else {  //otherwise select coordinates nearby previous coordinates
            //simply go: maxCols = cols+1, maxRows = rows+1
            //           minCols = cols-1, minRows = rows-1
            randRow = random.nextInt(max - min + 1) + min;
            randCol = random.nextInt(max - min + 1) + min;
        }
        Coordinate coor = new Coordinate(randRow, randCol);

        /*bojack: the idea behind this makes sense.
            Daniel and I may change the class structure so the . objects may change in the near future.*/
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
