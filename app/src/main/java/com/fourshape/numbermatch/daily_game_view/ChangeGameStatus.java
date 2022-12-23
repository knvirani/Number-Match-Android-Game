package com.fourshape.numbermatch.daily_game_view;

import android.content.Context;

import com.fourshape.numbermatch.custom_calender.data_formats.DateData;
import com.fourshape.numbermatch.daily_game_view.structure.SessionCellStatus;
import com.fourshape.numbermatch.puzzle_core.GameLevel;
import com.fourshape.numbermatch.utils.MakeLog;
import com.fourshape.numbermatch.utils.SharedData;
import com.google.gson.Gson;

public class ChangeGameStatus {

    private static final String TAG = "ChangeGameStatus";

    public void markCompleted (Context context, DateData dateData, int level, int cellRank) {

        try {

            String pathData = new SharedData(context).getDailyGameSessionPathData();

            if (pathData == null)
            {
                MakeLog.info(TAG, "Session Path Data is NULL.");
                return;
            }

            DailyGameSessionPath dailyGameSessionPath = new Gson().fromJson(pathData, DailyGameSessionPath.class);

            SessionPathMatrix matrix = dailyGameSessionPath.getSessionPathMatrix();

            boolean isFirstOpDone = false;
            boolean isSecondOpDone = false;

            for (int row=0; row<matrix.getMAXRow(); row++) {

                for (int col=0; col<matrix.getMAX_COL(); col++) {

                    if (matrix.getBoard()[row][col].getSessionRank() == cellRank) {
                        //MakeLog.info(TAG,"Cell Rank: " + cellRank + " Set to completed.");
                        matrix.setCellStatus(row, col, SessionCellStatus.COMPLETED);
                        matrix.setLevelToShape(row, col, level);
                        isFirstOpDone = true;
                    }

                    if (matrix.getBoard()[row][col].getSessionRank() == (cellRank+1)) {
                        //MakeLog.info(TAG,"Cell Rank: " + (cellRank+1) + " Set to Going On.");
                        matrix.setCellStatus(row, col, SessionCellStatus.GOING_ON);
                        matrix.setLevelToShape(row, col, GameLevel.START_POINT);
                        isSecondOpDone = true;
                    }

                    if (isFirstOpDone && isSecondOpDone) {
                        break;
                    }

                }

                if (isFirstOpDone && isSecondOpDone)
                    break;

            }

            dailyGameSessionPath.setSessionPathMatrix(matrix);
            dailyGameSessionPath.setDateData(dateData);

            new SharedData(context).setDailyGameSessionPathData(new Gson().toJson(dailyGameSessionPath));
            MakeLog.info(TAG, "Changed session status");

        } catch (Exception e) {
            MakeLog.exception(e);
        }

    }

}
