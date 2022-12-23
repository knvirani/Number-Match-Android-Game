package com.fourshape.numbermatch.daily_game_view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.fourshape.numbermatch.daily_game_view.structure.ConnectorLineSet;
import com.fourshape.numbermatch.daily_game_view.structure.HexagonSet;
import com.fourshape.easythingslib.utils.CommonSharedData;
import com.fourshape.easythingslib.utils.MakeLog;
import com.fourshape.numbermatch.app_color.AppColor;
import com.fourshape.numbermatch.daily_game_view.structure.Property;
import com.fourshape.numbermatch.daily_game_view.structure.SessionCell;
import com.fourshape.numbermatch.daily_game_view.structure.SessionCellStatus;
import com.fourshape.numbermatch.listeners.OnDailyGameCellTapListener;
import com.fourshape.numbermatch.puzzle_core.GameLevel;
import com.fourshape.numbermatch.ui_popups.PopupBottom;
import com.fourshape.numbermatch.utils.RandNumber;
import com.fourshape.numbermatch.utils.SharedData;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class SessionPathView extends View {

    private static final String TAG = "SessionPathView";

    private float radius, width, height, cornerRadius, borderThickness, triangularHeight, horizontalSpace;
    private Paint borderPaint, startPointBorderPaint, shapePaint, letterPaint, connectorLinePaint;
    private Rect letterPaintBounds;
    private Typeface letterTypeface;
    private AppColor appColor;
    private float xCoord, yCoord;
    private SessionPathMatrix matrix;
    private boolean shouldDraw = false, isAnimationMode = false;
    private boolean isCellTapAllowed = true;

    private final int ANIMATION_MILLISECONDS = 50;
    private int animationCounter = 0;
    private final int animationSlab = 790;

    private OnDailyGameCellTapListener onDailyGameCellTapListener;
    private boolean isXYSet = false;

    private final float startPointCellAnimSlab = 0.5f;
    private final float startPointCellAnimMax = 40f;
    private final float startPointCellAnimMin = 13f;
    private boolean shouldIncreaseStartPoint = true;
    private float startPointAnimCounter = 13f;
    private boolean shouldAnimSessionStartPoint = true;

    private ArrayList<HexagonSet> hexagonSetArrayList;
    private ArrayList<ConnectorLineSet> connectorLineSetArrayList;

    public SessionPathView(Context context) {
        super(context);
        init();
    }

    public SessionPathView (Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public SessionPathView (Context context, AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
        init();
    }

    private void init () {

        hexagonSetArrayList = new ArrayList<>();
        connectorLineSetArrayList = new ArrayList<>();

        borderPaint = new Paint();
        startPointBorderPaint = new Paint();
        shapePaint = new Paint();
        letterPaint = new Paint();
        connectorLinePaint = new Paint();
        letterPaintBounds = new Rect();

        radius = 25;
        cornerRadius = 6.5f;
        borderThickness = 13f;

        matrix = new SessionPathMatrix();

        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(borderThickness);
        borderPaint.setAntiAlias(true);

        startPointBorderPaint.setStyle(Paint.Style.STROKE);
        startPointBorderPaint.setStrokeWidth(borderThickness);
        startPointBorderPaint.setAntiAlias(true);

        shapePaint.setAntiAlias(true);
        shapePaint.setStyle(Paint.Style.FILL);

        CornerPathEffect corEffect = new CornerPathEffect(this.cornerRadius);
        borderPaint.setPathEffect(corEffect);
        shapePaint.setPathEffect(corEffect);
        startPointBorderPaint.setPathEffect(corEffect);
        connectorLinePaint.setPathEffect(corEffect);

        connectorLinePaint.setAntiAlias(true);
        connectorLinePaint.setStyle(Paint.Style.STROKE);
        connectorLinePaint.setStrokeWidth(borderThickness);

        letterTypeface = Typeface.createFromAsset(this.getContext().getAssets(), "fonts/roboto_regular_slim.ttf");

        appColor = new AppColor();
        appColor.setTheme(new CommonSharedData(this.getContext(), SharedData.SHARED_PREF_TITLE).getAppCurrentTheme());

        setMatrix(new SessionPathMatrix());

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (shouldDraw) {
            drawView(canvas);
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        float mWidth = MeasureSpec.getSize(widthMeasureSpec);
        float mHeight = MeasureSpec.getSize(heightMeasureSpec);

        float dimension = Math.min(mWidth, mHeight);

        final float PADDING_HORIZONTAL = 16, PADDING_VERTICAL = 16;

        this.width = dimension - PADDING_HORIZONTAL * 2 - 10;
        this.height = dimension - PADDING_VERTICAL * 2;

        this.horizontalSpace = this.width / this.matrix.getMAX_COL() * 0.98f;
        this.triangularHeight = (float)Math.tan(Math.toRadians(30)) * this.horizontalSpace;
        this.radius = (this.horizontalSpace) / (float) Math.cos(Math.toRadians(30)) * 0.92f;

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        boolean isValidTouchEvent = false;
        float x = event.getX(), y = event.getY();
        int action = event.getAction();
        xCoord = -1;
        yCoord = -1;

        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE) {
            xCoord = x;
            yCoord = y;
            isValidTouchEvent = true;
        }

        return isValidTouchEvent;

    }

    private Context getWeakContext () {

        try {
            return new WeakReference<Context>(getContext()).get();
        } catch (Exception e) {
            return null;
        }

    }

    public void setOnDailyGameCellTapListener(OnDailyGameCellTapListener onDailyGameCellTapListener) {
        this.onDailyGameCellTapListener = onDailyGameCellTapListener;
    }

    public void makeSetsReady () {

        connectorLineSetArrayList.clear();
        hexagonSetArrayList.clear();

        //1. first set the centerX and centerY for first time only
        float startX = horizontalSpace;
        float startY = radius * 2;

        for (int row=0; row<matrix.getMAXRow(); row++) {
            startY = radius * 1.630f * (row + 1);
            for (int col=0; col<matrix.getMAX_COL(); col++) {
                startX = (horizontalSpace) * (col + 1) + 9.55f;
                this.matrix.getBoard()[row][col].setCenterXY(startX, startY);
            }
        }

        // Make connector line set ready.
        final int[] arr = matrix.getSessionRankArr();
        if (arr != null) {
            for (int point=2; point<=arr.length; point++) {
                SessionCell cCell = matrix.getCellFromSessionRank(point);
                SessionCell pCell = matrix.getCellFromSessionRank(point-1);
                if (cCell != null && pCell != null) {
                    connectorLineSetArrayList.add(new ConnectorLineSet(pCell.getCenterX(), pCell.getCenterY(), cCell.getCenterX(), cCell.getCenterY(), pCell.isConnectorTouched() && cCell.isConnectorTouched()));
                }

            }
        }


        // Make hexagon shape set ready.
        for (int row = 0; row < matrix.getMAXRow(); row++) {
            startY = radius * 1.630f * (row + 1);
            for (int col = 0; col < matrix.getMAX_COL(); col++) {
                SessionCell currCell = this.matrix.getBoard()[row][col];
                startX = (horizontalSpace) * (col + 1) + 9.55f;
                if (currCell.getSessionRank() != -1) {
                    addToHexagonSet(startX, startY, currCell);
                }
            }
        }

    }

    public void disableCellTap () {
        isCellTapAllowed = false;
    }

    public void enableCellTap () {
        isCellTapAllowed = true;
    }

    public void refreshViewsColor () {
        if (appColor == null) {
            appColor = new AppColor();
        }
        appColor.setTheme(new CommonSharedData(this.getContext(), SharedData.SHARED_PREF_TITLE).getAppCurrentTheme());
    }

    public void setMatrix (SessionPathMatrix sessionPathMatrix) {
        this.matrix = sessionPathMatrix;
        this.horizontalSpace = this.width / this.matrix.getMAX_COL() * 0.98f;
        this.triangularHeight = (float)Math.tan(Math.toRadians(30)) * this.horizontalSpace;
        this.radius = (this.horizontalSpace) / (float) Math.cos(Math.toRadians(30)) * 0.92f;
    }

    public boolean animateForYTVideo () {

        for (int i=1; i<=matrix.getSessionRankArr().length; i++) {
            SessionCell cell = matrix.getCellFromSessionRank(i);

            if (i == 1) {
                if (cell.getStatus() == SessionCellStatus.GOING_ON) {
                    matrix.setCellStatus(cell.getRowLocation(), cell.getColLocation(), SessionCellStatus.COMPLETED);
                    matrix.setLevelToShape(cell.getRowLocation(), cell.getColLocation(), RandNumber.get(GameLevel.EASY, GameLevel.HARD));
                }
            }

            if (cell.getSessionRank() != -1 && (cell.getLevel() == -1) && (cell.getStatus() == SessionCellStatus.GOING_ON || cell.getStatus() == -1)) {

                matrix.setCellStatus(cell.getRowLocation(), cell.getColLocation(), SessionCellStatus.COMPLETED);
                matrix.setLevelToShape(cell.getRowLocation(), cell.getColLocation(), RandNumber.get(GameLevel.EASY, GameLevel.HARD));

                return true;
            }
        }

        return false;

    }

    private void calculatePath (Canvas canvas) {

        if (connectorLineSetArrayList != null) {
            if (connectorLineSetArrayList.size() > 0) {
                for (ConnectorLineSet connectorLineSet : connectorLineSetArrayList) {
                    drawConnectorLine(canvas, connectorLineSet);
                }
            }
        }

        if (hexagonSetArrayList != null) {
            if (hexagonSetArrayList.size() > 0) {
                for (HexagonSet hexagonSet : hexagonSetArrayList) {
                    drawHexagon(canvas, hexagonSet);
                    drawLetter(canvas, hexagonSet.getStartX(), hexagonSet.getStartY(), hexagonSet.getCurrentCell());
                }
            }
        }

    }

    private void drawView (Canvas canvas) {

        if (shouldDraw) {
            calculatePath(canvas);
            invalidate();
        }

    }

    private void drawLetter (Canvas canvas, float posX, float posY, SessionCell cell) {

        final float fontSize = this.width * 0.055f;

        try {
            if (cell.getStatus() == SessionCellStatus.UNTOUCHED)
                letterPaint.setColor(getWeakContext().getColor(appColor.getUntouchedCellTextColor()));
            else
                letterPaint.setColor(getWeakContext().getColor(appColor.getTouchedCellTextColor()));
        } catch (Exception e) {

        }

        String text;

        try {

            if (cell.getSessionRank() > 0) {
                text = String.valueOf(cell.getSessionRank());
            } else {
                text = "";
            }

        } catch (Exception e) {
            MakeLog.exception(e);
            text = "";
        }

        float width, height;
        letterPaint.getTextBounds(text, 0, (int)(text.length()), letterPaintBounds);
        letterPaint.setTypeface(letterTypeface);
        letterPaint.setFakeBoldText(true);
        letterPaint.setTextSize(fontSize);
        letterPaint.setAlpha(200);
        letterPaint.setAntiAlias(true);

        width = letterPaint.measureText(text);
        height = letterPaint.measureText(text);

        float x, y;
        x = posX - width / 2;

        y = posY + height / 1.95f + radius * 0.06f + (cell.getSessionRank() >= 10 ? -radius*0.25f : 0);

        //y = posY + height / 2 + radius * 0.06f + (cell.getValue() >= 10 ? -radius*0.25f : 0);

        canvas.drawText(text, x, y, letterPaint);

    }

    private void drawConnectorLine (Canvas canvas, ConnectorLineSet set) {

        connectorLinePaint.setColor(getContext().getColor(set.isConnectorTouched() ? appColor.getTouchedCellConnectorLineColor() : appColor.getUntouchedCellConnectorLineColor()));

        canvas.drawLine(
                set.getStartX(),
                set.getStartY(),
                set.getEndX(),
                set.getEndY(),
                connectorLinePaint);

    }

    private void addToHexagonSet (float startX, float startY, SessionCell cell) {

        float triangleHeight = (float) (Math.sqrt(3) * radius / 2);
        float centerX = startX, centerY = startY;
        float point1X = centerX, point1Y = centerY + radius;
        float point2X = centerX - triangleHeight, point2Y = centerY + radius / 2;
        float point3X = centerX - triangleHeight, point3Y = centerY - radius / 2;
        float point4X = centerX, point4Y = centerY - radius;
        float point5X = centerX + triangleHeight, point5Y = centerY - radius / 2;
        float point6X = centerX + triangleHeight, point6Y = centerY + radius / 2;

        Path path = new Path();
        path.moveTo(point1X, point1Y);
        path.lineTo(point2X, point2Y);
        path.lineTo(point3X, point3Y);
        path.lineTo(point4X, point4Y);
        path.lineTo(point5X, point5Y);
        path.lineTo(point6X, point6Y);
        path.close();

        HexagonSet hexagonSet = new HexagonSet(cell, path, startX, startY);
        hexagonSet.setPoints(point2X, point6X, point2Y, point3Y);
        hexagonSetArrayList.add(hexagonSet);

    }

    private void drawHexagon (Canvas canvas, HexagonSet set) {

        int row = set.getCurrentCell().getRowLocation();
        int col = set.getCurrentCell().getColLocation();

        boolean isValidTouch = (xCoord != -1 && yCoord != -1) && (xCoord > set.getPoint2X() && xCoord < set.getPoint6X()) && (yCoord < set.getPoint2Y() && yCoord > set.getPoint3Y());

        SessionCell cell = set.getCurrentCell();

        if (cell.getProperty() == Property.RESERVED && cell.getSessionRank() != -1) {

            int levelType = cell.getLevel();

            if (isValidTouch) {

                if (cell.getProperty() == Property.RESERVED && cell.getSessionRank() != -1) {

                    borderPaint.setColor(this.getContext().getColor(appColor.getTappedCellBorderColor()));
                    shapePaint.setColor(this.getContext().getColor(appColor.getTappedCellBackgroundColor()));
                    matrix.selectRC(row, col);

                    if (isCellTapAllowed) {
                        isCellTapAllowed = false;
                        if (onDailyGameCellTapListener != null) {

                            if (cell.getStatus() == SessionCellStatus.COMPLETED) {

                                String message;

                                if (cell.getSessionRank() != 21)
                                    message = "Session is already completed. Hence, go with the current active session (the animating one).";
                                else
                                    message = "Congratulations! All challenges for today are completed. New challenges will be available tomorrow.";

                                new PopupBottom(getWeakContext()).setMessage(message);
                                onDailyGameCellTapListener.onEnableTap();
                            } else if (cell.getStatus() == SessionCellStatus.UNTOUCHED || cell.getStatus() <= 0) {
                                String message = "First, complete previous sessions. Hence, go with the current active session (the animating one).";
                                new PopupBottom(getWeakContext()).setMessage(message);
                                onDailyGameCellTapListener.onEnableTap();
                            } else {
                                onDailyGameCellTapListener.onTap(cell);
                            }
                        }
                    }

                    canvas.drawPath(set.getShapePath(), borderPaint);
                }

            } else {

                shapePaint.setColor(this.getContext().getColor(this.appColor.getDailyShapeLevelBackgroundColor(levelType)));

                if (cell.getStatus() == SessionCellStatus.GOING_ON) {

                    startPointBorderPaint.setColor(this.getContext().getColor(this.appColor.getDailyShapeLevelBorderColor(levelType)));

                    if (shouldAnimSessionStartPoint) {
                        boolean shouldIncrease = shouldIncreaseStartPoint();
                        if (shouldIncrease) {
                            startPointAnimCounter+=startPointCellAnimSlab;
                        } else {
                            startPointAnimCounter-=startPointCellAnimSlab;
                        }
                        startPointBorderPaint.setStrokeWidth(startPointAnimCounter);
                    }

                    canvas.drawPath(set.getShapePath(), startPointBorderPaint);

                } else {
                    borderPaint.setColor(this.getContext().getColor(this.appColor.getDailyShapeLevelBorderColor(levelType)));
                    canvas.drawPath(set.getShapePath(), borderPaint);
                }

            }

            if (isAnimationMode) {
                if (animationCounter > animationSlab*5.9f) {
                    canvas.drawPath(set.getShapePath(), shapePaint);
                    isAnimationMode = false;
                }
            } else {
                canvas.drawPath(set.getShapePath(), shapePaint);
            }
        }

    }

    private boolean shouldIncreaseStartPoint () {

        boolean shouldDecrease = false;

        if (startPointAnimCounter <= startPointCellAnimMin) {
            shouldDecrease = false;
            shouldIncreaseStartPoint = true;
        }

        if (startPointAnimCounter >= startPointCellAnimMax) {
            shouldDecrease = true;
            shouldIncreaseStartPoint = false;
        }

        if (shouldIncreaseStartPoint && !shouldDecrease) {
            return true;
        }

        return false;

    }

    public void enableDrawing () {
        shouldDraw = true;
        invalidate();
        MakeLog.info(TAG, "Drawing has been enabled");
    }

    public void disableDrawing () {
        shouldDraw = false;
        MakeLog.info(TAG, "Drawing has been disabled");
    }

}
