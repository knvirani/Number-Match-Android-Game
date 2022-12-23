package com.fourshape.numbermatch.daily_game_view;

import com.fourshape.numbermatch.custom_calender.data_formats.DateData;

public class DailyGameSessionPath {

    private SessionPathMatrix sessionPathMatrix;
    private DateData dateData;

    public DailyGameSessionPath () {}

    public DailyGameSessionPath (SessionPathMatrix sessionPathMatrix, DateData dateData) {
        this.sessionPathMatrix = sessionPathMatrix;
        this.dateData = dateData;
    }

    public void setDateData(DateData dateData) {
        this.dateData = dateData;
    }

    public void setSessionPathMatrix(SessionPathMatrix sessionPathMatrix) {
        this.sessionPathMatrix = sessionPathMatrix;
    }

    public DateData getDateData() {
        return dateData;
    }

    public SessionPathMatrix getSessionPathMatrix() {
        return sessionPathMatrix;
    }
}
