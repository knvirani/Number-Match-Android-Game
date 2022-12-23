package com.fourshape.numbermatch.daily_game_view.structure;

public class ConnectorLineSet {

    private float startX, startY, endX, endY;
    private boolean isConnectorTouched;

    public ConnectorLineSet(float startX, float startY, float endX, float endY, boolean isConnectorTouched) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.isConnectorTouched = isConnectorTouched;
    }

    public float getEndX() {
        return endX;
    }

    public float getEndY() {
        return endY;
    }

    public float getStartX() {
        return startX;
    }

    public float getStartY() {
        return startY;
    }

    public boolean isConnectorTouched() {
        return isConnectorTouched;
    }
}
