package com.fourshape.numbermatch.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fourshape.easythingslib.listeners.OnDialogDismissListener;
import com.fourshape.easythingslib.listeners.OnThemeChangeListener;
import com.fourshape.easythingslib.utils.CommonSharedData;
import com.fourshape.easythingslib.utils.MakeLog;
import com.fourshape.numbermatch.app_ads.VideoAd;
import com.fourshape.easythingslib.utils.FormattedData;
import com.fourshape.numbermatch.GameSessionActivity;
import com.fourshape.numbermatch.R;
import com.fourshape.numbermatch.app_ads.PlacementIds;
import com.fourshape.numbermatch.app_color.AppColor;
import com.fourshape.numbermatch.custom_calender.data_formats.DateData;
import com.fourshape.numbermatch.custom_calender.utils.CurrentCalendar;
import com.fourshape.numbermatch.daily_game_view.DailyGameSessionPath;
import com.fourshape.numbermatch.daily_game_view.SessionPathMatrix;
import com.fourshape.numbermatch.daily_game_view.SessionPathView;
import com.fourshape.numbermatch.daily_game_view.structure.SessionCell;
import com.fourshape.numbermatch.daily_game_view.structure.SessionCellStatus;
import com.fourshape.numbermatch.listeners.OnDailyGameCellTapListener;
import com.fourshape.numbermatch.listeners.OnDailySessionLaunchListener;
import com.fourshape.numbermatch.puzzle_core.GameLevel;
import com.fourshape.numbermatch.puzzle_core.GameType;
import com.fourshape.numbermatch.ui_popups.PopupAdBeforeGameStart;
import com.fourshape.numbermatch.ui_popups.PopupDailyGameSessionLaunch;
import com.fourshape.numbermatch.utils.BundleParams;
import com.fourshape.numbermatch.utils.RandNumber;
import com.fourshape.numbermatch.utils.SharedData;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.gson.Gson;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DailyFragment extends Fragment {

    private static final String TAG = "DailyFragment";

    private View mainView;
    private ConstraintLayout parentLL;

    private MaterialCardView calendarContainerCV;
    private SessionPathView sessionPathView;
    private SessionPathMatrix sessionPathMatrix;
    private MaterialCardView dailyChallengeInfoCV;
    private TextView dailyChallengeTitleTV, dailyChallengeDateTV, dailyChallengeDurationTV;

    private boolean shouldRunChallengeDurationClock = false;

    private AppColor appColor;

    private DateData currentDate = null;

    private CircularProgressIndicator progressIndicator;

    private AudioAttributes audioAttributes;
    private SoundPool soundPool;
    private int[] soundId = new int[4];

    private OnThemeChangeListener onThemeChangeListener;

    public DailyFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mainView = inflater.inflate(R.layout.fragment_daily, container, false);

        appColor = new AppColor();

        dailyChallengeInfoCV = mainView.findViewById(R.id.date_info_cv);
        dailyChallengeTitleTV = mainView.findViewById(R.id.daily_challenge_title_tv);
        dailyChallengeDateTV = mainView.findViewById(R.id.daily_challenge_date_tv);
        dailyChallengeDurationTV = mainView.findViewById(R.id.daily_challenge_duration_tv);

        parentLL = mainView.findViewById(R.id.content_container);
        sessionPathView = mainView.findViewById(R.id.session_path_view);
        sessionPathView.setOnDailyGameCellTapListener(onDailyGameCellTapListener);

        progressIndicator = mainView.findViewById(R.id.progress_circular);
        calendarContainerCV = mainView.findViewById(R.id.calendar_container);

        if (getActivity() != null) {

            audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setMaxStreams(8)
                    .setAudioAttributes(audioAttributes)
                    .build();

            getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);

            soundId[0] = soundPool.load(this.getContext(), R.raw.ui_click, 1);

        }

        setViewsListener();

        currentDate = CurrentCalendar.getCurrentDateData();

        String dateText = currentDate.getDay() + " " + FormattedData.getMonth(currentDate.getMonth()) + ", " + currentDate.getYear();
        dailyChallengeDateTV.setText(dateText);

        return mainView;
    }

    @Override
    public void onResume() {
        super.onResume();

        refreshViewsColor();
        makeProgressVisible();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new GetDataTask().execute();
            }
        }, 500);

        if (sessionPathView != null) {
            sessionPathView.enableCellTap();
        }

        shouldRunChallengeDurationClock = true;
        runChallengeDurationClock();

    }

    private void runChallengeDurationClock () {

        if (shouldRunChallengeDurationClock) {

            if (dailyChallengeDurationTV != null) {
                dailyChallengeDurationTV.setText("Ends in " + getChallengeDuration());
            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (shouldRunChallengeDurationClock) {
                        runChallengeDurationClock();
                    }
                }
            }, 1000);
        }

    }

    private String getChallengeDuration () {

        try {

            String dur = "";
            String[] time = new SimpleDateFormat("k:m:s", Locale.getDefault()).format(new Date()).trim().split(":");

            int hour = Integer.parseInt(time[0]), minute = Integer.parseInt(time[1]), seconds = Integer.parseInt(time[2]);

            if (hour <= 23) {
                dur = getFormatted(23-hour);
            } else {
                dur = getFormatted(23);
            }

            dur += " : " + getFormatted(59-minute) + " : " + getFormatted(59-seconds);

            //MakeLog.info(TAG + " Time For: ", time[0] + ":" + time[1] + ":" + time[2] + "   Ends on: " + dur);

            return dur;

        } catch (Exception e) {
            MakeLog.exception(e);
            return "Ends on 23:59:00";
        }

    }

    private String getFormatted (int value) {
        if (value >= 10)
            return String.valueOf(value);
        else
            return "0" + value;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        sessionPathView = null;
        if (soundPool != null)
            soundPool.release();
    }

    private CommonSharedData getCommonSharedData () {
        return new CommonSharedData(getContext(), SharedData.SHARED_PREF_TITLE);
    }

    private void hideContent () {
        if (parentLL != null) {
            parentLL.setVisibility(View.INVISIBLE);
        }
    }

    private void makeProgressVisible () {

        if (progressIndicator != null)
            progressIndicator.setVisibility(View.VISIBLE);

        if (parentLL != null)
            parentLL.setVisibility(View.INVISIBLE);

    }

    private void makeContentVisible () {
        if (progressIndicator != null)
            progressIndicator.setVisibility(View.GONE);

        if (parentLL != null)
            parentLL.setVisibility(View.VISIBLE);
    }

    public void refreshViewsColor () {

        if (getContext() == null) {
            return;
        }

        appColor.setTheme(getCommonSharedData().getAppCurrentTheme());

        if (parentLL != null) {
            parentLL.setBackgroundColor(this.getContext().getColor(this.appColor.getAppBackgroundColor()));
        }

        if (calendarContainerCV != null) {
            calendarContainerCV.setCardBackgroundColor(this.getContext().getColor(this.appColor.getAppBackgroundColor()));
        }

        if (dailyChallengeInfoCV != null) {
            dailyChallengeInfoCV.setCardBackgroundColor(ColorStateList.valueOf(this.getContext().getColor(this.appColor.getAppBarBackgroundColor())));
        }

        if (dailyChallengeTitleTV != null) {
            dailyChallengeTitleTV.setTextColor(this.getContext().getColor(this.appColor.getDailyChallengeTitleColor()));
        }

        if (dailyChallengeDateTV != null) {
            dailyChallengeDateTV.setTextColor(this.getContext().getColor(this.appColor.getDailyChallengeDateColor()));
        }

        if (progressIndicator != null) {
            progressIndicator.setIndicatorColor(this.getContext().getColor(this.appColor.getProgressCircularIndicatorColor()));
        }

        if (sessionPathView != null) {
            sessionPathView.refreshViewsColor();
        }

    }

    private void setViewsListener () {

    }

    @Override
    public void onPause() {
        super.onPause();
        shouldRunChallengeDurationClock = false;
        hideContent();
        if (sessionPathView != null) {
            sessionPathView.disableDrawing();
        }
    }


    private final OnDailyGameCellTapListener onDailyGameCellTapListener = new OnDailyGameCellTapListener() {
        @Override
        public void onTap(SessionCell sessionCell) {

            if (sessionCell.getStatus() == SessionCellStatus.GOING_ON) {
                startGameActivity(sessionCell);
            }

        }

        @Override
        public void onEnableTap() {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (sessionPathView != null) {
                        sessionPathView.enableCellTap();
                    }
                }
            }, 500);
        }
    };

    private Activity getWeakActivity () {
        return new WeakReference<Activity>(getActivity()).get();
    }

    private Context getWeakContext () {
        return new WeakReference<Context>(getContext()).get();
    }

    private void startGameActivity (SessionCell sessionCell) {

        int cellRank = sessionCell.getSessionRank();
        int gameLevel = RandNumber.get(GameLevel.EASY, GameLevel.HARD);

        Intent intent = new Intent(getContext(), GameSessionActivity.class);
        intent.putExtra(BundleParams.GAME_LEVEL, gameLevel);

        intent.putExtra(BundleParams.GAME_TYPE, new SharedData(getContext()).getSavedDailyGame() == null ? GameType.DAILY : GameType.SAVED_DAILY);

        intent.putExtra(BundleParams.DAILY_GAME_DAY, currentDate.getDay());
        intent.putExtra(BundleParams.DAILY_GAME_MONTH, currentDate.getMonth());
        intent.putExtra(BundleParams.DAILY_GAME_YEAR, currentDate.getYear());
        intent.putExtra(BundleParams.CELL_RANK, cellRank);

        new SharedData(getWeakContext()).setDailyGameSessionPathData(new Gson().toJson(new DailyGameSessionPath(sessionPathMatrix, currentDate)));

        startActivity(intent);

        /*
        new PopupAdBeforeGameStart(getWeakContext()).with(getWeakActivity(), intent).setAdPlacementId(PlacementIds.INTERSTITIAL_ON_DAILY_GAME_START_AD).setOnDialogDismissListener(new OnDialogDismissListener() {
            @Override
            public void onDismiss() {
                if (sessionPathView != null) {
                    sessionPathView.enableCellTap();
                }
            }
        }).show();

         */

    }

    private void setDailyGameData () {

        String data = new SharedData(getWeakContext()).getDailyGameSessionPathData();

        if (data == null) {
            sessionPathMatrix = new SessionPathMatrix();
        } else {

            try {

                DailyGameSessionPath dailyGameSessionPath = new Gson().fromJson(data, DailyGameSessionPath.class);
                DateData dateData = dailyGameSessionPath.getDateData();

                if (dateData.getDay() == currentDate.getDay() && dateData.getMonth() == currentDate.getMonth() && dateData.getYear() == currentDate.getYear()) {
                    sessionPathMatrix = dailyGameSessionPath.getSessionPathMatrix();
                } else {
                    sessionPathMatrix = new SessionPathMatrix();
                }

            } catch (Exception e) {
                sessionPathMatrix = new SessionPathMatrix();
            }
        }

    }

    @Override
    protected void finalize() {
        try {
            super.finalize();
            sessionPathMatrix = null;
            sessionPathView = null;
        } catch (Exception e) {

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    private void setDataInBoard () {

        if (sessionPathView != null) {
            sessionPathView.setMatrix(sessionPathMatrix);
            sessionPathView.makeSetsReady();
            sessionPathView.enableDrawing();
            makeContentVisible();
        }

    }

    private class GetDataTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                setDailyGameData();
            } catch (Exception e) {
                return null;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            setDataInBoard();
        }
    }


}