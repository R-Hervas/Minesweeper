package com.multimed.minesweeper;

public class Box {

    public static final int BOMB = -1;
    public static final int BLANK = 0;

    private int value;
    private boolean revealed;
    private boolean flagged;

    public Box(int value){
        this.value = value;
        this.revealed = false;
        this.flagged = false;
    }

    public int getValue() {
        return value;
    }

    public boolean isRevealed() {
        return revealed;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }

    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }

}

