package com.fourshape.numbermatch.tf_anim;

import com.fourshape.numbermatch.listeners.OnCellScanAnimationListener;
import com.fourshape.numbermatch.puzzle_core.structure.CellRC;

import java.util.ArrayList;

public class CellScanAnim {

    private static final String TAG = "CellScanAnim";
    private static final float FACTOR = 0.75f;
    private float value;
    private final ArrayList<CellRC> cellRCArrayList;
    private boolean lockAnim;
    private OnCellScanAnimationListener onCellScanAnimationListener;

    public CellScanAnim () {
        cellRCArrayList = new ArrayList<>();
        reset();
    }

    public void setOnCellScanAnimListener(OnCellScanAnimationListener onCellScanAnimationListener) {
        this.onCellScanAnimationListener = onCellScanAnimationListener;
    }

    public ArrayList<CellRC> getCellRCArrayList() {
        return cellRCArrayList;
    }

    public void setCellRCArrayList (ArrayList<CellRC> cellRCArrayList) {
        this.cellRCArrayList.clear();
        this.cellRCArrayList.addAll(cellRCArrayList);
    }

    public boolean isAnimationUnlocked () {
        return !lockAnim;
    }

    public void run () {

        if (lockAnim)
            return;

        value += FACTOR;

        if (value > cellRCArrayList.size() && onCellScanAnimationListener != null) {
            lock();
            onCellScanAnimationListener.onFinish();
        }

    }

    public int getValue() {
        return (int)value;
    }

    public void reset () {
        value = 0.1f;
        lockAnim = true;
    }

    public void unlock () {
        lockAnim = false;
    }

    public void lock () {
        lockAnim = true;
    }

}
