package edu.oregonstate.cs361.battleship;

import java.util.Random;
import java.util.StringJoiner;

/**
 * Created by guita on 3/13/2017.
 */
public class Easy extends BattleshipModel{
    //place ships
    private String[] shipName = {"aircraftCarrier", "battleship", "submarine", "dhingy", "clipper", "fisher"};

    private int convertToInt(){

        return 0;
    }

    //place ships
    public void ComputerPlaceShip() {
        String x = "0", y = "0";
        int numX = 0, numY = 0, z = 0;    //x = x-coord, y = y-coord, z = helper in placing

        //need to send all string variable to BattleshipModel.placd()
        for(int i = 0; i < 6; i++){
            //first place aircraftCarrier
            placeShip(shipName[i], x, y, "down");

            if(i == 0){
                int placeHolder;
            }
            //then place battleship 4 spots to right and down
            else if(i == 1){
                numX = Integer.parseInt(x);
                numY = Integer.parseInt(y);

                numX = numX + 4;
                numY = numY + 4;

                x = String.valueOf(numX);
                y = String.valueOf(numY);
            }
            //then place submarine 3 spots right and down
            else if(i == 2){
                numX = Integer.parseInt(x);
                numY = Integer.parseInt(y);

                numX = numX + 3;
                numY = numY + 3;

                x = String.valueOf(numX);
                y = String.valueOf(numY);
            }
            //then place dhingy in bottom right corner
            else if(i == 3){
                numX = numY = 9;

                x = String.valueOf(numX);
                y = String.valueOf(numY);
            }
            //then place fisher top right corner
            else if(i == 4){
                numX = 0;
                numY = 9;

                x = String.valueOf(numX);
                y = String.valueOf(numY);
            }
            //then place clipper lower right hand corner
            else if(i == 5){
                numX = 9;
                numY = 0;

                x = String.valueOf(numX);
                y = String.valueOf(numY);
            }
        }
    }

    /*The function will fire on every other tile. FIRST, it will cover 0 and even numbered tiles
     *i.e. array[0][0], array[0][2], array[0][4]...
     *Then it will fire down; array[1][0]...array[9][0] covering the even tiles as above
     *Then it will go back to the top and fire on odd tiles
     *i.e. array[0][1], array[0][3], array[0][5]...
     *Then it will fire down in the same fashion stated above
     */

    @Override
    public void shootAtPlayer(){
        int max = 10, min = 1, row = 0, col = 0;
        Coordinate coor = new Coordinate(row, col);
        //firing on 0 and even numbers
        for(int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j = j + 2) {
                coor.setDown(i);
                coor.setAcross(j);

                //check for duplicates
                for(ShotData s : computerHits){
                    if(s.loc.getAcross() == coor.getAcross() && s.loc.getDown() == coor.getDown()){
                        shootAtPlayer();
                        playerShot(coor);
                        return;
                    }
                }
                for(ShotData s : computerMisses){
                    if(s.loc.getAcross() == coor.getAcross() && s.loc.getDown() == coor.getDown()){
                        shootAtPlayer();
                        playerShot(coor);
                        return;
                    }
                }
            }
        }
        playerShot(coor);
        for(int i = 0; i < 10; i++){
            for(int j = 1; j < 10; j=j+2){
                coor.setDown(i);
                coor.setAcross(j);

                //check for duplicates
                for(ShotData s : computerHits){
                    if(s.loc.getAcross() == coor.getAcross() && s.loc.getDown() == coor.getDown()){
                        shootAtPlayer();
                        playerShot(coor);
                        return;
                    }
                }
                for(ShotData s : computerMisses){
                    if(s.loc.getAcross() == coor.getAcross() && s.loc.getDown() == coor.getDown()){
                        shootAtPlayer();
                        playerShot(coor);
                        return;
                    }
                }
            }
        }
    }
}


