package com.fourshape.numbermatch.ui_popups;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;

import com.fourshape.customcalendarlib.data_formats.DateData;
import com.fourshape.easythingslib.utils.CommonSharedData;
import com.fourshape.easythingslib.utils.DimPopupWindow;
import com.fourshape.easythingslib.utils.MakeLog;
import com.fourshape.numbermatch.GameSessionActivity;
import com.fourshape.numbermatch.R;
import com.fourshape.numbermatch.app_color.AppColor;
import com.fourshape.numbermatch.game_db.GameDB;
import com.fourshape.numbermatch.listeners.OnDailySessionLaunchListener;
import com.fourshape.numbermatch.puzzle_core.GameLevel;
import com.fourshape.numbermatch.puzzle_core.GameType;
import com.fourshape.numbermatch.puzzle_core.structure.GameData;
import com.fourshape.numbermatch.utils.BundleParams;
import com.fourshape.numbermatch.utils.RandNumber;
import com.fourshape.numbermatch.utils.SharedData;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.gson.Gson;

import java.util.Date;

public class PopupDailyGameSessionLaunch {

    private static final String TAG = "PopupDailyGameSessionLaunch";

    private View view;
    private PopupWindow popupWindow;

    private LinearLayoutCompat contentLayout;
    private CircularProgressIndicator progressIndicator;

    private AppColor appColor;
    private OnDailySessionLaunchListener onDailySessionLaunchListener;
    private int recordId;
    private DateData targetDateData;

    private MaterialButton resumeMB, restartMB;

    private int gameLevel, gameSeconds;
    private String gameLevelTitle, gameSessionData;

    public PopupDailyGameSessionLaunch (Context context) {
        this.view = LayoutInflater.from(context).inflate(R.layout.popup_daily_game_session_launch_option, null);
        appColor = new AppColor();
        preparePopup();
    }

    public void setOnDailySessionLaunchListener (OnDailySessionLaunchListener onDailySessionLaunchListener) {
        this.onDailySessionLaunchListener = onDailySessionLaunchListener;
    }

    private void makeContentLayoutVisible () {
        contentLayout.setVisibility(View.VISIBLE);
        progressIndicator.setVisibility(View.INVISIBLE);
    }

    private void makeProgressVisible () {
        contentLayout.setVisibility(View.INVISIBLE);
        progressIndicator.setVisibility(View.VISIBLE);
    }

    public void setSearchData (int recordId, DateData dateData) {
        this.recordId = recordId;
        this.targetDateData = dateData;
    }

    public boolean isShowing () {
        if (popupWindow == null)
            return false;
        else
            return popupWindow.isShowing();
    }

    public void show () {

        refreshViewsColor();
        makeProgressVisible();

        if (this.view == null) {
            MakeLog.error(TAG, "View is NULL.");
            return;
        }

        if (this.popupWindow == null) {
            MakeLog.error(TAG, "Window is NULL");
            return;
        }

        if (this.popupWindow.isShowing()) {
            MakeLog.error(TAG, "Window is already visible");
            return;
        }

        popupWindow.setOverlapAnchor(true);
        popupWindow.setAttachedInDecor(true);
        popupWindow.showAtLocation(this.view, Gravity.BOTTOM, 0, 0);
        DimPopupWindow.dimBehindWithFactor(popupWindow, 0.5f);
        MakeLog.info(TAG, "Popup is Showing.");

        GameDB dailyGameDB = new GameDB(view.getContext());
        new FetchDBRecords(dailyGameDB).execute();

    }

    private void refreshViewsColor () {

        if (this.view.getContext() == null) {
            return;
        }

        appColor.setTheme(new CommonSharedData(this.view.getContext(), SharedData.SHARED_PREF_TITLE).getAppCurrentTheme());

        this.view.setBackgroundColor(this.view.getContext().getColor(this.appColor.getPopupCardBackgroundColor()));

        if (contentLayout != null) {
            contentLayout.setBackgroundColor(this.view.getContext().getColor(this.appColor.getPopupCardBackgroundColor()));
        }

        if (resumeMB != null) {
            resumeMB.setBackgroundTintList(ColorStateList.valueOf(this.view.getContext().getColor(this.appColor.getPrimaryBtnBackgroundColor())));
            resumeMB.setTextColor(this.view.getContext().getColor(this.appColor.getPrimaryBtnTextColor()));
            resumeMB.setIconTint(ColorStateList.valueOf(this.view.getContext().getColor(this.appColor.getPrimaryBtnTextColor())));
        }

        if (restartMB != null) {
            restartMB.setBackgroundTintList(ColorStateList.valueOf(this.view.getContext().getColor(this.appColor.getPrimaryBtnBackgroundColor())));
            restartMB.setTextColor(this.view.getContext().getColor(this.appColor.getPrimaryBtnTextColor()));
            restartMB.setIconTint(ColorStateList.valueOf(this.view.getContext().getColor(this.appColor.getPrimaryBtnTextColor())));
        }

        if (progressIndicator != null) {
            progressIndicator.setIndicatorColor(this.view.getContext().getColor(this.appColor.getProgressCircularIndicatorColor()));
            progressIndicator.setTrackColor(this.view.getContext().getColor(this.appColor.getGameSessionLinearProgressTrackColor()));
        }

    }

    private void preparePopup () {

        this.contentLayout = this.view.findViewById(R.id.content_layout);
        this.resumeMB = this.view.findViewById(R.id.resume_mb);
        this.progressIndicator = this.view.findViewById(R.id.progress_circular);
        this.restartMB = this.view.findViewById(R.id.restart_mb);

        int width = FrameLayout.LayoutParams.MATCH_PARENT;
        int height = FrameLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = false;

        this.popupWindow = new PopupWindow(this.view, width, height, focusable);

        this.contentLayout.setVisibility(View.INVISIBLE);
        this.progressIndicator.setVisibility(View.VISIBLE);

        setViewsListener();

    }

    private void setViewsListener () {

        this.resumeMB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popupWindow.dismiss();

                Intent intent = new Intent(view.getContext(), GameSessionActivity.class);
                intent.putExtra(BundleParams.GAME_LEVEL, gameLevel);
                intent.putExtra(BundleParams.GAME_TYPE, GameType.SAVED_DAILY_FROM_DB);
                intent.putExtra(BundleParams.GAME_SESSION, gameSessionData);
                intent.putExtra(BundleParams.DAILY_GAME_RECORD_ID, recordId);

                if (targetDateData != null) {
                    intent.putExtra(BundleParams.DAILY_GAME_DAY, targetDateData.getDay());
                    intent.putExtra(BundleParams.DAILY_GAME_MONTH, targetDateData.getMonth());
                    intent.putExtra(BundleParams.DAILY_GAME_YEAR, targetDateData.getYear());
                }

                if (onDailySessionLaunchListener != null) {
                    onDailySessionLaunchListener.onResume(intent);
                }

            }
        });

        this.restartMB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int gameLevel = RandNumber.get(GameLevel.EASY, GameLevel.HARD);

                Intent intent = new Intent(view.getContext(), GameSessionActivity.class);
                intent.putExtra(BundleParams.GAME_LEVEL, gameLevel);
                intent.putExtra(BundleParams.GAME_TYPE, GameType.NEW_DAILY_WITH_DB);
                intent.putExtra(BundleParams.DAILY_GAME_RECORD_ID, recordId);

                if (targetDateData != null) {
                    intent.putExtra(BundleParams.DAILY_GAME_DAY, targetDateData.getDay());
                    intent.putExtra(BundleParams.DAILY_GAME_MONTH, targetDateData.getMonth());
                    intent.putExtra(BundleParams.DAILY_GAME_YEAR, targetDateData.getYear());
                }

                popupWindow.dismiss();

                if (onDailySessionLaunchListener != null) {
                    onDailySessionLaunchListener.onRestart(intent);
                }

            }
        });

    }

    private class FetchDBRecords extends AsyncTask<Void, Void, GameData> {

        private final GameDB dailyGameDB;

        public FetchDBRecords (GameDB dailyGameDB) {
            this.dailyGameDB = dailyGameDB;
        }

        @Override
        protected void onPostExecute(GameData data) {
            super.onPostExecute(data);

            try {

                if (data != null) {

                    int time = data.getTime();
                    int gameLevel = data.getLevel();

                    MakeLog.info(TAG, "Time: " + time + " Level: " + gameLevel);

                    if (time != -1 && gameLevel != -1) {

                        PopupDailyGameSessionLaunch.this.gameLevel = gameLevel;
                        PopupDailyGameSessionLaunch.this.gameLevelTitle = getGameLevel(gameLevel);
                        PopupDailyGameSessionLaunch.this.gameSeconds = time;

                        gameSessionData = new Gson().toJson(data);

                        makeContentLayoutVisible();

                    } else {

                        MakeLog.error(TAG, "Time & Level are -1.");

                        gameSessionData = null;
                        makeContentLayoutVisible();
                        resumeMB.setVisibility(View.GONE);
                    }

                } else {

                    MakeLog.error(TAG, "NULL Session data.");

                    gameSessionData = null;
                    makeContentLayoutVisible();
                    resumeMB.setVisibility(View.GONE);
                }

            } catch (Exception e) {
                MakeLog.exception(e);
            }

        }

        @Override
        protected GameData doInBackground(Void... voids) {

            try {

                return dailyGameDB.getFullRecordWithSessionData(recordId);

            } catch (Exception e) {
                MakeLog.exception(e);
            }

            return null;
        }
    }

    public String getGameLevel (int gameLevel) {
        if (gameLevel == GameLevel.EASY) {
            return "EASY";
        } else if (gameLevel == GameLevel.MEDIUM) {
            return "MEDIUM";
        } else if (gameLevel == GameLevel.HARD) {
            return "HARD";
        } else {
            return "NONE";
        }
    }


}
