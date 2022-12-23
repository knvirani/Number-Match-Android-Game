package com.fourshape.numbermatch.listeners;

public interface OnCellSelectListener {
    void onSelected (boolean isSolved, boolean hasGapBetweenSelectedCells, boolean shouldAnimateOnNoSolution);
}
