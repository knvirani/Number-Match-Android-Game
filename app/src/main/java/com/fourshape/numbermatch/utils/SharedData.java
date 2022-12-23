package com.fourshape.numbermatch.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.fourshape.easythingslib.utils.AppThemes;
import com.fourshape.numbermatch.puzzle_core.BoardFontSize;
import com.fourshape.numbermatch.puzzle_core.GameControlLimit;
import com.fourshape.numbermatch.puzzle_core.GameLevel;

public class SharedData {

    public static final String SHARED_PREF_TITLE = "NUMBERMATCH_GUJMCQ_PREF";
    private SharedPreferences sharedPreference;
    private static final String DATE_FORMAT = "dd-MMM-yyyy";

    public SharedData(Context context) {
        if (context != null) {
            this.sharedPreference = context.getSharedPreferences(SHARED_PREF_TITLE, Context.MODE_PRIVATE);
        }
    }

    public void setAdsSoundStatus (boolean shouldPlaySound) {
        if (this.sharedPreference == null)
            return;
        this.sharedPreference.edit().putBoolean("ads_sound", shouldPlaySound).apply();
    }

    public boolean getAdsSoundStatus () {
        if (this.sharedPreference == null)
            return true;
        else
            return this.sharedPreference.getBoolean("ads_sound", true);
    }

    public void setDailyGameSessionPathData (String data) {
        if (this.sharedPreference == null)
            return;
        this.sharedPreference.edit().putString("daily_game_data", data).apply();
    }

    public String getDailyGameSessionPathData () {
        if (this.sharedPreference == null)
            return null;
        else
            return this.sharedPreference.getString("daily_game_data", null);
    }

    public void setBoardFontSize (float fontSize) {

        if (this.sharedPreference == null)
            return;

        this.sharedPreference.edit().putFloat("board_font_size", fontSize).apply();

    }

    public float getBoardFontSize () {

        if (this.sharedPreference == null)
            return BoardFontSize.MEDIUM;
        else
            return this.sharedPreference.getFloat("board_font_size", BoardFontSize.MEDIUM);

    }

    public void setTutorDialogShown () {
        if (this.sharedPreference == null)
            return;
        this.sharedPreference.edit().putBoolean("tutor_dialog", true).apply();
    }

    public boolean isTutorDialogShown () {
        if (this.sharedPreference == null)
            return false;
        else
            return this.sharedPreference.getBoolean("tutor_dialog", false);
    }

    public void setAllTimeBestScore (int score, int gameLevel) {

        if (this.sharedPreference == null)
            return;

        if (gameLevel == GameLevel.EASY)
            this.sharedPreference.edit().putInt("easy_level_all_time_best_score", score).apply();
        else if (gameLevel == GameLevel.MEDIUM)
            this.sharedPreference.edit().putInt("medium_level_all_time_best_score", score).apply();
        else if (gameLevel == GameLevel.HARD)
            this.sharedPreference.edit().putInt("hard_level_all_time_best_score", score).apply();

    }

    public int getAllTimeBestScore (int gameLevel) {

        if (this.sharedPreference == null)
            return 0;

        if (gameLevel == GameLevel.EASY)
            return this.sharedPreference.getInt("easy_level_all_time_best_score", 0);
        else if (gameLevel == GameLevel.MEDIUM)
            return this.sharedPreference.getInt("medium_level_all_time_best_score", 0);
        else if (gameLevel == GameLevel.HARD)
            return this.sharedPreference.getInt("hard_level_all_time_best_score", 0);
        else
            return 0;

    }

    public void setAchievement (String achievementJson, int gameLevel) {

        if (this.sharedPreference == null)
            return;

        if (gameLevel == GameLevel.EASY)
            this.sharedPreference.edit().putString("easy_level_achievements", achievementJson).apply();
        else if (gameLevel == GameLevel.MEDIUM)
            this.sharedPreference.edit().putString("medium_level_achievements", achievementJson).apply();
        else if (gameLevel == GameLevel.HARD)
            this.sharedPreference.edit().putString("hard_level_achievements", achievementJson).apply();

    }

    public String getAchievementJson (int gameLevel) {
        if (this.sharedPreference == null)
            return null;
        else {
            if (gameLevel == GameLevel.EASY)
                return this.sharedPreference.getString("easy_level_achievements", null);
            else if (gameLevel == GameLevel.MEDIUM)
                return this.sharedPreference.getString("medium_level_achievements", null);
            else if (gameLevel == GameLevel.HARD)
                return this.sharedPreference.getString("hard_level_achievements", null);
            else
                return null;
        }
    }

    public void saveGeneralGame (String generalGameJson) {
        if (this.sharedPreference == null)
            return;
        this.sharedPreference.edit().putString("general_game", generalGameJson).apply();
    }

    public String getSavedGeneralGame () {
        if (this.sharedPreference == null)
            return null;
        else
            return this.sharedPreference.getString("general_game", null);
    }

    public void saveDailyGame (String dailyGameJson) {
        if (this.sharedPreference == null)
            return;
        this.sharedPreference.edit().putString("daily_game", dailyGameJson).apply();
    }

    public String getSavedDailyGame () {
        if (this.sharedPreference == null)
            return null;
        else
            return this.sharedPreference.getString("daily_game", null);
    }

    public void setSetId (int setId) {
        if (this.sharedPreference == null)
            return;
        this.sharedPreference.edit().putInt("board_set_id", setId).apply();
    }

    public int getSetId () {
        if (this.sharedPreference == null)
            return 0;
        else
            return this.sharedPreference.getInt("board_set_id", -1);
    }

    public void setSetIncrementId (int setId) {
        if (this.sharedPreference == null)
            return;
        this.sharedPreference.edit().putInt("set_incr_id", setId).apply();
    }

    public int getSetIncrementId () {
        if (this.sharedPreference == null)
            return 0;
        else
            return this.sharedPreference.getInt("set_incr_id", -1);
    }

    public void setHintControl (int hintControlStatus) {
        if (this.sharedPreference == null)
            return;
        this.sharedPreference.edit().putInt("hint_control_status", hintControlStatus).apply();
    }

    public int getHintControl () {
        if (this.sharedPreference == null)
            return GameControlLimit.HINT_LIMIT;
        else
            return this.sharedPreference.getInt("hint_control_status", GameControlLimit.HINT_LIMIT);
    }


    public void toggleGameScore (boolean status) {
        if (this.sharedPreference == null)
            return;

        this.sharedPreference.edit().putBoolean("game_score", status).apply();
    }

    public boolean getGameScoreStatus () {
        if (this.sharedPreference == null)
            return true;
        else {
            return this.sharedPreference.getBoolean("game_score", true);
        }
    }

    public void toggleGameTime (boolean status) {
        if (this.sharedPreference == null)
            return;

        this.sharedPreference.edit().putBoolean("game_time", status).apply();
    }

    public boolean getGameTimeStatus () {
        if (this.sharedPreference == null)
            return true;
        else {
            return this.sharedPreference.getBoolean("game_time", true);
        }
    }


}
