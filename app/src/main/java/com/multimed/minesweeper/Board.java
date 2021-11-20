package com.multimed.minesweeper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board {

    private List<Box> boxes;
    private  int size;

    public Board(int size) {
        this.size = size;
        boxes = new ArrayList<>();
        for (int i = 0; i < size*size; i++){
            boxes.add(new Box(Box.BLANK));
        }
    }

    public void generateBoard(int numBomb){
        int settedbombs = 0;
        while (settedbombs < numBomb){
            int y = (new Random()).nextInt(size);
            int x = (new Random()).nextInt(size);

            int index = toIndex(x, y);
            if (boxes.get(index).getValue() == Box.BLANK){
                boxes.set(index, new Box(Box.BOMB));
                settedbombs++;
            }
        }

        for (int x = 0; x < size; x++){
            for (int y = 0; y < size; y++){
                if (boxAt(x, y).getValue() != Box.BOMB){
                    List<Box> surrBox =  surrBox(x, y);
                    int countBombs = 0;
                    for (Box box:surrBox){
                        if (box.getValue() == Box.BOMB){
                            countBombs++;
                        }
                    }
                    if (countBombs > 0) {
                        boxes.set(x + (y*size), new Box(countBombs));
                    }
                }
            }
        }
    }

    public List<Box> surrBox(int x, int y){
        List<Box> surrBox = new ArrayList<>();

        List<Box> boxList = new ArrayList<>();
        for(int i = -1; i <= 1; i++){
            for(int j = -1; j <= 1; j++){
                if (i != 0 || j != 0) {
                    boxList.add(boxAt(x + i, y + j));
                }
            }
        }

        for (Box box: boxList){
            if (box != null){
                surrBox.add(box);
            }
        }

        return surrBox;
    }

    public void revBombs () {
        for (Box box: boxes){
            if (box.getValue() == Box.BOMB){
                box.setRevealed(true);
            }
        }
    }

    public int toIndex(int x, int y) {
        return x + (y*size);
    }

    public int[] toXY(int index){
        int y = index / size;
        int x =  index - (y*size);
        return new int[]{x,y};
    }

    public Box boxAt(int x, int y){
        if( x < 0 || x >= size || y < 0 || y >= size ){
            return null;
        }
        return boxes.get(toIndex(x,y));
    }

    public List<Box> getBoxes() {
        return boxes;
    }
}
