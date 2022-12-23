package com.fourshape.numbermatch.puzzle_core;

public class GameControlStatus {

    public int addRowStatus, hintStatus;

    public GameControlStatus () {
        addRowStatus = -1;
        hintStatus = -1;
    }

    public void resetRowControl () {
        addRowStatus = GameControlLimit.ADD_ROW_LIMIT;
    }

    public void resetHintControl () {
        hintStatus = GameControlLimit.HINT_LIMIT;
    }

    public void setAddRowStatus(int addRowStatus) {
        this.addRowStatus = addRowStatus;
    }

    public void setHintStatus(int hintStatus) {
        this.hintStatus = hintStatus;
    }

    public int getAddRowStatus() {
        return addRowStatus;
    }

    public int getHintStatus() {
        return hintStatus;
    }

    public boolean isAddRowControlAvailable () {
        return addRowStatus > 0;
    }

    public boolean isHintControlAvailable () {
        return hintStatus > 0;
    }

    public void consumeAddMoreRowControl () {
        if (isAddRowControlAvailable())
            --addRowStatus;
    }

    public void consumeHintControl () {
        if (isHintControlAvailable())
            --hintStatus;
    }

}
