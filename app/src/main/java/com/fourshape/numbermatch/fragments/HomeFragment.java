package com.fourshape.numbermatch.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.Image;
import android.media.SoundPool;
import android.os.Bundle;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fourshape.customcalendarlib.data_formats.DateData;
import com.fourshape.customcalendarlib.utils.CurrentCalendar;
import com.fourshape.easythingslib.listeners.OnAdConsentDialogListener;
import com.fourshape.easythingslib.listeners.OnPolicyPopupDismissListener;
import com.fourshape.easythingslib.listeners.OnPolicyRequiredMessageListener;
import com.fourshape.easythingslib.listeners.OnPolicyStatusListener;
import com.fourshape.easythingslib.ui_popups.PopupPolicyRequired;
import com.fourshape.easythingslib.ui_popups.PopupPrivacyPolicy;
import com.fourshape.easythingslib.utils.CheckInternet;
import com.fourshape.easythingslib.utils.CommonSharedData;
import com.fourshape.easythingslib.utils.FormattedData;
import com.fourshape.numbermatch.GameSessionActivity;
import com.fourshape.numbermatch.HowToPlayActivity;
import com.fourshape.numbermatch.R;
import com.fourshape.numbermatch.app_ads.PlacementIds;
import com.fourshape.numbermatch.app_ads.UserConsentDialog;
import com.fourshape.numbermatch.listeners.OnFragmentChangeListener;
import com.fourshape.numbermatch.listeners.OnTutorialOptionChooseListener;
import com.fourshape.numbermatch.puzzle_core.GameLevel;
import com.fourshape.numbermatch.puzzle_core.structure.GameData;
import com.fourshape.numbermatch.ui_popups.PopupAdBeforeGameStart;
import com.fourshape.numbermatch.ui_popups.PopupGameLevel;
import com.fourshape.numbermatch.app_color.AppColor;
import com.fourshape.numbermatch.ui_popups.PopupTutorial;
import com.fourshape.numbermatch.utils.BundleParams;
import com.fourshape.numbermatch.puzzle_core.GameType;
import com.fourshape.numbermatch.utils.MakeLog;
import com.fourshape.numbermatch.utils.PlaySound;
import com.fourshape.numbermatch.utils.SharedData;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;

import java.lang.ref.WeakReference;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    private View mainView;

    private MaterialCardView dailyPlayCV;
    private ImageView dailyRightArrowIV, timelapseIV;
    private TextView appTitleTV, dayTV, monthTV;
    private LinearLayoutCompat dayTVLL;
    private MaterialButton newGameMB, savedGameMB;

    private OnFragmentChangeListener onFragmentChangeListener;

    private boolean shouldHeadToDailySession = false;

    private int dailyDay = -1, dailyMonth = -1, dailyYear = -1;

    private AppColor appColor;
    private PopupPrivacyPolicy popupPrivacyPolicy;

    private int gameLevel = -1;

    private AudioAttributes audioAttributes;
    private SoundPool soundPool;
    private int[] soundId = new int[4];

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.fragment_home, container, false);

        appColor = new AppColor();

        dailyPlayCV = mainView.findViewById(R.id.daily_cv);
        newGameMB = mainView.findViewById(R.id.new_game_mb);
        savedGameMB = mainView.findViewById(R.id.saved_game_mb);

        dailyRightArrowIV = mainView.findViewById(R.id.right_arrow_iv);
        timelapseIV = mainView.findViewById(R.id.timelapse_iv);

        appTitleTV = mainView.findViewById(R.id.app_title_home);
        dayTV = mainView.findViewById(R.id.daily_day_tv);
        monthTV = mainView.findViewById(R.id.daily_month_tv);

        dayTVLL = mainView.findViewById(R.id.day_ll);

        popupPrivacyPolicy = new PopupPrivacyPolicy(getContext(), getActivity());
        popupPrivacyPolicy.setOnPolicyStatusListener(onPolicyStatusListener);
        popupPrivacyPolicy.setOnPolicyPopupDismissListener(onPolicyPopupDismissListener);

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

        return mainView;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshViewsColor();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (soundPool != null)
            soundPool.release();
    }

    public HomeFragment setOnFragmentChangeListener(OnFragmentChangeListener onFragmentChangeListener) {
        this.onFragmentChangeListener = onFragmentChangeListener;
        return this;
    }

    @SuppressLint("SetTextI18n")
    private void setDataInViews () {

        if (getContext() == null)
            return;

        if (new SharedData(getContext()).getSavedGeneralGame() != null) {

            GameData gameData = new Gson().fromJson(new SharedData(getContext()).getSavedGeneralGame(), GameData.class);

            if (gameData != null) {

                String text = "Continue" + "\n" + getGameLevel(gameData.getLevel()) + " \u2022 " + FormattedData.getFormattedTime(gameData.getTime());

                savedGameMB.setText(text);

                savedGameMB.setTextColor(this.getContext().getColor(this.appColor.getHomePrimaryBtnTextColor()));
                savedGameMB.setBackgroundTintList(ColorStateList.valueOf(this.getContext().getColor(this.appColor.getHomePrimaryBtnBackgroundColor())));

                if (savedGameMB.getVisibility() != View.VISIBLE)
                    savedGameMB.setVisibility(View.VISIBLE);

                newGameMB.setBackgroundTintList(ColorStateList.valueOf(this.getContext().getColor(this.appColor.getSuccessBackgroundColor())));
                newGameMB.setTextColor(this.getContext().getColor(this.appColor.getHomePrimaryBtnTextColor()));

                if (newGameMB.getVisibility() != View.VISIBLE)
                    newGameMB.setVisibility(View.VISIBLE);

            } else {

                newGameMB.setBackgroundTintList(ColorStateList.valueOf(this.getContext().getColor(this.appColor.getSuccessBackgroundColor())));
                newGameMB.setTextColor(this.getContext().getColor(this.appColor.getHomePrimaryBtnTextColor()));

                savedGameMB.setVisibility(View.INVISIBLE);

            }

        } else {

            newGameMB.setBackgroundTintList(ColorStateList.valueOf(this.getContext().getColor(this.appColor.getSuccessBackgroundColor())));
            newGameMB.setTextColor(this.getContext().getColor(this.appColor.getHomePrimaryBtnTextColor()));

            savedGameMB.setVisibility(View.INVISIBLE);


        }

        DateData cDate = CurrentCalendar.getCurrentDateData();

        if (new SharedData(getContext()).getSavedDailyGame() != null) {

            GameData gameData = new Gson().fromJson(new SharedData(getContext()).getSavedDailyGame(), GameData.class);

            if (gameData != null) {

                dailyDay = gameData.getDay();
                dailyMonth = gameData.getMonth();
                dailyYear = gameData.getYear();

                if (dailyDay != -1 && dailyMonth != -1 && dailyYear != -1) {

                    if (dailyDay == cDate.getDay() && dailyMonth == cDate.getMonth() && dailyYear == cDate.getYear()) {

                        if (timelapseIV != null) {
                            timelapseIV.setVisibility(View.VISIBLE);
                        }

                        dayTV.setText(String.valueOf(dailyDay));
                        monthTV.setText(FormattedData.getMonth(dailyMonth));

                    } else {
                        timelapseIV.setVisibility(View.GONE);
                        dayTV.setText(String.valueOf(cDate.getDay()));
                        monthTV.setText(FormattedData.getMonth(cDate.getMonth()));
                    }

                } else {
                    timelapseIV.setVisibility(View.GONE);
                    dayTV.setText(String.valueOf(cDate.getDay()));
                    monthTV.setText(FormattedData.getMonth(cDate.getMonth()));
                }

            } else {
                timelapseIV.setVisibility(View.GONE);
                dayTV.setText(String.valueOf(cDate.getDay()));
                monthTV.setText(FormattedData.getMonth(cDate.getMonth()));
            }

        } else {
            timelapseIV.setVisibility(View.GONE);
            dayTV.setText(String.valueOf(cDate.getDay()));
            monthTV.setText(FormattedData.getMonth(cDate.getMonth()));
        }

    }

    public CommonSharedData getCommonSharedData () {
        return new CommonSharedData(getContext(), SharedData.SHARED_PREF_TITLE);
    }

    public String getGameLevel (int gameLevel) {

        MakeLog.info(TAG, "GameLevel Index: " + gameLevel);

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

    private Activity getWeakActivity () {
        return new WeakReference<Activity>(getActivity()).get();
    }

    private Context getWeakContext () {
        return new WeakReference<Context>(getContext()).get();
    }

    private void setViewsListener () {

        if (newGameMB != null) {
            newGameMB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    PlaySound.now(soundPool, soundId[0]);
                    if (!getCommonSharedData().getPolicyStatus()) {
                        popupPrivacyPolicy.show();
                    } else {
                        new PopupGameLevel(getWeakContext(), getWeakActivity()).setAdPlacementId(PlacementIds.INTERSTITIAL_ON_GAME_START_AD).show();
                    }

                }
            });
        }

        if (savedGameMB != null) {
            savedGameMB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    PlaySound.now(soundPool, soundId[0]);
                    Intent intent = new Intent(view.getContext(), GameSessionActivity.class);
                    intent.putExtra(BundleParams.GAME_TYPE, GameType.SAVED_GENERAL);
                    startActivity(intent);
                    //new PopupAdBeforeGameStart(getWeakContext()).with(getWeakActivity(), intent).setAdPlacementId(PlacementIds.INTERSTITIAL_ON_GAME_START_AD).show();

                }
            });
        }

        if (dailyPlayCV != null) {
            dailyPlayCV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    PlaySound.now(soundPool, soundId[0]);

                    if (onFragmentChangeListener != null) {
                        onFragmentChangeListener.onDailyFragmentLaunch();
                    }

                    //startDailyGameSession();

                }
            });
        }

    }

    private void startDailyGameSession () {

        if (getContext() == null)
            return;

        Intent intent = new Intent(getContext(), GameSessionActivity.class);

        if (timelapseIV.getVisibility() == View.VISIBLE) {

            intent.putExtra(BundleParams.GAME_TYPE, GameType.SAVED_DAILY);
            intent.putExtra(BundleParams.DAILY_GAME_DAY, dailyDay);
            intent.putExtra(BundleParams.DAILY_GAME_MONTH, dailyMonth);
            intent.putExtra(BundleParams.DAILY_GAME_YEAR, dailyYear);

        } else {

            intent.putExtra(BundleParams.GAME_TYPE, GameType.DAILY);

            DateData cDate = CurrentCalendar.getCurrentDateData();
            intent.putExtra(BundleParams.DAILY_GAME_DAY, cDate.getDay());
            intent.putExtra(BundleParams.DAILY_GAME_MONTH, cDate.getMonth());
            intent.putExtra(BundleParams.DAILY_GAME_YEAR, cDate.getYear());

        }

        startActivity(intent);

        /*
        if (new CommonSharedData(getContext(), SharedData.SHARED_PREF_TITLE).isFirstTime())
            getContext().startActivity(intent);
        else
            new PopupAdBeforeGameStart(getContext()).with(getActivity(), intent).setAdPlacementId(PlacementIds.INTERSTITIAL_ON_DAILY_GAME_START_AD).show();

         */

    }

    public void refreshViewsColor () {

        if (getContext() == null) {
            MakeLog.error(TAG, "Null Context before color refresh.");
            return;
        }

        appColor.setTheme(getCommonSharedData().getAppCurrentTheme());

        if (mainView != null) {
            mainView.setBackgroundColor(this.getContext().getColor(appColor.getAppBackgroundColor()));
        }

        if (appTitleTV != null) {
            appTitleTV.setTextColor(this.getContext().getColor(appColor.getHomeAppTitleColor()));
        }

        if (dailyPlayCV != null) {
            dailyPlayCV.setCardBackgroundColor(this.getContext().getColor(appColor.getHomeDailyPlayCardBackgroundColor()));
        }

        if (dayTVLL != null) {
            //dayTVLL.setBackgroundColor(this.getContext().getColor(appColor.getHomeDailyDayTextBackgroundColor()));
        }

        if (dayTV != null) {
            dayTV.setTextColor(this.getContext().getColor(appColor.getHomeDailyDayTextColor()));
        }

        if (monthTV != null) {
            monthTV.setTextColor(this.getContext().getColor(appColor.getHomeDailyMonthTextColor()));
        }

        if (dailyRightArrowIV != null) {
            dailyRightArrowIV.setImageTintList(ColorStateList.valueOf(this.getContext().getColor(appColor.getHomeDailyPlayRightArrowTintColor())));
        }

        if (savedGameMB != null) {
            savedGameMB.setBackgroundTintList(ColorStateList.valueOf(this.getContext().getColor(this.appColor.getPrimaryBtnBackgroundColor())));
            savedGameMB.setTextColor(this.getContext().getColor(this.appColor.getPrimaryBtnTextColor()));
        }

        if (newGameMB != null) {
            newGameMB.setBackgroundTintList(ColorStateList.valueOf(this.getContext().getColor(this.appColor.getPrimaryBtnBackgroundColor())));
            newGameMB.setTextColor(this.getContext().getColor(this.appColor.getPrimaryBtnTextColor()));
        }

        if (timelapseIV != null) {
            timelapseIV.setImageTintList(ColorStateList.valueOf(this.getContext().getColor(this.appColor.getHomeDailyDayTextBackgroundColor())));
        }

        setDataInViews();

    }

    private final OnTutorialOptionChooseListener onTutorialOptionChooseListener = new OnTutorialOptionChooseListener() {
        @Override
        public void onOpen() {
            if (getContext() != null)
                startActivity(new Intent(getContext(), HowToPlayActivity.class));
        }

        @Override
        public void onSkip() {

            if (shouldHeadToDailySession) {
                startDailyGameSession();
            } else {
                if (getContext() != null && getActivity() != null)
                    new PopupGameLevel(getContext(), getActivity()).show();
            }

        }
    };

    private final OnPolicyPopupDismissListener onPolicyPopupDismissListener = new OnPolicyPopupDismissListener() {
        @Override
        public void onDismiss() {

            if (getContext() != null && getActivity() != null) {
                if (!new SharedData(getContext()).isTutorDialogShown()) {
                    new PopupTutorial(getContext()).setOnTutorialOptionChooseListener(onTutorialOptionChooseListener).show();
                } else {
                    new PopupGameLevel(getContext(), getActivity()).show();
                }
            }

        }
    };

    private final OnPolicyStatusListener onPolicyStatusListener = new OnPolicyStatusListener() {
        @Override
        public void onAccepted() {

            getCommonSharedData().policyStatus(true);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (getActivity() != null) {
                        new UserConsentDialog(getActivity()).setOnAdConsentDialogListener(popupPrivacyPolicy.getAdConsentDialogListener()).loadForm();
                    }

                }
            }, 1500);

        }

        @Override
        public void onRejected() {
            getCommonSharedData().policyStatus(false);

            if (getContext() != null) {
                new PopupPolicyRequired(getContext()).setOnPolicyRequiredMessageListener(onPolicyRequiredMessageListener).show();
            }

        }
    };

    private final OnPolicyRequiredMessageListener onPolicyRequiredMessageListener = new OnPolicyRequiredMessageListener() {
        @Override
        public void onOkay() {
            new PopupPrivacyPolicy(getContext(), getActivity()).setOnPolicyStatusListener(onPolicyStatusListener).show();
        }

        @Override
        public void onExitApp() {

            if (getActivity() != null)
                getActivity().finish();

        }
    };

}