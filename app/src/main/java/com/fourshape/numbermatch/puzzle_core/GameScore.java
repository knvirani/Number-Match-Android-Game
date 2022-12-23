package com.fourshape.numbermatch.puzzle_core;

import com.fourshape.numbermatch.listeners.OnGameScoreChangeListener;

public class GameScore {

    private int score;

    private static final int SCORE_SESSION_WITHOUT_GAP_SOLVE = 1;
    private static final int SCORE_SESSION_WITH_GAP_SOLVE = 4;
    private static final int SCORE_SESSION_WITH_ROW_SOLVE = 12;
    private static final int SCORE_SESSION_WITH_STEP_UP_SOLVE = 250;

    private OnGameScoreChangeListener onGameScoreChangeListener;
    private int scoreForAnimation = 0;

    public GameScore () {
        score = 0;
        scoreForAnimation = 0;
    }

    public void setOnGameScoreChangeListener (OnGameScoreChangeListener onGameScoreChangeListener) {
        this.onGameScoreChangeListener = onGameScoreChangeListener;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void rowSolved () {
        score += SCORE_SESSION_WITH_ROW_SOLVE;
        if (onGameScoreChangeListener != null) {
            this.scoreForAnimation = SCORE_SESSION_WITH_ROW_SOLVE;
            onGameScoreChangeListener.onChange(score);
        }
    }

    public void stepSolved () {
        score += SCORE_SESSION_WITH_STEP_UP_SOLVE;
        if (onGameScoreChangeListener != null) {
            this.scoreForAnimation = SCORE_SESSION_WITH_STEP_UP_SOLVE;
            onGameScoreChangeListener.onChange(score);
        }
    }

    public void solvedWithGap () {
        score += SCORE_SESSION_WITH_GAP_SOLVE;
        if (onGameScoreChangeListener != null) {
            this.scoreForAnimation = SCORE_SESSION_WITH_GAP_SOLVE;
            onGameScoreChangeListener.onChange(score);
        }
    }

    public void solvedWithoutGap () {
        score += SCORE_SESSION_WITHOUT_GAP_SOLVE;
        if (onGameScoreChangeListener != null) {
            this.scoreForAnimation = SCORE_SESSION_WITHOUT_GAP_SOLVE;
            onGameScoreChangeListener.onChange(score);
        }
    }

    public int getScoreForAnimation() {
        return scoreForAnimation;
    }

    public int getScore() {
        return score;
    }

    public void reset () {
        this.score = 0;
        this.scoreForAnimation = 0;
    }
}
