package com.fourshape.numbermatch.puzzle_core;

public class GameStep {

    private int step;

    public void reset () {
        this.step = 1;
    }

    public GameStep () {
        this.step = 1;
    }

    public int getStep() {
        return this.step;
    }

    public void setStep (int step) {
        this.step = step;
    }

    public void increaseStep () {
        ++this.step;
    }
}
