package com.fourshape.numbermatch.puzzle_core;

public class GameLevelTitle {

    public static String get (int gameLevel) {
        switch (gameLevel) {

            case GameLevel.EASY:
                return "Easy";

            case GameLevel.MEDIUM:
                return "Medium";

                case GameLevel.HARD:
                    return "Hard";

            default:
                return "Easy";

        }
    }

}
