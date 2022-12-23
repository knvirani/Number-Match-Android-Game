package com.fourshape.numbermatch.puzzle_core;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.fourshape.easythingslib.utils.AppThemes;
import com.fourshape.easythingslib.utils.CommonSharedData;
import com.fourshape.easythingslib.utils.DeviceType;
import com.fourshape.numbermatch.app_color.AppColor;
import com.fourshape.numbermatch.listeners.OnBoardStartAnimationListener;
import com.fourshape.numbermatch.listeners.OnCellOpacityAnimationListener;
import com.fourshape.numbermatch.listeners.OnCellScanAnimationListener;
import com.fourshape.numbermatch.listeners.OnRowRemoveAnimationListener;
import com.fourshape.numbermatch.listeners.OnScoreAnimationListener;
import com.fourshape.numbermatch.listeners.OnScoreSizeAnimListener;
import com.fourshape.numbermatch.puzzle_core.structure.Cell;
import com.fourshape.numbermatch.puzzle_core.structure.CellRC;
import com.fourshape.numbermatch.tf_anim.BoardBeginAnim;
import com.fourshape.numbermatch.tf_anim.CellOpacityAnim;
import com.fourshape.numbermatch.tf_anim.CellScanAnim;
import com.fourshape.numbermatch.tf_anim.RowRemoveAnim;
import com.fourshape.numbermatch.tf_anim.ScoreSizeAnim;
import com.fourshape.numbermatch.utils.MakeLog;
import com.fourshape.numbermatch.utils.ScreenParams;
import com.fourshape.numbermatch.utils.SharedData;

import java.util.ArrayList;

public class BoardView extends View {

    private static final String TAG = "BoardView";

    private float width, height;
    private AppColor appColor;
    private boolean shouldDrawBoard = false;
    private Matrix matrix;
    private int xCoord = 0, yCoord = 0;
    private float fontSizeFactor = 64.0f;

    private float origViewDimen;

    private float cellSize;
    private float BOARD_ROW_COL_THICKNESS = 1.5f;
    private float BOARD_OUTER_BORDER_THICKNESS = 5f;
    private int padding = 16;

    private final Paint borderPaint = new Paint();
    private final Paint cellFillPaint = new Paint();
    private final Paint textPaint = new Paint();
    private final Rect textRectBounds = new Rect();

    private Typeface cellTextTypeface;

    private boolean shouldDrawTextOnBoard = false;

    private boolean isForTutorial = false;
    private boolean isForHint = false;

    private float radius, cornerRadius = 0, borderThickness = 0;

    private RowRemoveAnim rowRemoveAnim;
    private BoardBeginAnim boardBeginAnim;
    private CellOpacityAnim cellOpacityAnim;
    private CellScanAnim cellScanAnim;
    private ScoreSizeAnim scoreSizeAnim;
    private boolean shouldInvalidate = true;

    public BoardView(Context context) {
        super(context);
        init();
    }

    public BoardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BoardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setFontSizeFactor(float fontSizeFactor) {
        this.fontSizeFactor = fontSizeFactor;
    }

    public void setForTutorial () {
        isForTutorial = true;
    }

    public void setForHint () {
        isForHint = true;
    }

    public void unsetForHint () {
        isForHint = false;
    }

    public void enableTextDrawingOnBoard () {
        shouldDrawTextOnBoard = true;
    }

    public void disableTextDrawingOnBoard () {
        shouldDrawTextOnBoard = false;
    }

    public void enableBoardStartAnimation () {
        unlockInvalidate();
        boardBeginAnim.unlock();
    }

    public void disableBoardStartAnimation () {
        boardBeginAnim.lock();
    }

    public void enableCellOpacityAnimation () {
        cellOpacityAnim.unlock();
        unlockInvalidate();
        matrix.enableAnimationRangeCheck();
    }

    public void disableCellOpacityAnimation () {
        cellOpacityAnim.lock();
        matrix.cleanAnimationArr();
    }

    public void enableCellScanAnimation (ArrayList<CellRC> cellRCArrayList) {
        cellScanAnim.setCellRCArrayList(cellRCArrayList);
        cellScanAnim.unlock();
        unlockInvalidate();
    }

    public void enableRowRemoveAnimation (ArrayList<CellRC> cellRCArrayList) {
        unlockInvalidate();
        rowRemoveAnim.setCellRCArrayList(cellRCArrayList);
        rowRemoveAnim.unlock();
    }

    public void disableRowRemoveAnimation () {
        rowRemoveAnim.lock();
        lockInvalidate();
    }

    public void setOnBoardStartAnimationListener (OnBoardStartAnimationListener onBoardStartAnimationListener) {
        boardBeginAnim.setOnBoardStartAnimationListener(onBoardStartAnimationListener);
    }

    public void setOnRowRemoveAnimationListener (OnRowRemoveAnimationListener onRowRemoveAnimationListener) {
        rowRemoveAnim.setOnRowRemoveAnimationListener(onRowRemoveAnimationListener);
    }

    private void init () {

        appColor = new AppColor();
        appColor.setTheme(AppThemes.DAY);
        matrix = new Matrix();

        cellTextTypeface = Typeface.createFromAsset(this.getContext().getAssets(), "fonts/roboto_regular_slim.ttf");
        textPaint.setTypeface(cellTextTypeface);

        boardBeginAnim = new BoardBeginAnim();

        scoreSizeAnim = new ScoreSizeAnim();
        scoreSizeAnim.setOnScoreSizeAnimListener(new OnScoreSizeAnimListener() {
            @Override
            public void onEnd() {
                scoreSizeAnim.reset();
                invalidate();
                singleLockInvalidate();
            }
        });

        rowRemoveAnim = new RowRemoveAnim();

        cellOpacityAnim = new CellOpacityAnim();
        cellOpacityAnim.setOnCellOpacityAnimationListener(new OnCellOpacityAnimationListener() {
            @Override
            public void onFinish() {
                cellOpacityAnim.reset();
                invalidate();
                singleLockInvalidate();
                matrix.resetSelectedRC();
            }
        });

        cellScanAnim = new CellScanAnim();
        cellScanAnim.setOnCellScanAnimListener(new OnCellScanAnimationListener() {
            @Override
            public void onFinish() {

                cellScanAnim.reset();
                matrix.increaseBoardRows();

                if (isForTutorial) {
                    ArrayList<CellRC> cellRCArrayList = matrix.getSinglePossibleMatch();
                    if (cellRCArrayList != null) {
                        int row1 = cellRCArrayList.get(0).getRow();
                        int col1 = cellRCArrayList.get(0).getCol();
                        int row2 = cellRCArrayList.get(1).getRow();
                        int col2 = cellRCArrayList.get(1).getCol();
                        matrix.selectRCForTutor(row1+1, col1+1, row2+1, col2+1);
                    }
                } else {
                    LinearLayoutCompat.LayoutParams layoutParams;
                    if (DeviceType.TYPE == DeviceType.MOBILE) {
                        layoutParams = new LinearLayoutCompat.LayoutParams(getViewWidth(), getViewHeight());
                    } else {
                        layoutParams = new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, getViewHeight());
                    }
                    BoardView.this.setLayoutParams(layoutParams);
                }

                invalidate();
                singleLockInvalidate();

            }
        });

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawView(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        float mWidth = MeasureSpec.getSize(widthMeasureSpec);
        float mHeight = MeasureSpec.getSize(heightMeasureSpec);

        float dimension = 0.0f;

        if (!isForTutorial)
            dimension = Math.min(mWidth, mHeight);
        else
            dimension = Math.max(mWidth, mHeight);

        this.origViewDimen = dimension;

        MakeLog.info(TAG, "onMeasure: Dimension: " + dimension);

        this.cellSize = dimension / this.matrix.getItemsPerRow();
        MakeLog.info(TAG, "CellSize: " + this.cellSize);
        this.width = dimension;
        this.height = this.cellSize * this.matrix.getBoard().length;

        MakeLog.info(TAG, "onMeasure: Width: " + this.width + " Height: " + this.height);

        setMeasuredDimension((int)this.width, (int)this.height);

    }

    public void invalidateAnimation () {
        scoreSizeAnim.reset();
        cellOpacityAnim.reset();
        cellScanAnim.reset();
        rowRemoveAnim.reset();
    }

    public void startScoreSizeAnimation (int score) {
        scoreSizeAnim.unlock();
        scoreSizeAnim.setScoreValue(score);
        unlockInvalidate();
    }

    public void singleLockInvalidate () {

        if (scoreSizeAnim != null && cellScanAnim != null && cellOpacityAnim != null && rowRemoveAnim != null && boardBeginAnim != null) {
            if (!scoreSizeAnim.isAnimationUnlocked() && !cellScanAnim.isAnimationUnlocked() && !cellOpacityAnim.isAnimUnlocked() && !rowRemoveAnim.isAnimationUnlocked() && !boardBeginAnim.isAnimUnlocked()) {
                lockInvalidate();
            }
        }

    }

    public void lockInvalidate () {
        shouldInvalidate = false;
        invalidate();
    }

    public void unlockInvalidate () {
        shouldInvalidate = true;
        invalidate();
    }

    private void triggerInvalidate () {
        if (shouldInvalidate)
            invalidate();
    }

    public int getViewWidth () {
        return ScreenParams.getDisplayWidthPixels(getContext()) - padding * 2;
    }

    public int getBoardWidthUsingCellSize () {
        return (int) this.cellSize * 9;
    }

    public int getViewHeight () {

        int width = ScreenParams.getDisplayWidthPixels(getContext());
        int height = ScreenParams.getDisplayHeightPixels(getContext());

        int dimension = Math.min(width, height);

        MakeLog.info(TAG, "Total Row: " + matrix.getBoard().length);

        this.cellSize = (float) dimension / this.matrix.getItemsPerRow();

        return (int)this.cellSize * this.matrix.getBoard().length;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        boolean isValidTouchEvent = false;

        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            isValidTouchEvent = true;

            if (matrix != null) {
                MakeLog.info(TAG, "onTouchEvent Valid Matrix");

                try {
                    matrix.setSelectedRC( (int) Math.ceil(event.getY()/cellSize), (int) Math.ceil(event.getX()/cellSize) );
                } catch (Exception e) {
                    MakeLog.exception(e);
                }

            }

            invalidate();

        }

        return isValidTouchEvent;
    }

    private void drawView (Canvas canvas) {

        if (!shouldDrawBoard)
            return;

        // Draw board background
        drawBoardBackground(canvas);

        // Highlight tutor selected cell
        if (isForTutorial) {
            drawTutorSelectedCell(canvas);
        }

        // Highlight hint selected cell
        if (isForHint) {
            drawTutorSelectedCell(canvas);
        }

        // Highlight Selected Cell
        drawSelectedCell(canvas);

        // draw cell scan animation
        cellScanAnim.run();
        if (cellScanAnim.isAnimationUnlocked()){
            drawCellScanAnimation(canvas);
        }

        // draw row removal animation
        rowRemoveAnim.run();
        if (rowRemoveAnim.isAnimationUnlocked()) {
            drawCellRowRemovalAnimation(canvas);
        }

        // drawBoard
        drawBoard(canvas);

        // draw text
        cellOpacityAnim.run();
        if (shouldDrawTextOnBoard)
            drawCellText(canvas);

        scoreSizeAnim.run();
        if (scoreSizeAnim.isAnimationUnlocked()) {
            drawScoreSizeAnimText(canvas);
        }

        boardBeginAnim.run();

        triggerInvalidate();

    }

    public void drawBoardBackground (Canvas canvas) {

        cellFillPaint.setStyle(Paint.Style.FILL);
        cellFillPaint.setColor(this.getContext().getColor(this.appColor.getInactiveCellFillColor()));

        canvas.drawRect(0,0, getWidth(), getHeight(), cellFillPaint);

    }

    private void drawScoreSizeAnimText (Canvas canvas) {

        String text = "+" + String.valueOf(scoreSizeAnim.getScoreValue());
        textPaint.setColor(getContext().getColor(appColor.getGameScoreTextColor()));
        textPaint.setTextSize(getWidth() * scoreSizeAnim.getValueForSize());
        textPaint.setAlpha(scoreSizeAnim.getValueForOpacity());
        canvas.getClipBounds(textRectBounds);
        int cHeight = getWidth();// r.height();
        int cWidth = getWidth(); // r.width();
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.getTextBounds(text, 0, text.length(), textRectBounds);
        float x = cWidth / 2f - textRectBounds.width() / 2f - textRectBounds.left;
        float y = cHeight / 2f + textRectBounds.height() / 2f - textRectBounds.bottom;
        canvas.drawText(text, x, y, textPaint);

    }

    public void enableBoardDrawing () {
        shouldDrawBoard = true;
        MakeLog.info(TAG, "Board Drawing Enabled.");
        invalidate();
    }

    public void disableBoardDrawing () {
        shouldDrawBoard = false;
        MakeLog.info(TAG, "Board Drawing Disabled.");
    }

    public void setMatrix (Matrix matrix) {
        this.matrix = matrix;
    }

    public void refreshViewsColor () {
        if (appColor != null) {
            appColor.setTheme(new CommonSharedData(getContext(), SharedData.SHARED_PREF_TITLE).getAppCurrentTheme());
        }
    }

    private void drawCellText (Canvas canvas) {

        if (matrix != null) {
            if (matrix.getBoard() != null) {

                int cellScanIndex = 1;

                for (int row=0; row<matrix.getBoard().length; row++) {
                    for (int col=0; col<matrix.getBoard()[row].length; col++) {

                        if (matrix.isValidRC(row, col)) {
                            Cell cell = this.matrix.getBoard()[row][col];
                            if (cell != null) {
                                if (cell.getValue() != 0) {
                                    drawTextPerCell(canvas, cell, row, col);
                                }
                            }

                        }

                    }
                }

            }
        }

    }

    private void drawTextPerCell (Canvas canvas, Cell cell, int row, int col) {

        String text = "";

        if (cell.getValue() != 0) {
            try {
                text = String.valueOf(cell.getValue());
            } catch (Exception e) {
                text = "";
            }
        }

        float fontSizeFactor = -1;
        fontSizeFactor = (float)(this.fontSizeFactor * (int)cellSize);

        textPaint.getTextBounds(text, 0, (int)(text.length()), textRectBounds);
        textPaint.setTextSize(fontSizeFactor);
        textPaint.setAntiAlias(true);

        if (cell.isSolved()) {
            textPaint.setColor(this.getContext().getColor(this.appColor.getSolvedCellTextColor()));
        } else {
            textPaint.setColor(this.getContext().getColor(this.appColor.getUnsolvedCellTextColor()));
        }

        if (cellOpacityAnim.isAnimUnlocked()) {
            if (this.matrix.isInAnimationRange(row, col)) {
                try {
                    textPaint.setAlpha(cellOpacityAnim.getValue());
                } catch (Exception e) { }
            }
        }

        drawCenter(canvas, textRectBounds,  textPaint, row, col, cellSize, text);

    }

    private static void drawCenter(Canvas canvas, Rect r, Paint paint, int row, int col, float cellSize, String text) {
        canvas.getClipBounds(r);
        float cHeight = cellSize;// r.height();
        float cWidth = cellSize; // r.width();
        paint.setTextAlign(Paint.Align.LEFT);
        paint.getTextBounds(text, 0, text.length(), r);
        float x = col*cellSize + cWidth / 2f - r.width() / 2f - r.left;
        float y = row*cellSize + cHeight / 2f + r.height() / 2f - r.bottom;
        canvas.drawText(text, x, y, paint);
    }

    private void drawCellRowRemovalAnimation (Canvas canvas) {

        try {

            int row = rowRemoveAnim.getCellRCArrayList().get(rowRemoveAnim.getValue()).getRow();
            int col = rowRemoveAnim.getCellRCArrayList().get(rowRemoveAnim.getValue()).getCol();

            cellFillPaint.setStyle(Paint.Style.FILL);
            cellFillPaint.setColor(this.getContext().getColor(this.appColor.getCellScanAnimationColor()));

            canvas.drawRect(
                    (col) * cellSize,
                    (row) * cellSize,
                    (col + 1) * cellSize,
                    (row + 1) * cellSize,
                    cellFillPaint);

        } catch (Exception e) {}

    }

    private void drawCellScanAnimation (Canvas canvas) {

        try {

            int row = cellScanAnim.getCellRCArrayList().get((int)cellScanAnim.getValue()).getRow();
            int col = cellScanAnim.getCellRCArrayList().get((int)cellScanAnim.getValue()).getCol();

            Cell cell = matrix.getBoard()[row][col];

            if (cell.getValue() != 0 && !cell.isSolved()) {

                cellFillPaint.setStyle(Paint.Style.FILL);
                cellFillPaint.setColor(this.getContext().getColor(this.appColor.getCellScanAnimationColor()));

                canvas.drawRect(
                        (col) * cellSize,
                        (row) * cellSize,
                        (col + 1) * cellSize,
                        (row + 1) * cellSize,
                        cellFillPaint);

            }

        } catch (Exception e) {}


    }

    private void drawTutorSelectedCell (Canvas canvas) {

        int row = matrix.getSelectedTutorCell1()[0] - 1;
        int col = matrix.getSelectedTutorCell1()[1] - 1;

        if (row >= 0 && col >= 0 && matrix.isValidRC(row, col)) {

            cellFillPaint.setStyle(Paint.Style.FILL);
            cellFillPaint.setColor(this.getContext().getColor(this.appColor.getTutorSelectedCellBackgroundColor()));

            canvas.drawRect(
                    (col) * cellSize,
                    (row) * cellSize,
                    (col + 1) * cellSize,
                    (row + 1) * cellSize,
                    cellFillPaint);

        }

        row = matrix.getSelectedTutorCell2()[0] - 1;
        col = matrix.getSelectedTutorCell2()[1] - 1;

        if (row >= 0 && col >= 0 && matrix.isValidRC(row, col)) {

            cellFillPaint.setStyle(Paint.Style.FILL);
            cellFillPaint.setColor(this.getContext().getColor(this.appColor.getTutorSelectedCellBackgroundColor()));

            canvas.drawRect(
                    (col) * cellSize,
                    (row) * cellSize,
                    (col + 1) * cellSize,
                    (row + 1) * cellSize,
                    cellFillPaint);

        }


    }

    private void drawSelectedCell (Canvas canvas) {

        int row = matrix.getSelectedCell1()[0] - 1;
        int col = matrix.getSelectedCell1()[1] - 1;

        if (row >= 0 && col >= 0 && matrix.isValidRC(row, col)) {

            cellFillPaint.setStyle(Paint.Style.FILL);
            cellFillPaint.setColor(this.getContext().getColor(this.appColor.getSelectedCellFillColor()));

            canvas.drawRect(
                    (col) * cellSize,
                    (row) * cellSize,
                    (col + 1) * cellSize,
                    (row + 1) * cellSize,
                    cellFillPaint);

        }

        row = matrix.getSelectedCell2()[0] - 1;
        col = matrix.getSelectedCell2()[1] - 1;

        if (row >= 0 && col >= 0 && matrix.isValidRC(row, col)) {

            cellFillPaint.setStyle(Paint.Style.FILL);
            cellFillPaint.setColor(this.getContext().getColor(this.appColor.getSelectedCellFillColor()));

            canvas.drawRect(
                    (col) * cellSize,
                    (row) * cellSize,
                    (col + 1) * cellSize,
                    (row + 1) * cellSize,
                    cellFillPaint);

        }

    }

    private void drawBoard (Canvas canvas) {

        if (matrix != null && matrix.getBoard() != null) {

            int rowLimit = matrix.getBoard().length;

            // Draw cols
            for (int col=0; col<=matrix.getItemsPerRow(); col++) {

                borderPaint.setStyle(Paint.Style.STROKE);
                borderPaint.setAntiAlias(true);

                if (col == 0 || col == matrix.getItemsPerRow()) {
                    borderPaint.setStrokeWidth(this.BOARD_OUTER_BORDER_THICKNESS);
                    borderPaint.setColor(this.getContext().getColor(this.appColor.getBoardBorderColor()));
                } else {

                    if (boardBeginAnim.isAnimUnlocked()) {
                        borderPaint.setStrokeWidth(boardBeginAnim.getValue());
                    } else {
                        borderPaint.setStrokeWidth(this.BOARD_ROW_COL_THICKNESS);
                    }

                    borderPaint.setColor(this.getContext().getColor(this.appColor.getRowColBorderColor()));

                }

                canvas.drawLine(cellSize*col, 0, cellSize*col, getHeight(), borderPaint);

            }

            // Draw rows
            for (int row=0; isForTutorial ? row<=rowLimit : row<rowLimit ; row++) {

                borderPaint.setStyle(Paint.Style.STROKE);
                borderPaint.setAntiAlias(true);

                if (row == 0 || row == rowLimit) {
                    borderPaint.setStrokeWidth(this.BOARD_OUTER_BORDER_THICKNESS);
                    borderPaint.setColor(this.getContext().getColor(this.appColor.getBoardBorderColor()));
                    canvas.drawLine(0, cellSize*row, getWidth(), cellSize*row,  borderPaint);
                } else {

                    if (boardBeginAnim.isAnimUnlocked()) {
                        borderPaint.setStrokeWidth(boardBeginAnim.getValue());
                    } else {
                        borderPaint.setStrokeWidth(this.BOARD_ROW_COL_THICKNESS);
                    }

                    borderPaint.setColor(this.getContext().getColor(this.appColor.getRowColBorderColor()));
                    canvas.drawLine(0+BOARD_OUTER_BORDER_THICKNESS/2, cellSize*row, getWidth()-BOARD_OUTER_BORDER_THICKNESS/2, cellSize*row,  borderPaint);
                }


            }



        }

    }

}
