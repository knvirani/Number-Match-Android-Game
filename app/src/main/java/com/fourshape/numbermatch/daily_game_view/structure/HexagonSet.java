package com.fourshape.numbermatch.daily_game_view.structure;

import android.graphics.Path;

public class HexagonSet {
    private float startX, startY, point2X, point6X, point2Y, point3Y;
    private SessionCell currentCell;
    private Path shapePath;

    public HexagonSet (SessionCell currentCell, Path shapePath, float startX, float startY) {
        this.currentCell = currentCell;
        this.shapePath = shapePath;
        this.startX = startX;
        this.startY = startY;
    }

    public void setPoints (float point2X, float point6X, float point2Y, float point3Y) {
        this.point2X = point2X;
        this.point6X = point6X;
        this.point2Y = point2Y;
        this.point3Y = point3Y;
    }


    public float getPoint2X() {
        return point2X;
    }

    public float getPoint2Y() {
        return point2Y;
    }

    public float getPoint3Y() {
        return point3Y;
    }

    public float getPoint6X() {
        return point6X;
    }

    public float getStartY() {
        return startY;
    }

    public float getStartX() {
        return startX;
    }

    public Path getShapePath() {
        return shapePath;
    }

    public SessionCell getCurrentCell() {
        return currentCell;
    }
}
