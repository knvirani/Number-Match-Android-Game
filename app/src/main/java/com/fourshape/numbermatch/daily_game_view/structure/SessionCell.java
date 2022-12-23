package com.fourshape.numbermatch.daily_game_view.structure;

public class SessionCell {

    private int rank;
    private int status;
    private int level;
    private int value;
    private int sessionRank;
    private int property;
    private int rowLocation, colLocation;
    private float centerX, centerY;
    private boolean isConnectorTouched;

    public SessionCell(int rank, int status, int level, int rowLocation, int property, int colLocation) {
        this.rank = rank;
        this.status = status;
        this.level = level;
        this.rowLocation = rowLocation;
        this.colLocation = colLocation;
        this.property = property;
        this.sessionRank = -1;
        isConnectorTouched = false;
        this.value = -1;
        this.centerY = -1;
        this.centerX = -1;
    }

    public void setConnectorTouched(boolean connectorTouched) {
        isConnectorTouched = connectorTouched;
    }

    public boolean isConnectorTouched() {
        return isConnectorTouched;
    }

    public void setSessionRank(int sessionRank) {
        this.sessionRank = sessionRank;
    }

    public void setCenterXY (float centerX, float centerY) {
        this.centerX = centerX;
        this.centerY = centerY;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public int getProperty() {
        return property;
    }

    public int getSessionRank() {
        return sessionRank;
    }

    public int getLevel() {
        return level;
    }

    public float getCenterY() {
        return centerY;
    }

    public float getCenterX() {
        return centerX;
    }

    public int getRank() {
        return rank;
    }

    public int getColLocation() {
        return colLocation;
    }

    public int getRowLocation() {
        return rowLocation;
    }

    public int getStatus() {
        return status;
    }
}
