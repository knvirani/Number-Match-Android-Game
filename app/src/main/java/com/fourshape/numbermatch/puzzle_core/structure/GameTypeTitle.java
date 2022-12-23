package com.fourshape.numbermatch.puzzle_core.structure;

import com.fourshape.numbermatch.puzzle_core.GameType;

public class GameTypeTitle {

    public static String get (int gameType) {

        if (gameType == GameType.GENERAL) {
            return "NEW SESSION";
        } else if (gameType == GameType.SAVED_GENERAL) {
            return "LAST SESSION";
        } else if (gameType == GameType.DAILY || gameType == GameType.NEW_DAILY_WITH_DB) {
            return "NEW DAILY";
        } else if (gameType == GameType.SAVED_DAILY || gameType == GameType.SAVED_DAILY_FROM_DB) {
            return "LAST DAILY";
        } else {
            return "NEW SESSION";
        }

    }

}
