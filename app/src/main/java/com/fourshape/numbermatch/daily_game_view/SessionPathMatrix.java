package com.fourshape.numbermatch.daily_game_view;

import com.fourshape.easythingslib.utils.MakeLog;
import com.fourshape.numbermatch.daily_game_view.structure.Hexagon;
import com.fourshape.numbermatch.daily_game_view.structure.Property;
import com.fourshape.numbermatch.daily_game_view.structure.SessionCell;
import com.fourshape.numbermatch.daily_game_view.structure.SessionCellStatus;
import com.fourshape.numbermatch.puzzle_core.GameLevel;

public class SessionPathMatrix {

    private static final String TAG = "SessionPathMatrix";

    private final int MAX = 9;
    private final int MAX_COL = 18;
    private int selectedCol;
    private int selectedRow;

    private static final int END_NUMBER = 61;
    private static final int BEGIN_NUMBER = 1;

    private SessionCell[][] board;
    private int[] sessionRankArr;

    public SessionPathMatrix () {
        board = new SessionCell[MAX][MAX_COL];
        selectedCol = -1;
        selectedRow = -1;
        reset();
        setHexagonShape();
    }

    public void reset () {

        int[] totalFillPerRow = {5, 6, 7, 8, MAX, 8, 7, 6, 5};

        // Init -1 to all cells.

        int rank = 1;

        for (int row=0; row<MAX; row++) {

            int totalFill = totalFillPerRow[row];
            final int startsFrom = MAX - totalFill;
            int currentFill = 0;

            for (int col=0; col<MAX_COL; col++) {

                if (col >= startsFrom) {
                    if (startsFrom % 2 == 0) {
                        if (col % 2 == 0 && currentFill < totalFill) {

                            this.board[row][col] = new SessionCell(rank, -1, -1, row, Property.RESERVED, col);

                            currentFill++;
                            rank++;

                        } else {
                            this.board[row][col] = new SessionCell(rank, -1, -1, row, Property.USELESS, col);
                        }
                    } else {
                        if (col % 2 != 0 && currentFill < totalFill) {

                            this.board[row][col] = new SessionCell(rank, -1, -1, row, Property.RESERVED, col);
                            currentFill++;
                            rank++;

                        } else {
                            this.board[row][col] = new SessionCell(rank, -1, -1, row, Property.USELESS, col);
                        }
                    }
                } else {
                    this.board[row][col] = new SessionCell(rank, -1, -1, row, Property.USELESS, col);
                }

            }

        }

    }

    public int getBeginNumber() {
        return BEGIN_NUMBER;
    }

    public int getEndNumber() {
        return END_NUMBER;
    }

    public int getSelectedRow() {
        return selectedRow;
    }

    public int getSelectedCol() {
        return selectedCol;
    }

    public void selectRC (int row, int col) {
        this.selectedRow = row;
        this.selectedCol = col;
    }

    public SessionCell[][] getBoard () {
        return board;
    }

    public boolean isValidRC (int row, int col) {
        try {
            SessionCell cell = this.board[row][col];
            return true;
        } catch (Exception e) {
            MakeLog.error(TAG, "Invalid Row Column");
            return false;
        }
    }

    public void setCellStatus (int row, int col, int status) {

        if (!isValidRC(row, col))
            return;

        this.board[row][col].setStatus(status);

        if (status != SessionCellStatus.UNTOUCHED) {
            this.board[row][col].setConnectorTouched(true);
        }

    }



    public void setLevelToShape (int row, int col, int gameLevel) {

        this.board[row][col].setLevel(gameLevel);

    }

    public SessionCell getCellFromCorrectValue (int cValue) {

        for (int row=0; row<MAX; row++) {
            for (int col=0; col<MAX_COL; col++) {

                if (this.board[row][col].getValue() == cValue) {
                    return this.board[row][col];
                }

            }
        }

        return null;

    }

    public SessionCell getPreviousCellForConnectorLine (int matchCorrectValue) {

        for (int row=0; row<MAX; row++) {
            for (int col=0; col<MAX_COL; col++) {

                SessionCell cell = this.board[row][col];

                if (cell.getProperty() == Property.RESERVED) {
                    if (cell.getValue() == (matchCorrectValue-1)) {
                        return cell;
                    }
                }

            }
        }

        return null;

    }

    public int getMAXRow() {
        return MAX;
    }

    public int getMAX_COL () {
        return MAX_COL;
    }

    private void setHexagonShape () {

        Hexagon hexagon = new Hexagon();
        int[] shape = hexagon.getShape(0);
        int index = 0;

        /*
        First, set values of 1 to 61 to each cell.  After that, these values will be helpful for identifying the correct paths.
         */
        for (int row=0; row<MAX; row++) {
            for (int col=0; col<MAX_COL; col++) {

                if (this.board[row][col].getProperty() == Property.RESERVED) {

                    this.board[row][col].setValue(shape[index]);
                    index++;

                }

            }
        }


        int[] sessionRanks = new int[] {
                1,18,5,8,10,12,15,39,24,21,30,32,35,37,44,42,59,56,53,51,48
        };

        this.sessionRankArr = sessionRanks;

        /*
        Set the session rank to the pre-decided cells.
         */

        index = 1;

        for (int rank : sessionRanks) {
            for (int row=0; row<MAX; row++) {
                for (int col=0; col<MAX_COL; col++) {
                    if (this.board[row][col].getValue() == rank) {

                        this.board[row][col].setSessionRank(index);

                        MakeLog.info(TAG, "Index: " + index + " Value: " + this.board[row][col].getValue());

                        // following will set the start point.
                        if (index == 1) {
                            int status = this.board[row][col].getStatus();
                            if (status == SessionCellStatus.UNTOUCHED || status <= 0) {
                                this.board[row][col].setLevel(GameLevel.START_POINT);
                                this.board[row][col].setStatus(SessionCellStatus.GOING_ON);
                            }
                        }

                        index++;
                        break;

                    }
                }
            }
        }

    }

    public SessionCell getCellFromSessionRank (int sessionRank) {

        for (int row=0; row<MAX; row++) {
            for (int col=0; col<MAX_COL; col++) {

                SessionCell cell = this.board[row][col];

                if (cell.getProperty() == Property.RESERVED) {
                    if (cell.getSessionRank() == sessionRank) {
                        return cell;
                    }
                }

            }
        }

        return null;

    }

    public int[] getSessionRankArr() {
        return sessionRankArr;
    }
}
