package com.fourshape.numbermatch.puzzle_core.structure;

public class Cell {

    private int row;
    private int col;
    private int value;
    private boolean isSolved;

    public Cell (int row, int col, int value, boolean isSolved) {
        this.row = row;
        this.col = col;
        this.value = value;
        this.isSolved = isSolved;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setSolved(boolean solved) {
        isSolved = solved;
    }

    public int getValue() {
        return value;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public boolean isSolved() {
        return isSolved;
    }
}
