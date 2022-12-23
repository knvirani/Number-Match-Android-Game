package com.fourshape.numbermatch.puzzle_core;

import android.os.AsyncTask;

import com.fourshape.numbermatch.listeners.OnAchievementUnlockListener;
import com.fourshape.numbermatch.listeners.OnCellSelectListener;
import com.fourshape.numbermatch.puzzle_core.structure.Cell;
import com.fourshape.numbermatch.puzzle_core.structure.CellRC;
import com.fourshape.numbermatch.utils.MakeLog;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

public class Matrix {

    private static final String TAG = "Matrix";

    private int gameLevel;
    private Cell[][] cells;
    private int ITEMS_PER_ROW = 9;

    private int selectedRow1, selectedCol1, selectedRow2, selectedCol2;
    private int tutorSelectedRow1, tutorSelectedCol1, tutorSelectedRow2, tutorSelectedCol2;
    private boolean shouldCheckAnimationRange = false;

    private OnCellSelectListener onCellSelectListener;
    private final ArrayList<CellRC> animationCellRCArrayList;

    private boolean shouldSelect = true;

    public Matrix () {
        resetMatrix();
        gameLevel = -1;
        resetSelectedRC();
        animationCellRCArrayList = new ArrayList<>();
        //setTestData();
    }

    public void cleanAnimationArr () {
        shouldCheckAnimationRange = false;
        if (this.animationCellRCArrayList != null) {
            this.animationCellRCArrayList.clear();
        }
    }

    public void enableAnimationRangeCheck () {
        shouldCheckAnimationRange = true;
    }

    public void enableSelection () {
        shouldSelect = true;
    }

    public void disableSelection () {
        shouldSelect = false;
    }

    public void selectRCForTutor (int row1, int col1, int row2, int col2) {
        this.tutorSelectedRow1 = row1;
        this.tutorSelectedCol1 = col1;
        this.tutorSelectedRow2 = row2;
        this.tutorSelectedCol2 = col2;
    }

    public void resetRCForTutor () {
        this.tutorSelectedRow1 = -1;
        this.tutorSelectedCol1 = -1;
        this.tutorSelectedRow2 = -1;
        this.tutorSelectedCol2 = -1;
    }

    public int[] getSelectedTutorCell1 () {
        return new int[] {this.tutorSelectedRow1, this.tutorSelectedCol1};
    }

    public int[] getSelectedTutorCell2 () {
        return new int[] {this.tutorSelectedRow2, this.tutorSelectedCol2};
    }

    public void setItemsPerRow (int totalItemsPerRow) {
        ITEMS_PER_ROW = totalItemsPerRow;
        resetMatrixForTutor();
        resetSelectedRC();
    }

    public void customizeCellsForTutorial (int indexId) {

        if (this.cells != null) {

            if (this.cells[0].length == 5) {

                if (indexId == 2) {
                    this.cells[0][2].setSolved(true);
                    this.cells[0][3].setSolved(true);
                    this.cells[1][1].setSolved(true);
                    this.cells[1][2].setSolved(true);
                    this.cells[1][4].setSolved(true);
                    this.cells[2][0].setSolved(true);
                } else if (indexId == 4) {
                    this.cells[1][0].setSolved(true);
                    this.cells[1][1].setSolved(true);
                    this.cells[1][2].setSolved(true);
                    this.cells[1][3].setSolved(true);
                } else if (indexId == 6) {
                    this.cells[1][1].setSolved(true);
                    this.cells[1][2].setSolved(true);
                    this.cells[1][3].setSolved(true);
                    this.cells[1][4].setSolved(true);
                } else if (indexId == 8) {
                    this.cells[0][0].setSolved(true);
                    this.cells[1][0].setSolved(true);
                    this.cells[2][0].setSolved(true);
                    this.cells[2][1].setSolved(true);
                    this.cells[1][4].setSolved(true);
                }

            }

        }

    }

    public void setOnCellSelectListener (OnCellSelectListener onCellSelectListener) {
        this.onCellSelectListener = onCellSelectListener;
    }

    public int getItemsPerRow () {
        return ITEMS_PER_ROW;
    }

    public void resetMatrix () {

        cells = new Cell[ITEMS_PER_ROW][ITEMS_PER_ROW];

        for (int row=0; row<cells.length; row++) {
            for (int col=0; col<cells[row].length; col++) {
                this.cells[row][col] = new Cell(row, col, 0, false);
            }
        }

    }

    public void resetMatrixForTutor () {
        cells = new Cell[3][ITEMS_PER_ROW];

        for (int row=0; row<cells.length; row++) {
            for (int col=0; col<cells[row].length; col++) {
                this.cells[row][col] = new Cell(row, col, 0, false);
            }
        }
    }

    public void setCellsData (Cell[][] savedCells) {

        this.cells = new Cell[savedCells.length][savedCells[0].length];

        for (int row=0; row<this.cells.length; row++) {
            for (int col=0; col<this.cells[row].length; col++) {

                if (this.cells[row][col] == null) {
                    this.cells[row][col] = new Cell(row, col, savedCells[row][col].getValue(), savedCells[row][col].isSolved());
                } else {

                    Cell savedCell = savedCells[row][col];
                    this.cells[row][col].setRow(savedCell.getRow());
                    this.cells[row][col].setCol(savedCell.getCol());
                    this.cells[row][col].setValue(savedCell.getValue());
                    this.cells[row][col].setSolved(savedCell.isSolved());

                }
            }
        }

    }

    public void setCollection (int[] set) {

        int index = 0;

        if (this.cells != null) {
            for (int row=0; row<this.cells.length; row++) {
                for (int col=0; col<this.cells[row].length; col++) {
                    if (isValidRC(row, col) && index < set.length) {
                        this.cells[row][col].setValue(set[index]);
                        index++;
                    } else {
                        break;
                    }
                }
            }
        }

    }

    private void setTestData () {
        this.cells[0][0].setValue(2);
        this.cells[0][1].setValue(3);
        this.cells[0][2].setValue(3);
        this.cells[0][3].setValue(7);
        this.cells[0][4].setValue(8);
        this.cells[0][5].setValue(9);
        this.cells[0][6].setValue(1);
        this.cells[0][7].setValue(4);
        this.cells[0][8].setValue(5);

        this.cells[1][0].setValue(4);
        this.cells[1][1].setValue(1);
        this.cells[1][2].setValue(1);
        this.cells[1][3].setValue(6);
        this.cells[1][4].setValue(8);
        this.cells[1][5].setValue(1);
        this.cells[1][6].setValue(3);
        this.cells[1][7].setValue(6);
        this.cells[1][8].setValue(7);

        this.cells[2][0].setValue(3);
        this.cells[2][1].setValue(1);
        this.cells[2][2].setValue(2);
        this.cells[2][3].setValue(4);
        this.cells[2][4].setValue(5);
        this.cells[2][5].setValue(9);
        this.cells[2][6].setValue(7);
        this.cells[2][7].setValue(9);
        this.cells[2][8].setValue(3);
    }

    private class SelectRCInBG extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... ints) {

            int row = ints[0], col = ints[1];

            selectRC(row, col);

            return null;
        }
    }

    private void selectRC (int row, int col) {

        if (!shouldSelect)
            return;

        disableSelection();

        if (isValidRC(row-1, col-1)) {
            if (cells[row-1][col-1].getValue() == 0 || cells[row-1][col-1].isSolved())
            {
                enableSelection();
                return;
            }
        } else {
            enableSelection();
            return;
        }

        if (this.selectedRow1 == row && this.selectedCol1 == col)
        {
            this.selectedRow1 = -1;
            this.selectedCol1 = -1;
            enableSelection();
            return;
        }

        if (this.selectedRow1 == -1 && this.selectedCol1 == -1) {
            this.selectedRow1 = row;
            this.selectedCol1 = col;
            MakeLog.info(TAG,  "Selected Cell-1 First Time. R: " + (row-1) + " C: " + (col-1));
            enableSelection();
        } else {

            if (areSelectedCellsCanBeSolved(getUsableRC(this.selectedRow1), getUsableRC(this.selectedCol1), getUsableRC(row), getUsableRC(col))) {

                MakeLog.info(TAG, "Selected cells can be solved.");

                if (areSelectedCellsPlacedVertically(getUsableRC(this.selectedCol1), getUsableRC(col))) {

                    this.selectedRow2 = row;
                    this.selectedCol2 = col;
                    MakeLog.info(TAG, "Selected Cell-2 in vertical match R: " + (row-1) + " C: " + (col-1));

                    if (hasGapInVertical(getUsableRC(this.selectedRow1), getUsableRC(this.selectedRow2))) {
                        MakeLog.info(TAG, "Has gap in vertical.");
                        if (isVerticalMatchPathClear(getUsableRC(this.selectedRow1), getUsableRC(this.selectedCol1), getUsableRC(this.selectedRow2), getUsableRC(this.selectedCol2))) {
                            if (onCellSelectListener != null) {
                                onCellSelectListener.onSelected(true, true, false);
                            }
                        } else {
                            if (onCellSelectListener != null) {
                                onCellSelectListener.onSelected(false, true, true);
                            }
                        }
                    } else {
                        if (onCellSelectListener != null) {
                            onCellSelectListener.onSelected(true, false, false);
                        }
                    }

                } else if (areSelectedCellsPlacedHorizontally(getUsableRC(this.selectedRow1), getUsableRC(row))) {

                    this.selectedRow2 = row;
                    this.selectedCol2 = col;
                    MakeLog.info(TAG, "Selected Cell-2 in horizontal match");

                    if (hasGapInHorizontal(getUsableRC(this.selectedCol1), getUsableRC(this.selectedCol2))) {
                        MakeLog.info(TAG, "Has Gap in Horizontal");
                        if (isHorizontalMatchPathClear(getUsableRC(this.selectedRow1), getUsableRC(this.selectedCol1), getUsableRC(this.selectedRow2), getUsableRC(this.selectedCol2))) {
                            if (onCellSelectListener != null) {
                                onCellSelectListener.onSelected(true, true, false);
                            }
                        } else {
                            if (onCellSelectListener != null) {
                                onCellSelectListener.onSelected(false, true, true);
                            }
                        }
                    } else {
                        if (onCellSelectListener != null) {
                            onCellSelectListener.onSelected(true, false, false);
                        }
                    }

                } else if (areSelectedCellsPlacedDiagonally(getUsableRC(this.selectedRow1), getUsableRC(this.selectedCol1), getUsableRC(row), getUsableRC(col))) {

                    this.selectedRow2 = row;
                    this.selectedCol2 = col;
                    MakeLog.info(TAG, "Selected Cell-2 in diagonal match");

                    if (hasGapInDiagonal(getUsableRC(this.selectedRow1), getUsableRC(this.selectedRow2))) {
                        if (isDiagonalMatchPathClear(getUsableRC(this.selectedRow1), getUsableRC(this.selectedCol1), getUsableRC(this.selectedRow2), getUsableRC(this.selectedCol2))) {
                            if (onCellSelectListener != null) {
                                onCellSelectListener.onSelected(true, true, false);
                            }
                        } else {
                            if (onCellSelectListener != null) {
                                onCellSelectListener.onSelected(false, true, true);
                            }
                        }
                    } else {
                        if (onCellSelectListener != null) {
                            onCellSelectListener.onSelected(true, false, false);
                        }
                    }

                } else if (areSelectedCellsPlacedInLFWay(getUsableRC(this.selectedRow1), getUsableRC(this.selectedCol1), getUsableRC(row), getUsableRC(col))) {

                    this.selectedRow2 = row;
                    this.selectedCol2 = col;
                    MakeLog.info(TAG, "Selected Cell-2 in LF match");
                    MakeLog.info(TAG, "Cell-2 R: " + (row-1) + " C: " + (col-1));

                    if (isLFMatchPathClear(getUsableRC(this.selectedRow1), getUsableRC(this.selectedCol1), getUsableRC(this.selectedRow2), getUsableRC(this.selectedCol2))) {
                        if (onCellSelectListener != null) {
                            onCellSelectListener.onSelected(true, true, false);
                        }
                    } else {
                        if (onCellSelectListener != null) {
                            onCellSelectListener.onSelected(false, true, true);
                        }
                    }

                } else {

                    if (onCellSelectListener != null) {
                        onCellSelectListener.onSelected(false, false, false);
                    }
                    MakeLog.info(TAG, "Selected Cell-1 as Unknown placement of cell found.");

                }

            } else {
                this.selectedRow2 = row;
                this.selectedCol2 = col;
                if (onCellSelectListener != null) {
                    onCellSelectListener.onSelected(false, false, false);
                }
                MakeLog.info(TAG, "Selected Cell-1 as Both can't be solved.");
            }

        }

        enableSelection();

    }

    public void setSelectedRC (int row, int col) {

        new SelectRCInBG().execute(row, col);

    }

    public boolean isInAnimationRange (int row, int col) {

        if (!shouldCheckAnimationRange)
            return false;

        if (isValidRC(row, col) && this.cells[row][col].isSolved())
            return false;

        try {
            if (animationCellRCArrayList != null && animationCellRCArrayList.size() > 0) {
                for (CellRC cellRC : animationCellRCArrayList) {
                    if (cellRC.getRow() == row && cellRC.getCol() == col)
                        return true;
                }
            }
        } catch (Exception e) {
            return false;
        }

        return false;

    }

    private int getUsableRC (int value) {
        return value-1;
    }

    public void solveSelectedCells () {

        int row1 = this.selectedRow1-1, col1 = this.selectedCol1-1;
        int row2 = this.selectedRow2-1, col2 = this.selectedCol2-1;

        if (isValidRC(row1, col1) && isValidRC(row2, col2)) {
            this.cells[row1][col1].setSolved(true);
            this.cells[row2][col2].setSolved(true);
        }

    }

    public void resetSelectedRC () {
        this.selectedRow1 = -1;
        this.selectedCol1 = -1;
        this.selectedRow2 = -1;
        this.selectedCol2 = -1;
    }

    public int[] getSelectedCell1 () {
        return new int[] {this.selectedRow1, this.selectedCol1};
    }

    public int[] getSelectedCell2 () {
        return new int[] {this.selectedRow2, this.selectedCol2};
    }

    public void setGameLevel(int gameLevel) {
        this.gameLevel = gameLevel;
    }

    public Cell[][] getBoard () {
        return cells;
    }

    public ArrayList<CellRC> getRemoveEligibleRowCellsData () {

        final ArrayList<CellRC> cellRCArrayList = new ArrayList<>();

        if (cells != null) {

            for (int row=0; row<cells.length; row++) {

                boolean isEntireRowEmpty = true;

                for (int col=0; col<cells[row].length; col++) {
                    if (isValidRC(row, col) && !this.cells[row][col].isSolved()) {
                        isEntireRowEmpty = false;
                        break;
                    }
                }

                if (isEntireRowEmpty) {
                    MakeLog.info(TAG, "Row - " + row + " empty.");
                    for (int col=0; col<cells[row].length; col++) {
                        if (isValidRC(row, col)) {
                            cellRCArrayList.add(new CellRC(row, col));
                        }
                    }
                }
            }

        }

        return cellRCArrayList;

    }

    public void removeSolvedRow () {

        MakeLog.info(TAG + " Remove Row", "FUN ENTER");

        if (cells != null) {

            ArrayList<Integer> emptyRowIndexArrList = new ArrayList<>();

            for (int row=0; row<cells.length; row++) {

                boolean isEntireRowEmpty = true;

                for (int col=0; col<ITEMS_PER_ROW; col++) {
                    if (isValidRC(row, col) && !this.cells[row][col].isSolved() && this.cells[row][col].getValue() != 0) {
                        isEntireRowEmpty = false;
                        break;
                    }
                }
                if (isEntireRowEmpty) {
                    MakeLog.info(TAG + " Remove Row", "ROW-" + row + " EMPTY");
                    emptyRowIndexArrList.add(row);
                }
            }

            MakeLog.info(TAG, "EmptyRowIndexSize: " + emptyRowIndexArrList.size());

            if (emptyRowIndexArrList.size() > 0) {

                int newRowLimit = this.cells.length - emptyRowIndexArrList.size();

                if (newRowLimit < ITEMS_PER_ROW) {
                    newRowLimit = ITEMS_PER_ROW;
                }

                int newColLimit = ITEMS_PER_ROW;

                int emptyRowIndex = 0;

                Cell[][] cell = new Cell[newRowLimit][newColLimit];

                initCellArr(cell);

                for (int row=0; row<this.cells.length; row++) {

                    if (emptyRowIndex < emptyRowIndexArrList.size()) {
                        if (emptyRowIndexArrList.get(emptyRowIndex) == row) {
                            emptyRowIndex++;
                            continue;
                        }
                    }

                    for (int col=0; col<ITEMS_PER_ROW; col++) {
                        cell[row-emptyRowIndex][col].setValue(this.cells[row][col].getValue());
                        cell[row-emptyRowIndex][col].setRow(row-emptyRowIndex);
                        cell[row-emptyRowIndex][col].setCol(col);
                        cell[row-emptyRowIndex][col].setSolved(this.cells[row][col].isSolved());
                    }
                }

                this.cells = cell;

                MakeLog.info(TAG, "ROW Removed.");

            }

        }

    }

    public ArrayList<CellRC> getRCofNewCellsRequired () {

        ArrayList<CellRC> cellRCArrayList = new ArrayList<>();

        for (int row=0; row<cells.length; row++) {
            for (int col=0; col<cells[row].length; col++) {

                // Check for if current cell is solved.
                if (isValidRC(row, col) && !cells[row][col].isSolved() && cells[row][col].getValue() != 0) {
                    cellRCArrayList.add(new CellRC(row, col));
                }

            }
        }

        return cellRCArrayList;

    }

    public int getTotalNewCellsRequired () {

        int totalNewCellsRequired = 0;

        for (int row=0; row<cells.length; row++) {
            for (int col=0; col<cells[row].length; col++) {

                // Check for if current cell is solved.
                if (isValidRC(row, col) && !cells[row][col].isSolved() && cells[row][col].getValue() != 0) {
                    totalNewCellsRequired++;
                }

            }
        }

        return totalNewCellsRequired;

    }

    public void increaseBoardRows () {

        int totalNewCellsRequired = 0;
        int totalEmptyCells = 0;

        final ArrayList<Integer> unsolvedCellArrList = new ArrayList<>();

        if (cells != null) {

            for (int row=0; row<cells.length; row++) {
                for (int col=0; col<cells[row].length; col++) {

                    // Check for if current cell is solved.
                    if (isValidRC(row, col) && !cells[row][col].isSolved() && cells[row][col].getValue() != 0) {
                        unsolvedCellArrList.add(cells[row][col].getValue());
                        totalNewCellsRequired++;
                    }

                    if (cells[row][col].getValue() == 0) {
                        totalEmptyCells++;
                    }

                }
            }

        }

        if (totalEmptyCells > totalNewCellsRequired) {

            int index=0;

            for (int row=0; row<cells.length; row++) {
                for (int col=0; col<cells[row].length; col++) {

                    if (this.cells[row][col].getValue() == 0) {

                        this.cells[row][col].setSolved(false);
                        if (index < unsolvedCellArrList.size()){
                            this.cells[row][col].setValue(unsolvedCellArrList.get(index));
                            index++;
                        } else {
                            break;
                        }

                    }
                }
            }

        } else if (totalNewCellsRequired > 0) {

            int totalRowsRequired = (int) Math.ceil( (double) (totalNewCellsRequired - totalEmptyCells) / ITEMS_PER_ROW) + 1;

            MakeLog.info(TAG, "NewRow: Total New Cell required: " + totalNewCellsRequired);
            MakeLog.info(TAG, "NewRow: Total Row required: " + totalRowsRequired);

            Cell[][] cells = new Cell[this.cells.length + totalRowsRequired][ITEMS_PER_ROW];
            initCellArr(cells);

            int index = 0;

            for (int row=0; row < cells.length; row++) {
                for (int col = 0; col < cells[row].length; col++) {

                    if (isValidRC(row, col) && this.cells[row][col].getValue() != 0) {
                        Cell tCell = this.cells[row][col];
                        cells[row][col].setCol(tCell.getCol());
                        cells[row][col].setRow(tCell.getRow());
                        cells[row][col].setSolved(tCell.isSolved());
                        cells[row][col].setValue(tCell.getValue());
                    } else {

                        if (index < unsolvedCellArrList.size()) {
                            cells[row][col].setValue(unsolvedCellArrList.get(index));
                            cells[row][col].setSolved(false);
                            cells[row][col].setRow(row);
                            cells[row][col].setCol(col);
                            index++;
                        } else {
                            cells[row][col].setValue(0);
                            cells[row][col].setSolved(false);
                            cells[row][col].setRow(row);
                            cells[row][col].setCol(col);
                        }

                    }

                }
            }

            this.cells = cells;

        }

    }

    private void initCellArr (Cell[][] cells) {

        if (cells != null) {
            for (int row=0; row<cells.length; row++) {
                for (int col=0; col<cells[row].length; col++) {
                    cells[row][col] = new Cell(row, col, 0, false);
                }
            }
        }

    }

    public boolean isValidRC (int row, int col) {
        try {
            Cell cell = this.cells[row][col];
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isWholeMatrixSolved () {

        if (cells != null) {

            for (int row=0; row<cells.length; row++) {
                for (int col=0; col<cells[row].length; col++) {
                    if (isValidRC(row, col) && cells[row][col].getValue() != 0 && !cells[row][col].isSolved()) {
                        return false;
                    }
                }
            }

        }

        return true;

    }

    private int getTotalUnsolvedCells () {

        int totalUnsolvedCells = 0;

        if (cells != null) {

            for (int row=0; row<cells.length; row++) {
                for (int col=0; col<cells[row].length; col++) {
                    if (isValidRC(row, col) && !cells[row][col].isSolved()) {
                        totalUnsolvedCells++;
                    }
                }
            }

        }

        return totalUnsolvedCells;

    }

    public ArrayList<CellRC> getSinglePossibleMatch () {

        ArrayList<CellRC> cellRCArrayList = new ArrayList<>();

        // 1. Check each cell horizontally
        try {
            for (int row=0; row<cells.length; row++) {

                MakeLog.info(TAG, "horizontal check");

                Cell cell1 = null, cell2 = null;
                int col1 = 0, col2 = 0;
                while (col2 < ITEMS_PER_ROW) {

                    col1 = col2;
                    col2 = col1+1;

                    if (isValidRC(row, col1) && isValidRC(row, col2)) {

                        if (this.cells[row][col1].getValue() == 0 || this.cells[row][col2].getValue() == 0)
                            continue;

                        if (!this.cells[row][col1].isSolved()) {
                            cell1 = this.cells[row][col1];
                        }

                        if (!this.cells[row][col2].isSolved()) {
                            cell2 = this.cells[row][col2];
                        }

                        if (cell1 != null && cell2 != null) {
                            if ((cell1.getValue() == cell2.getValue() || (cell1.getValue()+cell2.getValue() == 10)) && cell1.getCol() != cell2.getCol()) {
                                cellRCArrayList.add(new CellRC(cell1.getRow(), cell1.getCol()));
                                cellRCArrayList.add(new CellRC(cell2.getRow(), cell2.getCol()));
                                return cellRCArrayList;
                            }
                        }

                    }

                    if (col2 == 8)
                        break;

                }

            }
        } catch (Exception e) {}

        // 2. Check each cell vertically
        try {
            for (int col=0; col<ITEMS_PER_ROW; col++) {

                MakeLog.info(TAG, "vertical check");

                Cell cell1 = null, cell2 = null;
                int row1 = 0, row2 = 0;

                while (row2 < cells.length) {

                    row1 = row2;
                    row2 = row1+1;

                    if (isValidRC(row1, col) && isValidRC(row2, col)) {

                        if (this.cells[row1][col].getValue() == 0 || this.cells[row2][col].getValue() == 0)
                            continue;

                        if (!this.cells[row1][col].isSolved()) {
                            cell1 = this.cells[row1][col];
                        }

                        if (!this.cells[row2][col].isSolved()) {
                            cell2 = this.cells[row2][col];
                        }

                        if (cell1 != null && cell2 != null) {
                            if ((cell1.getValue() == cell2.getValue() || (cell1.getValue()+cell2.getValue() == 10)) && cell1.getRow() != cell2.getRow()) {
                                cellRCArrayList.add(new CellRC(cell1.getRow(), cell1.getCol()));
                                cellRCArrayList.add(new CellRC(cell2.getRow(), cell2.getCol()));
                                return cellRCArrayList;
                            }
                        }

                    }

                    if (row2 == cells.length-1)
                        break;

                }

            }
        } catch (Exception e) {}

        // 3. Check each cell diagonally - Top,Start to Bottom,End
        // 3.1 Check from top (use col)
        try {
            for (int col=0; col<ITEMS_PER_ROW; col++) {
                MakeLog.info(TAG, "Top to bottom-end diagonal check");

                Cell cell1 = null, cell2 = null;
                int row1 = 0, row2 = 0, col1 = 0, col2 = 0;

                for (int row=0; row<ITEMS_PER_ROW; row++) {

                    int newCol = col+row;
                    int newRow = row;

                    row1 = newRow;
                    col1 = newCol;
                    row2 = row1+1;
                    col2 = col1+1;

                    if (!isValidRC(row1, col1) || !isValidRC(row2, col2)) {
                        continue;
                    }

                    if (this.cells[row1][col1].getValue() == 0 || this.cells[row2][col2].getValue() == 0)
                        continue;

                    if (!this.cells[row1][col1].isSolved()) {
                        cell1 = this.cells[row1][col1];
                    }

                    if (!this.cells[row2][col2].isSolved()) {
                        cell2 = this.cells[row2][col2];
                    }

                    if (cell1 != null && cell2 != null) {
                        if ((cell1.getValue() == cell2.getValue() || (cell1.getValue()+cell2.getValue() == 10)) && cell1.getRow() != cell2.getRow()) {
                            cellRCArrayList.add(new CellRC(cell1.getRow(), cell1.getCol()));
                            cellRCArrayList.add(new CellRC(cell2.getRow(), cell2.getCol()));
                            return cellRCArrayList;
                        }
                    }

                }
            }
        } catch (Exception e) {}

        // 3.2 Check Start-top-to-end-bottom
        try {
            for (int row=1; row<this.cells.length; row++) {
                MakeLog.info(TAG, "Start-top to end-bottom diagonal check");

                Cell cell1 = null, cell2 = null;
                int row1 = 0, row2 = 0, col1 = 0, col2 = 0;

                for (int col=0; col<ITEMS_PER_ROW; col++) {

                    int newRow = row;
                    int newCol = (row < ITEMS_PER_ROW) ? row : row%ITEMS_PER_ROW + col;

                    row1 = newRow;
                    col1 = newCol;
                    row2 = row1+1;
                    col2 = col1+1;

                    if (!isValidRC(row1, col1) || !isValidRC(row2, col2)) {
                        continue;
                    }

                    if (this.cells[row1][col1].getValue() == 0 || this.cells[row2][col2].getValue() == 0)
                        continue;

                    if (!this.cells[row1][col1].isSolved()) {
                        cell1 = this.cells[row1][col1];
                    }

                    if (!this.cells[row2][col2].isSolved()) {
                        cell2 = this.cells[row2][col2];
                    }

                    if (cell1 != null && cell2 != null) {
                        if ((cell1.getValue() == cell2.getValue() || (cell1.getValue()+cell2.getValue() == 10)) && cell1.getRow() != cell2.getRow()) {
                            cellRCArrayList.add(new CellRC(cell1.getRow(), cell1.getCol()));
                            cellRCArrayList.add(new CellRC(cell2.getRow(), cell2.getCol()));
                            return cellRCArrayList;
                        }
                    }

                }

            }
        } catch (Exception e) {}

        // 3.3 Check from right-top (use col)
        try {
            for (int col=ITEMS_PER_ROW-1; col>=0; col--) {
                MakeLog.info(TAG, "Top to bottom-start diagonal check");

                Cell cell1 = null, cell2 = null;
                int row1 = 0, row2 = 0, col1 = 0, col2 = 0;

                for (int row=0; row<ITEMS_PER_ROW; row++) {

                    int newCol = col-row;
                    int newRow = row;

                    row1 = newRow;
                    col1 = newCol;
                    row2 = row1+1;
                    col2 = col1-1;

                    if (!isValidRC(row1, col1) || !isValidRC(row2, col2)) {
                        continue;
                    }

                    if (this.cells[row1][col1].getValue() == 0 || this.cells[row2][col2].getValue() == 0)
                        continue;

                    if (!this.cells[row1][col1].isSolved()) {
                        cell1 = this.cells[row1][col1];
                    }

                    if (!this.cells[row2][col2].isSolved()) {
                        cell2 = this.cells[row2][col2];
                    }

                    if (cell1 != null && cell2 != null) {
                        if ((cell1.getValue() == cell2.getValue() || (cell1.getValue()+cell2.getValue() == 10)) && cell1.getRow() != cell2.getRow()) {
                            cellRCArrayList.add(new CellRC(cell1.getRow(), cell1.getCol()));
                            cellRCArrayList.add(new CellRC(cell2.getRow(), cell2.getCol()));
                            return cellRCArrayList;
                        }
                    }

                }
            }
        } catch (Exception e) {}

        // 3.2 Check end-top-to-start-bottom
        try {
            for (int row=1; row<this.cells.length; row++) {
                MakeLog.info(TAG, "end-top to start-bottom diagonal check");

                Cell cell1 = null, cell2 = null;
                int row1 = 0, row2 = 0, col1 = 0, col2 = 0;

                for (int col=ITEMS_PER_ROW-1; col>=0; col--) {

                    int newRow = row;
                    int newCol = (row < ITEMS_PER_ROW) ? row : row%ITEMS_PER_ROW + col;

                    row1 = newRow;
                    col1 = newCol;
                    row2 = row1+1;
                    col2 = col1-1;

                    if (!isValidRC(row1, col1) || !isValidRC(row2, col2)) {
                        continue;
                    }

                    if (this.cells[row1][col1].getValue() == 0 || this.cells[row2][col2].getValue() == 0)
                        continue;

                    if (!this.cells[row1][col1].isSolved()) {
                        cell1 = this.cells[row1][col1];
                    }

                    if (!this.cells[row2][col2].isSolved()) {
                        cell2 = this.cells[row2][col2];
                    }

                    if (cell1 != null && cell2 != null) {
                        if ((cell1.getValue() == cell2.getValue() || (cell1.getValue()+cell2.getValue() == 10)) && cell1.getRow() != cell2.getRow()) {
                            cellRCArrayList.add(new CellRC(cell1.getRow(), cell1.getCol()));
                            cellRCArrayList.add(new CellRC(cell2.getRow(), cell2.getCol()));
                            return cellRCArrayList;
                        }
                    }

                }

            }
        } catch (Exception e) {}

        // 4. Check LF
        try {
            for (int row=0; row<cells.length; row++) {
                MakeLog.info(TAG, "Checking LF points");

                Cell cell1 = null, cell2 = null;

                for (int col=0; col<ITEMS_PER_ROW; col++) {

                    if (!isValidRC(row,col))
                        break;

                    if (this.cells[row][col].getValue() != 0 && !this.cells[row][col].isSolved())
                        cell1 = this.cells[row][col];

                }

                for (int col=0; col<ITEMS_PER_ROW; col++) {

                    if (!isValidRC(row+1,col))
                        break;

                    if (this.cells[row+1][col].getValue() != 0 && !this.cells[row+1][col].isSolved()) {
                        cell2 = this.cells[row+1][col];
                        break;
                    }

                }

                if (cell1 != null && cell2 != null) {
                    if ((cell1.getValue() == cell2.getValue() || (cell1.getValue()+cell2.getValue() == 10)) && cell1.getCol() != cell2.getCol()) {
                        cellRCArrayList.add(new CellRC(cell1.getRow(), cell1.getCol()));
                        cellRCArrayList.add(new CellRC(cell2.getRow(), cell2.getCol()));
                        return cellRCArrayList;
                    }
                }


            }
        } catch (Exception e) {}

        MakeLog.info(TAG, "NULL from Last");

        return null;

    }

    private CellRC isMatchedAnywhereInMatrix (Cell cell) {

        if (this.cells != null) {

            boolean hCheck = false, vCheck = false, dCheck = false, LFCheck = false;

            for (int row=0; row<this.cells.length; row++) {
                for (int col=0; col<this.cells[row].length; col++) {

                    if (!isValidRC(row, col))
                        return null;

                    Cell cCell = this.cells[row][col];

                    if (cCell.getValue() == 0)
                        return null;

                    if (cCell.isSolved())
                        continue;

                    if (row != cell.getRow() && col != cell.getCol()) {

                        if (areSelectedCellsCanBeSolved(cell.getRow(), cell.getCol(), cCell.getRow(), cCell.getCol())) {

                            if (areSelectedCellsPlacedHorizontally(cell.getRow(), cCell.getRow())) {
                                hCheck = true;
                                if (isHorizontalMatchPathClear(cell.getRow(), cell.getCol(), cCell.getRow(), cCell.getCol())) {
                                    return new CellRC(row, col);
                                }
                            } else if (areSelectedCellsPlacedVertically(cell.getCol(), cCell.getCol())) {
                                vCheck = true;
                                if (isVerticalMatchPathClear(cell.getRow(), cell.getCol(), cCell.getRow(), cCell.getCol())) {
                                    return new CellRC(row, col);
                                }
                            } else if (areSelectedCellsPlacedDiagonally(cell.getRow(), cell.getCol(), cCell.getRow(), cCell.getCol())) {
                                dCheck = true;
                                if (isDiagonalMatchPathClear(cell.getRow(), cell.getCol(), cCell.getRow(), cCell.getCol())) {
                                    return new CellRC(row, col);
                                }
                            } else if (areSelectedCellsPlacedInLFWay(cell.getRow(), cell.getCol(), cCell.getRow(), cCell.getCol())) {
                                LFCheck = true;
                                if (isLFMatchPathClear(cell.getRow(), cell.getCol(), cCell.getRow(), cCell.getCol())) {
                                    return new CellRC(row, col);
                                }
                            }

                        } else {

                            if (areSelectedCellsPlacedHorizontally(cell.getRow(), cCell.getRow())) {
                                hCheck = true;
                            } else if (areSelectedCellsPlacedVertically(cell.getCol(), cCell.getCol())) {
                                vCheck = true;
                            } else if (areSelectedCellsPlacedDiagonally(cell.getRow(), cell.getCol(), cCell.getRow(), cCell.getCol())) {
                                dCheck = true;
                            } else if (areSelectedCellsPlacedInLFWay(cell.getRow(), cell.getCol(), cCell.getRow(), cCell.getCol())) {
                                LFCheck = true;
                            }

                        }

                    }

                    if (hCheck && vCheck && dCheck && LFCheck)
                        return null;

                }
            }

        }

        return null;

    }

    private boolean isCellMatch (Cell startCell, Cell destCell) {

        return startCell != null && destCell != null && !startCell.isSolved() && !destCell.isSolved() && startCell.getValue() != 0 && destCell.getValue() != 0 && (startCell.getValue() == destCell.getValue() || startCell.getValue()+destCell.getValue() == 10);

    }

    private boolean areSelectedCellsPlacedVertically (int col1, int col2) {
        return col1 == col2;
    }

    private boolean areSelectedCellsPlacedHorizontally (int row1, int row2) {
        return row1 == row2;
    }

    private boolean areSelectedCellsPlacedDiagonally (int row1, int col1, int row2, int col2) {
        return getPositive(getPositive(row1)-getPositive(row2)) == getPositive(getPositive(col1)-getPositive(col2));
    }

    private boolean areSelectedCellsPlacedInLFWay (int row1, int col1, int row2, int col2) {
        return row1 != row2 && col1 != col2;
    }

    private boolean areSelectedCellsCanBeSolved (Cell cell1, Cell cell2) {

        try {
            return cell1.getValue() == cell2.getValue() || (cell1.getValue()+cell2.getValue() == 10);
        } catch (Exception e) {
            return false;
        }

    }

    private boolean areSelectedCellsCanBeSolved (int row1, int col1, int row2, int col2) {
        try {

            Cell cell1 = this.cells[row1][col1], cell2 = this.cells[row2][col2];
            return !cell1.isSolved() && cell1.getValue() != 0 && !cell2.isSolved() && cell2.getValue() != 0 && (cell1.getValue() == cell2.getValue() || cell1.getValue()+cell2.getValue() == 10);

        } catch (Exception e) {
            return false;
        }
    }

    private boolean hasGapInHorizontal (int col1, int col2) {
        return getPositive(col1-col2) > 1;
    }

    private boolean hasGapInVertical (int row1, int row2) {
        return getPositive(row1-row2) > 1;
    }

    private boolean hasGapInDiagonal (int row1, int row2) {
        return getPositive(getPositive(row1-row2)) > 1;
    }

    private boolean isLFMatchPathClear (int startRow, int startCol, int destRow, int destCol) {

        /*isLFMatchPathClear
        startCell = target cell
        destCell = cell that found in match

        The goal of this function is to check for the void path between start and target cell.

         */

        boolean isClear = true;

        if (startRow < destRow) {

            /*
            # # # # 1
            # 2 # # #
             */

            for (int row=startRow; row<=destRow; row++) {
                for (int col=0; col<cells[row].length; col++) {

                    boolean shouldCheck = false;

                    if (row == startRow && col > startCol) {
                        shouldCheck = true;
                    } else if (row == destRow && col < destCol) {
                        shouldCheck = true;
                    } else if (row > startRow && row < destRow) {
                        shouldCheck = true;
                    }

                    if (isValidRC(row, col)) {
                        if (shouldCheck && !cells[row][col].isSolved() && cells[row][col].getValue() != 0) {
                            animationCellRCArrayList.add(new CellRC(row, col));
                            isClear = false;
                        }
                    }

                }
            }

        } else {

            /*
            # # 2 # #
            # 1 # # #
             */

            for (int row=destRow; row<=startRow; row++) {
                for (int col=0; col<cells[row].length; col++) {

                    boolean shouldCheck = false;

                    if (row == destRow && col > destCol) {
                        shouldCheck = true;
                    } else if (row == startRow && col < startCol) {
                        shouldCheck = true;
                    } else if (row > destRow && row < startRow) {
                        shouldCheck = true;
                    }

                    if (isValidRC(row, col)) {
                        if (shouldCheck && !cells[row][col].isSolved() && cells[row][col].getValue() != 0) {
                            isClear = false;
                            animationCellRCArrayList.add(new CellRC(row, col));
                        }
                    }

                }
            }

        }

        return isClear;

    }

    private boolean isVerticalMatchPathClear (int startRow, int startCol, int destRow, int destCol) {

        /*
        startCell = target cell
        destCell = cell that found in match

        The goal of this function is to check for the void path between start and target cell.

         */

        boolean isClear = true;

        if (startCol == destCol) {

            MakeLog.info(TAG, "Same Col in verticaL match");

            if (startRow < destRow) {
                for (int row=startRow+1; row<destRow; row++) {
                    if (isValidRC(row, startCol) && !this.cells[row][startCol].isSolved() && this.cells[row][startCol].getValue() != 0) {
                        isClear = false;
                        animationCellRCArrayList.add(new CellRC(row, startCol));
                    }
                }
            } else {
                for (int row=startRow-1; row>destRow; row--) {
                    if (isValidRC(row, startCol) && !this.cells[row][startCol].isSolved() && this.cells[row][startCol].getValue() != 0) {
                        isClear = false;
                        animationCellRCArrayList.add(new CellRC(row, startCol));
                    }
                }
            }

        }

        return isClear;

    }

    private boolean isHorizontalMatchPathClear (int startRow, int startCol, int destRow, int destCol) {

        /*
        startCell = target cell
        destCell = cell that found in match

        The goal of this function is to check for the void path between start and target cell.

         */

        boolean isClear = true;

        if (startRow == destRow) {

            if (startCol < destCol) {

                for (int col=startCol+1; col<destCol; col++) {
                    if (isValidRC(startRow, col) && !this.cells[startRow][col].isSolved() && this.cells[startRow][col].getValue() != 0) {
                        isClear = false;
                        animationCellRCArrayList.add(new CellRC(startRow,  col));
                    }
                }

            } else {

                for (int col=startCol-1; col>destCol; col--) {
                    if (isValidRC(startRow, col) && !this.cells[startRow][col].isSolved() && this.cells[startRow][col].getValue() != 0) {
                        isClear = false;
                        animationCellRCArrayList.add(new CellRC(startRow,  col));
                    }
                }

            }

        }

        return isClear;

    }

    private boolean isDiagonalMatchPathClear (int startRow, int startCol, int destRow, int destCol) {

        /*
        startCell = target cell
        destCell = cell that found in match

        The goal of this function is to check for the void path between start and target cell.

         */

        boolean isClear = true;

        if (startRow < destRow) {

            if (startCol < destCol) {

                int index = 1, row = startRow, col = startCol;

                while (startRow+index < destRow) {

                    row = startRow + index;
                    col = startCol + index;

                    if (this.isValidRC(row, col) && this.cells[row][col].getValue() != 0 && !this.cells[row][col].isSolved()) {
                        isClear = false;
                        animationCellRCArrayList.add(new CellRC(row, col));
                    }

                    index++;

                }

                    /*
                for (int row=startRow+1; row<destRow; row++) {
                    for (int col=startCol+1; col<destCol; col++) {

                        int rowDiff = getPositive(getPositive(row)-getPositive(destRow));
                        int colDiff = getPositive(getPositive(col)-getPositive(destCol));

                        if (rowDiff == colDiff) {
                            if (isValidRC(row, col) && !this.cells[row][col].isSolved() && this.cells[row][col].getValue() != 0)
                                return false;
                        }

                    }
                }

                     */

            } else {

                int index = 1, row = startRow, col = startCol;

                while (startRow+index < destRow) {

                    row = startRow + index;
                    col = startCol - index;

                    if (this.isValidRC(row, col) && this.cells[row][col].getValue() != 0 && !this.cells[row][col].isSolved()) {
                        isClear = false;
                        animationCellRCArrayList.add(new CellRC(row, col));
                    }

                    index++;

                }

                /*
                for (int row=startRow+1; row<destRow; row++) {
                    for (int col=destCol-1; col<startCol; col--) {

                        int rowDiff = getPositive(getPositive(row)-getPositive(destRow));
                        int colDiff = getPositive(getPositive(col)-getPositive(destCol));

                        if (rowDiff == colDiff) {
                            if (isValidRC(row, col) && !this.cells[row][col].isSolved() && this.cells[row][col].getValue() != 0)
                                return false;
                        }

                    }
                }

                 */

            }

        } else {

            if (startCol < destCol) {

                int index = 1, row = startRow, col = startCol;

                while (startRow-index > destRow) {

                    row = startRow - index;
                    col = startCol + index;

                    if (this.isValidRC(row, col) && this.cells[row][col].getValue() != 0 && !this.cells[row][col].isSolved())
                    {
                        isClear = false;
                        animationCellRCArrayList.add(new CellRC(row, col));
                    }

                    index++;

                }


                /*
                for (int row=destRow-1; row>startRow; row--) {
                    for (int col=startCol+1; col<destCol; col++) {

                        int rowDiff = getPositive(getPositive(row)-getPositive(destRow));
                        int colDiff = getPositive(getPositive(col)-getPositive(destCol));

                        if (rowDiff == colDiff) {
                            if (isValidRC(row, col) && !this.cells[row][col].isSolved() && this.cells[row][col].getValue() != 0)
                                return false;
                        }

                    }
                }

                 */

            } else {

                int index = 1, row = startRow, col = startCol;

                while (startRow-index > destRow) {

                    row = startRow - index;
                    col = startCol - index;

                    if (this.isValidRC(row, col) && this.cells[row][col].getValue() != 0 && !this.cells[row][col].isSolved())
                    {
                        isClear = false;
                        animationCellRCArrayList.add(new CellRC(row, col));
                    }

                    index++;

                }

                /*
                for (int row=destRow-1; row>startRow; row--) {
                    for (int col=destCol-1; col<startCol; col--) {

                        int rowDiff = getPositive(getPositive(row)-getPositive(destRow));
                        int colDiff = getPositive(getPositive(col)-getPositive(destCol));

                        if (rowDiff == colDiff) {
                            if (isValidRC(row, col) && !this.cells[row][col].isSolved() && this.cells[row][col].getValue() != 0)
                                return false;
                        }

                    }
                }

                 */

            }

        }

        return isClear;

    }

    private int getPositive (int value) {
        if (value < 0)
            return value*-1;
        else
            return value;
    }

}
