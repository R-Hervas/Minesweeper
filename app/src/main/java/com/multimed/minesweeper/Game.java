package com.multimed.minesweeper;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private Board board;
    private boolean clearMode;
    private boolean flaggedMode;
    private boolean isGameOver;
    private int flagCount;
    private int numberOfBombs;


    public Game(int size, int numBombs) {
        this.clearMode = true;

        this.flaggedMode = false;

        this.isGameOver = false;

        this.flagCount = 0;

        this.numberOfBombs = numBombs;

        this.board = new Board(size);

        this.board.generateBoard(numBombs);
    }

    public void handleBoxClick(Box box) {
        if (!isGameOver) {
            if (clearMode && !box.isFlagged()) {
                reveal(box);
            }
        }
    }

    public void handleBoxLongClick(Box box) {
        if (!isGameOver) {
            flag(box);
        }
    }

    public void reveal(Box box) {
        int index = getBoard().getBoxes().indexOf(box);
        getBoard().getBoxes().get(index).setRevealed(true);

        if (box.getValue() == Box.BLANK) {
            List<Box> toReveal = new ArrayList<>();
            List<Box> toCheckAdjacents = new ArrayList<>();

            toCheckAdjacents.add(box);

            while (toCheckAdjacents.size() > 0) {
                Box c = toCheckAdjacents.get(0);
                int boxIndex = getBoard().getBoxes().indexOf(c);
                int[] boxCoord = getBoard().toXY(boxIndex);
                for (Box adjacent : getBoard().surrBox(boxCoord[0], boxCoord[1])) {
                    if (adjacent.getValue() == Box.BLANK) {
                        if (!toReveal.contains(adjacent)) {
                            if (!toCheckAdjacents.contains(adjacent)) {
                                toCheckAdjacents.add(adjacent);
                            }
                        }
                    } else {
                        if (!toReveal.contains(adjacent)) {
                            toReveal.add(adjacent);
                        }
                    }
                }
                toCheckAdjacents.remove(c);
                toReveal.add(c);
            }

            for (Box c : toReveal) {
                if (c.isFlagged()){
                    c.setFlagged(false);
                }
                c.setRevealed(true);
            }
        } else if (box.getValue() == Box.BOMB) {
            isGameOver = true;
        }
    }

    public boolean isGameWon() {
        int numUnrev = 0;
        for (Box c : getBoard().getBoxes()) {
            if (c.getValue() != Box.BOMB && c.getValue() != Box.BLANK && !c.isRevealed()) {
                numUnrev++;
            }
        }

        if (numUnrev == 0) {
            return true;
        } else {
            return false;
        }
    }

    public void flag(Box box) {
        if (!box.isRevealed() && (flagCount < numberOfBombs || box.isFlagged())) {
            box.setFlagged(!box.isFlagged());
            int count = 0;
            for (Box c : getBoard().getBoxes()) {
                if (c.isFlagged()) {
                    count++;
                }
            }
            flagCount = count;
        }
    }

    public Board getBoard() {
        return board;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public int getFlagCount() {
        return flagCount;
    }

    public int getNumberOfBombs() {
        return numberOfBombs;
    }
}
