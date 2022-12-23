package com.fourshape.numbermatch.game_db;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fourshape.easythingslib.utils.CommonSharedData;
import com.fourshape.easythingslib.utils.FormattedData;
import com.fourshape.numbermatch.R;
import com.fourshape.numbermatch.app_color.AppColor;
import com.fourshape.numbermatch.listeners.OnStatsDataFetchListener;
import com.fourshape.numbermatch.puzzle_core.structure.GameCompletion;
import com.fourshape.numbermatch.puzzle_core.structure.GameData;
import com.fourshape.numbermatch.utils.SharedData;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class FetchStats {

    private static final String TAG = "FetchStats";

    private int statsId;
    private GameDB gameDB;

    private ViewGroup viewContainer;
    private AppColor appColor;
    private Context context;

    private OnStatsDataFetchListener onStatsDataFetchListener;

    int totalCompleted = 0;
    float averageScore = 0;
    int lowestTime = 0;
    int highestTime = 0;
    int allTimeBestScore = 0;
    int totalTimeSpent = 0;
    int bestStepPerSession = 0;

    public FetchStats (Context context, ViewGroup viewContainer, int gameLevel) {
        this.context = context;
        this.gameDB = new GameDB(context);
        appColor = new AppColor();
        appColor.setTheme(new CommonSharedData(context, SharedData.SHARED_PREF_TITLE).getAppCurrentTheme());
        this.statsId = gameLevel;
        this.viewContainer = viewContainer;
    }

    public void setOnStatsDataFetchListener (OnStatsDataFetchListener onStatsDataFetchListener) {
        this.onStatsDataFetchListener = onStatsDataFetchListener;
    }

    public void fetchData () {
        new FetchDataInBG().execute();
    }

    private class FetchDataInBG extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPostExecute(Void unused) {

            super.onPostExecute(unused);

            if (onStatsDataFetchListener != null) {
                onStatsDataFetchListener.onFetched();
            }

        }

        @Override
        protected Void doInBackground(Void... voids) {
            fetch();
            return null;
        }
    }

    private void fetch () {

        ArrayList<GameData> gameDataArrayList = gameDB.getRecordsPerLevelWithoutSessionData(statsId);
        gameDB.closeDB();

        if (gameDataArrayList != null && gameDataArrayList.size() > 0) {

            for (GameData gameData : gameDataArrayList) {

                if (gameData.getCompletion() == GameCompletion.COMPLETE) {

                    totalCompleted++;
                    averageScore += gameData.getScore();

                    if (lowestTime == 0) {
                        lowestTime = gameData.getTime();
                    } else if (gameData.getTime() < lowestTime) {
                        lowestTime = gameData.getTime();
                    }

                    if (highestTime == 0) {
                        highestTime = gameData.getTime();
                    } else if (gameData.getTime() > highestTime) {
                        highestTime = gameData.getTime();
                    }

                    if (allTimeBestScore == 0) {
                        allTimeBestScore = gameData.getScore();
                    } else if (gameData.getTime() > allTimeBestScore) {
                        allTimeBestScore = gameData.getScore();
                    }

                    if (bestStepPerSession == 0) {
                        bestStepPerSession = gameData.getStep();
                    } else if (gameData.getStep() > bestStepPerSession) {
                        bestStepPerSession = gameData.getStep();
                    }

                }

                totalTimeSpent += gameData.getTime();

            }

        } else {

        }

        averageScore = averageScore/totalCompleted;

    }

    public void fitData () {

        fitDataIntoViews(0, allTimeBestScore);
        fitDataIntoViews(1, (int) averageScore);
        fitDataIntoViews(2, totalCompleted);
        fitDataIntoViews(3, lowestTime);
        fitDataIntoViews(4, highestTime);
        fitDataIntoViews(5, totalTimeSpent);
        fitDataIntoViews(6, bestStepPerSession);

    }

    private void fitDataIntoViews (int itr, int value) {

        View view;

        if (itr == 0) {
            view = LayoutInflater.from(context).inflate(R.layout.default_child_stats_for_alltimebest_score, null);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.dynamic_child_for_stats, null);
        }

        MaterialCardView parentCV = view.findViewById(R.id.parent_cv);
        TextView statsValueTV = view.findViewById(R.id.stats_value);
        TextView statsTitleTV = view.findViewById(R.id.stats_title);

        if (itr == 0) {
            parentCV.setCardBackgroundColor(parentCV.getContext().getColor(this.appColor.getDynamicStatsCardBackgroundColor()));
            statsValueTV.setTextColor(statsValueTV.getContext().getColor(this.appColor.getDynamicStatsValueTextColor()));
            statsTitleTV.setTextColor(statsTitleTV.getContext().getColor(this.appColor.getAppBarTitleTextColor()));
            statsTitleTV.setBackgroundColor(statsTitleTV.getContext().getColor(this.appColor.getDynamicStatsCardBackgroundColor()));
        } else {
            parentCV.setCardBackgroundColor(parentCV.getContext().getColor(this.appColor.getDynamicStatsCardBackgroundColor()));
            statsValueTV.setTextColor(statsValueTV.getContext().getColor(this.appColor.getDynamicStatsValueTextColor()));
            statsTitleTV.setTextColor(statsTitleTV.getContext().getColor(this.appColor.getDynamicStatsTitleTextColor()));
            statsTitleTV.setBackgroundColor(statsTitleTV.getContext().getColor(this.appColor.getDynamicStatsTitleBackgroundColor()));
        }

        statsTitleTV.setText(getStatusTitle(itr));

        if (itr == 0 || itr == 1 || itr == 2 || itr == 6) {
            statsValueTV.setText(String.valueOf(value));
        } else {
            if (value == 0) {
                statsValueTV.setText("--:--");
            } else {
                statsValueTV.setText(FormattedData.getFormattedTime(value));
            }
        }

        if (viewContainer != null) {
            viewContainer.addView(view);
        }

    }

    private String getStatusTitle (int i) {
        if (i == 0)
            return "All Time Best Score";
        else if (i == 1)
            return "Average Score per Session";
        else if (i == 2)
            return "Total Session Completed";
        else if (i == 3)
            return "Lowest Time";
        else if (i == 4)
            return "Highest Time";
        else if (i == 5)
            return "Total Time Spent";
        else if (i == 6)
            return "Best Step per Session";
        else
            return "None";
    }

}
