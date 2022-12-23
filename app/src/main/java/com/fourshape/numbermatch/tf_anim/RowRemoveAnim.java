package com.fourshape.numbermatch.tf_anim;

import com.fourshape.numbermatch.listeners.OnRowRemoveAnimationListener;
import com.fourshape.numbermatch.puzzle_core.structure.CellRC;

import java.util.ArrayList;

public class RowRemoveAnim {

    private static final String TAG = "RowRemoveAnim";
    private static final float FACTOR = 0.5f;
    private int totalCellsForScan = 0;
    private float value = 0.1f;
    private final ArrayList<CellRC> cellRCArrayList;
    private OnRowRemoveAnimationListener onRowRemoveAnimationListener;
    private boolean lockAnim;

    public RowRemoveAnim () {
        cellRCArrayList = new ArrayList<>();
        reset();
    }

    public void setCellRCArrayList (ArrayList<CellRC> cellRCArrayList) {
        this.cellRCArrayList.clear();
        this.cellRCArrayList.addAll(cellRCArrayList);
        this.totalCellsForScan = cellRCArrayList.size();
    }

    public ArrayList<CellRC> getCellRCArrayList() {
        return cellRCArrayList;
    }

    public void setOnRowRemoveAnimationListener(OnRowRemoveAnimationListener onRowRemoveAnimationListener) {
        this.onRowRemoveAnimationListener = onRowRemoveAnimationListener;
    }

    public void run () {

        if (lockAnim)
            return;

        value += FACTOR;

        if (value > totalCellsForScan) {
            lockAnim = true;
            if (onRowRemoveAnimationListener != null) {
                reset();
                onRowRemoveAnimationListener.onFinish();
            }
        }

    }

    public boolean isAnimationUnlocked () {
        return !lockAnim;
    }

    public int getValue() {
        return (int) value;
    }

    public void lock () {
        lockAnim = true;
    }

    public void unlock () {
        lockAnim = false;
    }

    public void reset () {
        value = 0.1f;
        lock();
    }

}


