package com.fourshape.numbermatch.puzzle_core.structure;

import com.fourshape.numbermatch.puzzle_core.GameType;

public class GameData {

    private int rowControlLimit;
    private int level;
    private int mainType;
    private int time;
    private int day;
    private int month;
    private int year;
    private int score;
    private int completion;
    private int step;
    private String data;
    private int recordId;

    public GameData (int recordId, int level, int mainType, int time, int day, int month, int year, int score, int completion, int step, int rowControlLimit, String data) {

        this.level = level;

        if (mainType == GameType.GENERAL || mainType == GameType.SAVED_GENERAL) {
            this.mainType = MainType.GENERAL;
        } else {
            this.mainType = MainType.DAILY_GAME;
        }

        this.time = time;
        this.day = day;
        this.month = month;
        this.year = year;
        this.score = score;
        this.completion = completion;
        this.step = step;
        this.data = data;
        this.recordId = recordId;
        this.rowControlLimit = rowControlLimit;

    }

    public int getRowControlLimit() {
        return rowControlLimit;
    }

    public int getRecordId() {
        return recordId;
    }

    public int getLevel() {
        return level;
    }

    public int getMainType() {
        return mainType;
    }

    public int getTime() {
        return time;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public int getScore() {
        return score;
    }

    public int getCompletion() {
        return completion;
    }

    public int getStep() {
        return step;
    }

    public String getData() {
        return data;
    }
}
